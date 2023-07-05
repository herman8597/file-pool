package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.framework.util.*;
import io.filpool.pool.entity.*;
import io.filpool.pool.model.PowerPledgeVo;
import io.filpool.pool.request.ConfirmOrderRequest;
import io.filpool.pool.request.CreateOrderRequest;
import io.filpool.pool.request.OrderDetailRequest;
import io.filpool.pool.request.OrderPageRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.log.annotation.Module;
import org.apache.commons.lang3.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/order")
@Module("pool")
@Api(value = "订单表API", tags = {"订单API"})
public class OrderController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AssetAccountService assetService;
    @Autowired
    private AssetAccountLogService assetAccountLogService;
    @Autowired
    private RewardRecordService rewardRecordService;
    @Autowired
    private InviteRecordService inviteRecordService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TransferRecordService transferRecordService;

    @Autowired
    private PowerOrderService powerOrderService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CurrencyConfigService currencyConfigService;

    /**
     * 创建订单
     */
    @PostMapping("/createOrder")
    @CheckLogin
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("创建订单")
    public ApiResult<String> createOrder(@RequestBody CreateOrderRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        Goods goods = goodsService.getById(request.getGoodId());
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new FILPoolException("order.quantity.not-empty");
        }
        //商品是否可购买
        if (goods.getStatus() != 1) {
            throw new FILPoolException("order.goods.not.enable");
        }
        //判断商品限制
        if (goods.getMinLimit() != null && goods.getMinLimit() != 0 && request.getQuantity() < goods.getMinLimit()) {
            throw new FILPoolException("order.quantity.min-limit");
        }
        if (goods.getMaxLimit() != null && goods.getMaxLimit() != 0 && request.getQuantity() > goods.getMaxLimit()) {
            throw new FILPoolException("order.quantity.max-limit");
        }
        String key = "create_order:" + user.getId();
        if (redisLock.lock(key, 60)) {
            try {
                Order order = new Order();
                order.setGoodId(request.getGoodId());
                order.setGoodName(goods.getName());
                order.setPrice(goods.getPrice());
                order.setDiscountPrice(goods.getDiscountPrice());
                order.setQuantity(request.getQuantity());
                BigDecimal totalAmount;
                if (order.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                    totalAmount = order.getDiscountPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
                } else {
                    totalAmount = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
                }
                order.setTotalAmount(totalAmount);
                order.setStatus(1);
                order.setUserId(user.getId());
                //支付币种默认设为usdt
                Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, "USDT")
                        .eq(Currency::getSeries, "TRX").one();
                if (currency == null) {
                    throw new FILPoolException("transfer.currency.non-exits");
                }
                order.setCurrencyId(currency.getId());
                //生成订单编号 时间字符串+时间生成的6位字符串
                Date date = new Date();
                order.setOrderNumber(DateUtil.getDateTimeNumberString(date) + InviteCode.instance().gen(date.getTime()).toLowerCase());
                order.setCreateTime(date);
                order.setUpdateTime(date);
                order.setGoodType(goods.getType());
                order.setTotalPower(goods.getPower().multiply(new BigDecimal(order.getQuantity())));
                //未设置商品合约天数则默认180天
                order.setContractDays(goods.getContractPeriod());
                order.setPowerCurrencyId(goods.getCurrencyId());
                order.setPowerSymbol(goods.getSymbol());
                if (goods.getType() == 2&&StringUtils.equals(goods.getSymbol(),"FIL") && goods.getTbPledge().compareTo(BigDecimal.ZERO) >0) {
                    //FIL云算力计算质押
                    order.setPledgeAmount(order.getTotalPower().multiply(goods.getTbPledge()));
                }else{
                    order.setPledgeAmount(BigDecimal.ZERO);
                }
                //创建订单
                orderService.saveOrder(order);
                //缓存订单编号
                redisUtil.set(Constants.ORDER_NUMBER_KEY + order.getOrderNumber(), order.getOrderNumber(), Constants.ORDER_KEY_EXPIRE);
                return ApiResult.ok(order.getOrderNumber());
            } finally {
                redisLock.unlock(key);
            }
        } else {
            throw new FILPoolException("system.lock.err");
        }
    }

    /**
     * 确认订单
     */
    @PostMapping("/confirmOrder")
    @CheckLogin
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("确认订单")
    public ApiResult<Boolean> ConfirmOrder(@RequestBody ConfirmOrderRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        Order order = orderService.lambdaQuery().eq(Order::getOrderNumber, request.getOrderNumber())
                .eq(Order::getUserId, user.getId()).one();
        if (order == null) {
            throw new FILPoolException("order.not-exits");
        }
        if (order.getStatus() != 1) {
            throw new FILPoolException("order.not.enable");
        }
        //查询商品状态是否可用
        Goods goods = goodsService.getById(order.getGoodId());
        //商品是否可购买
        if (goods.getStatus() != 1) {
            throw new FILPoolException("order.goods.not.enable");
        }
        //判断商品限制
        if (goods.getMinLimit() != null && goods.getMinLimit() != 0 && order.getQuantity() < goods.getMinLimit()) {
            throw new FILPoolException("order.quantity.min-limit");
        }
        if (goods.getQuantity() < order.getQuantity()) {
            throw new FILPoolException("order.quantity.out-limit");
        }
        if (goods.getMaxLimit() != null && goods.getMaxLimit() != 0 && order.getQuantity() > goods.getMaxLimit()) {
            throw new FILPoolException("order.quantity.max-limit");
        }
        //未设置交易密码
        if (StringUtils.isEmpty(user.getPayPassword())) {
            throw new FILPoolException("user.paypwd.not-set");
        }
        //校验密码
        String encrypt = PasswordUtil.encrypt(request.getPayPwd(), user.getSalt());
        if (!StringUtils.equals(encrypt, user.getPayPassword())) {
            throw new FILPoolException("user.paypwd.err");
        }
        //判断资金余额
        assetService.checkAccount(user.getId());
        AssetAccount account = assetService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
                .eq(AssetAccount::getCurrencyId, order.getCurrencyId()).one();
        if (account == null) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        if (account.getAvailable().compareTo(order.getTotalAmount()) < 0) {
            throw new FILPoolException("transfer.account.balance-low");
        }
        if (order.getPledgeAmount() != null && order.getPledgeAmount().compareTo(BigDecimal.ZERO) > 0) {
            //质押账户
            AssetAccount pledgeAccount = assetService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
                    .eq(AssetAccount::getCurrencyId, order.getPowerCurrencyId()).one();
            if (pledgeAccount == null) {
                throw new FILPoolException("transfer.currency.non-exits");
            }
            if (pledgeAccount.getAvailable().compareTo(order.getPledgeAmount()) < 0) {
                throw new FILPoolException("transfer.account.balance-low");
            }
        }
        String key = "confirm_order:" + user.getId();
        if (redisLock.lock(key, 60)) {
            try {
                //存储社区奖励经验值
                BigDecimal expPlus=BigDecimal.ZERO;

                //更新可用余额
                Date now = new Date();
                boolean isUpdate = assetService.lambdaUpdate().eq(AssetAccount::getId, account.getId())
                        .setSql("available = available -"+order.getTotalAmount().stripTrailingZeros().toPlainString())
                        .update();
                if (!isUpdate){
                    throw new FILPoolException("asset.modification");
                }
                //刷新资产
                account = assetService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
                        .eq(AssetAccount::getCurrencyId, order.getCurrencyId()).one();
//                account.setAvailable(account.getAvailable().subtract(order.getTotalAmount()));
//                account.setUpdateTime(now);
//                assetService.updateAssetAccount(account);
                //更新商品信息
                goods.setQuantity(goods.getQuantity() - order.getQuantity());
                goods.setSoldQuantity(goods.getSoldQuantity() + order.getQuantity());
                goods.setUpdateTime(now);
                goodsService.updateGoods(goods);
                //移除订单编号缓存
                redisUtil.remove(Constants.ORDER_NUMBER_KEY + order.getOrderNumber());
                //插入相关资金记录
                switch (goods.getType()) {
                    case 1:
                        assetAccountLogService.saveLog(account, order.getTotalAmount(), AccountLogType.TYPE_BUY_MACHINE, "购买矿机", order.getId());
                        break;
                    case 2:
                        //增加经验值
                        CurrencyConfig expRateConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId,order.getPowerCurrencyId())
                                .eq(CurrencyConfig::getType,1).one();
                        //经验值比例
                        BigDecimal exp = order.getTotalPower().multiply(BigDecimal.ONE.divide(expRateConfig.getAmount(),8,BigDecimal.ROUND_DOWN));
                        expPlus=exp;
                        user.setExperience(user.getExperience().add(exp));
                        //增加社区等级的经验值，1T=1经验
                        user.setCommunityExperience(user.getCommunityExperience().add(exp));
                        userService.updateUser(user);
                        //设置为有效算力
                        order.setIsEffect(true);
                        //云算力直接给用户增加算力
                        powerOrderService.addPowerOrder(user.getId(), order.getTotalPower(), 1, order.getId(), order.getContractDays(),order.getPowerSymbol());
                        assetAccountLogService.saveLog(account, order.getTotalAmount(), AccountLogType.TYPE_BUY_POWER, "购买云算力", order.getId());
                        //需要质押
                        if (order.getPledgeAmount() != null && order.getPledgeAmount().compareTo(BigDecimal.ZERO) > 0) {
                            //判断是否需要向用户划转质押,订单总算力(TB) * 每tb需要质押数量
                            //增加质押金额
                            assetService.lambdaUpdate().eq(AssetAccount::getCurrencyId, order.getPowerCurrencyId())
                                    .eq(AssetAccount::getUserId, user.getId())
                                    .setSql("available = available - " +order.getPledgeAmount().stripTrailingZeros().toPlainString())
                                    .setSql("pledge = pledge + "+order.getPledgeAmount().stripTrailingZeros().toPlainString())
                                    .update();
                            AssetAccount filAsset = assetService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
                                    .eq(AssetAccount::getCurrencyId, order.getPowerCurrencyId()).one();
//                                filAsset.setPledge(filAsset.getPledge().add(pledgeAmount));
//                                filAsset.setUpdateTime(now);
//                                assetService.updateAssetAccount(filAsset);
                            //添加质押划转记录
                            TransferRecord transferRecord = new TransferRecord();
                            transferRecord.setSymbol(filAsset.getSymbol());
                            transferRecord.setCurrencyId(filAsset.getCurrencyId());
                            transferRecord.setUserId(order.getUserId());
                            transferRecord.setAmount(order.getPledgeAmount());
                            transferRecord.setType(3);
                            transferRecord.setOperationType(1);
                            transferRecord.setOrderId(order.getId());
                            transferRecord.setCreateTime(now);
                            transferRecordService.saveTransferRecord(transferRecord);
                            //添加资产划转记录
                            assetAccountLogService.saveLog(filAsset, transferRecord.getAmount(), AccountLogType.TYPE_SYSTEM_TRANSFER, "购买商品划转质押", transferRecord.getId());
                        }
                        break;
                    case 3:
                        assetAccountLogService.saveLog(account, order.getTotalAmount(), AccountLogType.TYPE_BUY_MACHINE_GROUP, "购买矿机集群", order.getId());
                        break;
                    case 4:
                        //购买BZZ
                        //增加经验值
                        CurrencyConfig expRateConfigTwo = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId,order.getPowerCurrencyId())
                                .eq(CurrencyConfig::getType,1).one();
                        //经验值比例
                        BigDecimal expTwo = order.getTotalPower().multiply(BigDecimal.ONE.divide(expRateConfigTwo.getAmount(),8,BigDecimal.ROUND_DOWN));
                        expPlus=expTwo;
                        user.setExperience(user.getExperience().add(expTwo));
                        //增加社区等级的经验值，1T=1经验
                        user.setCommunityExperience(user.getCommunityExperience().add(expTwo));
                        userService.updateUser(user);
                        //设置为有效算力
                        order.setIsEffect(true);
                        //BZZ直接给用户增加算力
                        powerOrderService.addPowerOrder(user.getId(), order.getTotalPower(), 7, order.getId(), order.getContractDays(),order.getPowerSymbol());
                        assetAccountLogService.saveLog(account, order.getTotalAmount(), AccountLogType.TYPE_BUY_POWER_BZZ, "购买BZZ", order.getId());
                        //需要质押
                        if (order.getPledgeAmount() != null && order.getPledgeAmount().compareTo(BigDecimal.ZERO) > 0) {
                            //判断是否需要向用户划转质押,订单总算力(TB) * 每tb需要质押数量
                            //增加质押金额
                            assetService.lambdaUpdate().eq(AssetAccount::getCurrencyId, order.getPowerCurrencyId())
                                    .eq(AssetAccount::getUserId, user.getId())
                                    .setSql("available = available - " +order.getPledgeAmount().stripTrailingZeros().toPlainString())
                                    .setSql("pledge = pledge + "+order.getPledgeAmount().stripTrailingZeros().toPlainString())
                                    .update();
                            AssetAccount filAsset = assetService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
                                    .eq(AssetAccount::getCurrencyId, order.getPowerCurrencyId()).one();
//                                filAsset.setPledge(filAsset.getPledge().add(pledgeAmount));
//                                filAsset.setUpdateTime(now);
//                                assetService.updateAssetAccount(filAsset);
                            //添加质押划转记录
                            TransferRecord transferRecord = new TransferRecord();
                            transferRecord.setSymbol(filAsset.getSymbol());
                            transferRecord.setCurrencyId(filAsset.getCurrencyId());
                            transferRecord.setUserId(order.getUserId());
                            transferRecord.setAmount(order.getPledgeAmount());
                            transferRecord.setType(3);
                            transferRecord.setOperationType(1);
                            transferRecord.setOrderId(order.getId());
                            transferRecord.setCreateTime(now);
                            transferRecordService.saveTransferRecord(transferRecord);
                            //添加资产划转记录
                            assetAccountLogService.saveLog(filAsset, transferRecord.getAmount(), AccountLogType.TYPE_SYSTEM_TRANSFER, "购买商品划转质押", transferRecord.getId());
                        }
                        break;
                    default:
                        break;
                }
                //更新订单
                order.setStatus(2);
                order.setUpdateTime(now);
                orderService.updateOrder(order);
                InviteRecord inviteOne = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, user.getId()).one();
                if (inviteOne != null && inviteOne.getInviteUserId() != null) {
                    //发放订单奖励
                    rewardRecordService.orderReward(order);
                    //社区奖励
                    rewardRecordService.communityRewards(order,expPlus,1);
                }
            } finally {
                redisLock.unlock(key);
            }
        } else {
            throw new FILPoolException("system.lock.err");
        }
        return ApiResult.ok(true);
    }

    /**
     * 根据状态获取订单列表
     */
    @PostMapping("/getOrderListByStatus")
    @CheckLogin
    @ApiOperation("根据类型获取订单列表")
    public ApiResult<List<Order>> getOrderListByStatus(@RequestBody OrderPageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<Order> wr = Wrappers.lambdaQuery(Order.class)
                .eq(Order::getUserId, user.getId())
                .orderByDesc(Order::getCreateTime);
        if (request.getStatus() != null && request.getStatus() != 0) {
            if (request.getStatus() == 3) {
                Integer[] array = new Integer[]{3,4};
                wr.in(Order::getStatus, Arrays.stream(array).collect(Collectors.toList()));
            } else {
                wr.eq(Order::getStatus, request.getStatus());
            }
        }
        Page<Order> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = orderService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(page.getRecords());
    }

    /**
     * 订单详情
     */
    @PostMapping("/getOrderDetail")
    @CheckLogin
    @ApiOperation("获取订单详情")
    public ApiResult<OrderDetailVo> getOrderDetail(@RequestBody OrderDetailRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        Order order = orderService.lambdaQuery().eq(Order::getUserId, user.getId())
                .eq(Order::getId, request.getOrderId()).one();
        if (order == null) {
            throw new FILPoolException("order.not-exits");
        }
        OrderDetailVo vo = new OrderDetailVo();
        vo.setCurrentTime(new Date());
        BeanUtils.copyProperties(order, vo);
        return ApiResult.ok(vo);
    }

    /**
     * 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/cancelOrder")
    @CheckLogin
    @ApiOperation("取消订单")
    public ApiResult<Boolean> cancelOrder(@RequestBody OrderDetailRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        Order order = orderService.lambdaQuery().eq(Order::getUserId, user.getId())
                .eq(Order::getId, request.getOrderId()).one();
        if (order == null) {
            throw new FILPoolException("order.not-exits");
        }
        order.setStatus(3);
        return ApiResult.ok(orderService.updateOrder(order));
    }
}

