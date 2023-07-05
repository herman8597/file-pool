package io.filpool.pool.service.impl;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.framework.util.DateUtil;
import io.filpool.framework.util.InviteCode;
import io.filpool.framework.util.PrimaryKeyUtil;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.SupplementDeductMapper;
import io.filpool.pool.mapper.SupplementMapper;
import io.filpool.pool.param.SupplementDeductPageParam;
import io.filpool.pool.request.SysDeductSupRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.param.SupplementPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.BigDecimalUtil;
import org.fusesource.jansi.AnsiRenderer;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 算力补单 服务实现类
 *
 * @author filpool
 * @since 2021-03-29
 */
@Slf4j
@Service
public class SupplementServiceImpl extends BaseServiceImpl<SupplementMapper, Supplement> implements SupplementService {

    @Autowired
    private SupplementMapper supplementMapper;

    @Autowired
    private SysUtilController sysUtilController;

    @Autowired
    private AssetAccountService assetAccountService;

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private AssetAccountLogService assetAccountLogService;

    @Autowired
    private TransferRecordServiceImpl transferRecordService;
    @Autowired
    private PowerOrderService powerOrderService;
    @Autowired
    private SupplementDeductService deductService;
    @Autowired
    private RewardRecordService rewardRecordService;
    @Autowired
    private InviteRecordService inviteRecordService;
    @Autowired
    private UserService userService;

    @Autowired
    private SupplementDeductMapper supplementDeductMapper;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private CurrencyConfigService currencyConfigService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSupplement(Supplement supplement) throws Exception {
        //根据用户账号获取用户id
        Date now = new Date();
        Long userId = sysUtilController.queryUserId(supplement.getAccount());
        //判断资金账户是否存在
        assetAccountService.checkAccount(userId);
        //根据用户id查询用户资产
        AssetAccount fil = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, userId).eq(AssetAccount::getCurrencyId, supplement.getPowerCurrencyId()).one();
        BigDecimal available = fil.getAvailable();

        BigDecimal gas$pledge = BigDecimalUtil.add(supplement.getPledgePrice(), supplement.getGasPrice());

        //判断用户资产是否大于质押+gas
        if (BigDecimalUtil.less(available, gas$pledge)) {
            throw new FILPoolException("supplements.assetAccount");
        }
        supplement.setCreateTime(now);
        supplement.setUpdateTime(now);
        //生成补单编号
//        supplement.setId(new PrimaryKeyUtil(redisTemplate).getOrderId(now));
        return super.save(supplement);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSupplement(Supplement supplement) throws Exception {
        return super.updateById(supplement);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSupplement(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Supplement> getSupplementPageList(SupplementPageParam supplementPageParam) throws Exception {
        Page<Supplement> page = new PageInfo<>(supplementPageParam, OrderItem.desc(getLambdaColumn(Supplement::getCreateTime)));
        LambdaQueryWrapper<Supplement> wrapper = new LambdaQueryWrapper<>();
        if (supplementPageParam.getId() != null) {
            wrapper.eq(Supplement::getId, supplementPageParam.getId());
        }
        if (supplementPageParam.getAccount() != null) {
            wrapper.inSql(Supplement::getUId, "'" + sysUtilController.queryUserId(supplementPageParam.getAccount()) + "'");
        }
        if (supplementPageParam.getType() != null) {
            wrapper.eq(Supplement::getType, supplementPageParam.getType());
        }
        if (supplementPageParam.getStatus() != null) {
            wrapper.eq(Supplement::getStatus, supplementPageParam.getStatus());
        }
        if (supplementPageParam.getStartTime() != null) {
            wrapper.ge(Supplement::getCreateTime, supplementPageParam.getStartTime());
        }
        if (supplementPageParam.getEndTime() != null) {
            wrapper.le(Supplement::getCreateTime, supplementPageParam.getEndTime());
        }

        IPage<Supplement> iPage = supplementMapper.selectPage(page, wrapper);
        for (Supplement supplement : iPage.getRecords()) {
            //根据用户id查询用户账号
            if (supplement.getUId() != null) {
                supplement.setAccount(sysUtilController.queryAccount(Long.parseLong(supplement.getUId().toString())));
            } else {
                supplement.setAccount("");
            }
            //根据币种id查询币种名称
            if (supplement.getAssetId() != null) {
                supplement.setAssetName(sysUtilController.querySymbol(Integer.parseInt(supplement.getAssetId().toString())));
            } else {
                supplement.setAssetName("");
            }
        }
        return new Paging<Supplement>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean audit(Supplement request) throws Exception {
        //审批通过
        Supplement supplement = getById(request.getId());
        if (supplement == null) {
            throw new FILPoolException("order.not-exits");
        }
        if (supplement.getStatus() != 0) {
            throw new FILPoolException("order.has-audit");
        }
        String key = "sys_supplement:" + supplement.getId();
        if (redisLock.lock(key, 60)) {
            try {
                if (request.getStatus() == 1) {
                    //根据用户账号获取用户id
//                    Long userId = sysUtilController.queryUserId(supplement.getUId());
                    assetAccountService.checkAccount(supplement.getUId());
                    //根据用户id查询用户资产
                    AssetAccount fil = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, supplement.getUId()).eq(AssetAccount::getSymbol, supplement.getPowerSymbol()).one();
                    BigDecimal available = fil.getAvailable();
                    BigDecimal gas$pledge = BigDecimalUtil.add(supplement.getPledgePrice(), supplement.getGasPrice());
                    if (available.compareTo(gas$pledge) < 0) {
                        throw new FILPoolException("transfer.account.balance-low");
                    }
                    //根据用户id修改用户fil资产
                    boolean updateFil = assetAccountService.lambdaUpdate()
                            .setSql("available = available - " + gas$pledge.stripTrailingZeros().toPlainString())
                            .setSql("pledge = pledge + " + supplement.getPledgePrice().stripTrailingZeros().toPlainString())
                            .eq(AssetAccount::getUserId, supplement.getUId())
                            .eq(AssetAccount::getCurrencyId, supplement.getPowerCurrencyId()).update();
                    if (!updateFil) {
                        throw new FILPoolException("asset.modification");
                    }
                    //刷新资产
                    fil = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, supplement.getUId()).eq(AssetAccount::getSymbol, supplement.getPowerSymbol()).one();
                    //新增划转记录
                    if (supplement.getPledgePrice().compareTo(BigDecimal.ZERO) > 0) {
                        TransferRecord transferRecord = new TransferRecord().
                                setUserId(supplement.getUId()).setCreateTime(new Date()).
                                setOperationType(1).setAmount(supplement.getPledgePrice())
                                .setType(1).setCurrencyId(fil.getCurrencyId())
                                .setSymbol(fil.getSymbol()).setOrderId(supplement.getId());
                        transferRecordService.save(transferRecord);
                        assetAccountLogService.saveLog(fil, supplement.getPledgePrice(), AccountLogType.TYPE_SYSTEM_TRANSFER, "补单质押金额", supplement.getId());
                    }
                    //gas费记录
                    if (supplement.getGasPrice().compareTo(BigDecimal.ZERO) > 0) {
                        assetAccountLogService.saveLog(fil, supplement.getGasPrice(), AccountLogType.TYPE_GAS, "补单GAS费", supplement.getId());
                    }
                    //增加算力订单
                    powerOrderService.addPowerOrder(supplement.getUId(), supplement.getTbSum(), supplement.getType() == 1 ? 3 : supplement.getType() == 2 ? 2 : 5, supplement.getId(), supplement.getContractDays(),
                            supplement.getPowerSymbol());
                    //发放补单奖励
                    InviteRecord inviteOne = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, supplement.getUId()).one();
                    if (inviteOne != null && inviteOne.getInviteUserId() != null) {
                        rewardRecordService.supplementReward(supplement);
                    }
                    //增加经验值
                    CurrencyConfig expRateConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId, supplement.getPowerCurrencyId())
                            .eq(CurrencyConfig::getType, 1).one();
                    BigDecimal exp = supplement.getTbSum().multiply(BigDecimal.ONE.divide(expRateConfig.getAmount(), 8, BigDecimal.ROUND_DOWN));
                    boolean update = userService.lambdaUpdate().eq(User::getId, supplement.getUId())
                            .setSql("experience = experience + " + exp.stripTrailingZeros().toPlainString())
                            .setSql("community_experience=community_experience + " + exp.stripTrailingZeros().toPlainString())
                            .update();
                    if (!update) {
                        //更新失败，抛异常回滚
                        throw new RuntimeException("更新用户经验值失败");
                    }

                    Order order = new Order();
                    order.setUserId(request.getUId());
                    order.setTotalAmount(request.getAmountPrice());
                    order.setGoodType(supplement.getType() == 1 ? 2 : supplement.getType() == 2 ? 1 : 3);
                    order.setQuantity(Integer.parseInt(request.getTbSum().toString()));
                    order.setPowerSymbol(request.getPowerSymbol());
                    order.setOrderNumber(request.getId().toString());
                    order.setContractDays(request.getContractDays());
                    //2,表示补单
                    rewardRecordService.communityRewards(order, exp,2);

                    supplement.setStatus(1);
                    supplement.setRemark(request.getRemark());
                    updateSupplement(supplement);
                    return true;
                } else {
                    supplement.setStatus(2);
                    supplement.setRemark(request.getRemark());
                    updateSupplement(supplement);
                    return true;
                }
            } finally {
                redisLock.unlock(key);
            }
        } else {
            return false;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deduct(SysDeductSupRequest request) throws Exception {
//        if (request.getAccount() == null) {
//            throw new FILPoolException("supplement.account.not-empty");
//        }
        if (request.getId() == null) {
            throw new FILPoolException("supplement.id.not-empty");
        }
        if (request.getAmount() == null) {
            throw new FILPoolException("supplement.amount.not-empty");
        }
        Supplement supplement = supplementMapper.selectById(request.getId());
        if (supplement == null) {
            throw new FILPoolException("order.not-exits");
        }
        if (supplement.getTbSum().compareTo(request.getAmount()) < 0) {
            throw new FILPoolException("supplement.amount.has-short");
        }
        Date now = new Date();
        //增加补单扣除记录
        SupplementDeduct deduct = new SupplementDeduct();
        deduct.setDeductSum(request.getAmount());
        deduct.setTbSum(supplement.getTbSum());
        deduct.setCreateTime(now);
        deduct.setOrderId(supplement.getId());
        deduct.setUserId(supplement.getUId());
        deduct.setPowerSymbol(supplement.getPowerSymbol());
        deductService.saveSupplementDeduct(deduct);
        //修改补单
        supplement.setTbSum(supplement.getTbSum().subtract(request.getAmount()));
        supplement.setUpdateTime(now);
        updateSupplement(supplement);
        //修改算力订单
        Integer type = supplement.getType() == 1 ? 3 : supplement.getType() == 2 ? 2 : 5;
        PowerOrder powerOrder = powerOrderService.lambdaQuery().eq(PowerOrder::getType, type)
                .eq(PowerOrder::getRecordId, supplement.getId()).one();
        if (powerOrder == null) {
            throw new FILPoolException("order.not-exits");
        }
        //修改经验值,扣除对应的经验
        CurrencyConfig expConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId, supplement.getPowerCurrencyId())
                .eq(CurrencyConfig::getType, 1).one();
        BigDecimal exp = request.getAmount().multiply(BigDecimal.ONE.divide(expConfig.getAmount(), 8, BigDecimal.ROUND_DOWN));
        boolean update = userService.lambdaUpdate().eq(User::getId, supplement.getUId())
                .setSql("experience = experience - " + exp.stripTrailingZeros().toPlainString()).update();
        if (!update) {
            //更新失败，抛异常回滚
            throw new RuntimeException("更新用户经验值失败");
        }
        powerOrder.setAmount(supplement.getTbSum());
        powerOrderService.updatePowerOrder(powerOrder);
        return true;
    }

    @Override
    public Paging<SupplementDeduct> getSupplementDeductPageList(SupplementDeductPageParam supplementDeductPageParam) {
        Page<SupplementDeduct> page = new PageInfo<>(supplementDeductPageParam, OrderItem.desc(getLambdaColumn(Supplement::getCreateTime)));
        LambdaQueryWrapper<SupplementDeduct> wrapper = new LambdaQueryWrapper<>();
        if (supplementDeductPageParam.getOrderId() != null) {
            wrapper.eq(SupplementDeduct::getOrderId, supplementDeductPageParam.getOrderId());
        }
        if (supplementDeductPageParam.getAccount() != null) {
            wrapper.eq(SupplementDeduct::getUserId, sysUtilController.queryUserId(supplementDeductPageParam.getAccount()));
        }
        if (supplementDeductPageParam.getStartTime() != null) {
            wrapper.ge(SupplementDeduct::getCreateTime, supplementDeductPageParam.getStartTime());
        }
        if (supplementDeductPageParam.getEndTime() != null) {
            wrapper.le(SupplementDeduct::getCreateTime, supplementDeductPageParam.getEndTime());
        }

        Page<SupplementDeduct> supplementDeductPage = supplementDeductMapper.selectPage(page, wrapper);
        for (SupplementDeduct supplementDeduct : supplementDeductPage.getRecords()) {
            //根据用户id查询用户账号
            supplementDeduct.setAccount(sysUtilController.queryAccount(Long.parseLong(supplementDeduct.getUserId().toString())));
            //订单实际算力
            supplementDeduct.setActualTB(BigDecimalUtil.sub(supplementDeduct.getActualTB(), supplementDeduct.getDeductSum()));
        }
        return new Paging<SupplementDeduct>(supplementDeductPage);
    }
}
