package io.filpool.pool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.AssetAccountLogMapper;
import io.filpool.pool.param.AssetsRecordPageParam;
import io.filpool.pool.request.AssetRecordsRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.param.AssetAccountLogPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.AssetRecordVo;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账户资产变化表 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class AssetAccountLogServiceImpl extends BaseServiceImpl<AssetAccountLogMapper, AssetAccountLog> implements AssetAccountLogService {

    @Autowired
    private AssetAccountLogMapper assetAccountLogMapper;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RewardRecordService rewardRecordService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAssetAccountLog(AssetAccountLog assetAccountLog) throws Exception {
        return super.save(assetAccountLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAssetAccountLog(AssetAccountLog assetAccountLog) throws Exception {
        return super.updateById(assetAccountLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAssetAccountLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<AssetAccountLog> getAssetAccountLogPageList(AssetAccountLogPageParam assetAccountLogPageParam) throws Exception {
        Page<AssetAccountLog> page = new PageInfo<>(assetAccountLogPageParam, OrderItem.desc(getLambdaColumn(AssetAccountLog::getCreateTime)));
        LambdaQueryWrapper<AssetAccountLog> wrapper = new LambdaQueryWrapper<>();
        IPage<AssetAccountLog> iPage = assetAccountLogMapper.selectPage(page, wrapper);
        return new Paging<AssetAccountLog>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveLog(AssetAccount account, BigDecimal amount, Integer type, String remark, Long recordId) throws Exception {
        AssetAccountLog log = new AssetAccountLog();
        log.setRemark(remark);
        log.setType(type);
        log.setOperationAmount(amount);
        log.setAvailable(account.getAvailable());
        log.setFrozen(account.getFrozen());
        log.setPledge(account.getPledge());
        log.setAssetAccountId(account.getId());
        log.setUserId(account.getUserId());
        log.setCreateTime(new Date());
        log.setRecordId(recordId);
        saveAssetAccountLog(log);
        return log.getId();
    }

    @Override
    public List<AssetRecordVo> getAssetRecords(AssetRecordsRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<AssetAccountLog> wrapper = Wrappers.lambdaQuery(AssetAccountLog.class)
                .eq(AssetAccountLog::getUserId, user.getId());
        assetAccountService.checkAccount(user.getId());
        Currency currency = currencyService.getByCache(request.getCurrencyId());
        AssetAccount account = assetAccountService.lambdaQuery().eq(AssetAccount::getSymbol, currency.getSymbol()).eq(AssetAccount::getUserId, user.getId()).one();
        if (account == null) {
            log.error("account == null");
            throw new FILPoolException("illegal.access");
        }
        wrapper.eq(AssetAccountLog::getAssetAccountId, account.getId());

        if (request.getType() != null && request.getType() > 0) {
            if (request.getType() > 5) {
                //过滤了划转两个类型，type需要+2
                wrapper.eq(AssetAccountLog::getType, request.getType() + 2);
            } else {
                wrapper.eq(AssetAccountLog::getType, request.getType());
            }
        } else {
            //过滤类型
            List<Integer> types = new ArrayList<>();
            types.add(AccountLogType.TYPE_SYSTEM_TRANSFER);
            types.add(AccountLogType.TYPE_TRANSFER);
            wrapper.notIn(AssetAccountLog::getType, types);
        }
        wrapper.orderByDesc(AssetAccountLog::getCreateTime);
        AssetsRecordPageParam param = new AssetsRecordPageParam();
        param.setPageIndex(request.getPageIndex());
        param.setPageSize(request.getPageSize());
        PageInfo<AssetAccountLog> pageInfo = getBaseMapper().selectPage(new PageInfo<>(param), wrapper);
        List<AssetRecordVo> vos = new ArrayList<>();
        for (AssetAccountLog log : pageInfo.getRecords()) {
            AssetRecordVo vo = new AssetRecordVo();
            vo.setSymbol(currency.getSymbol());
            vo.setAssetAccountId(account.getId());
            switch (log.getType()) {
                case 1:
                    //充币
                    RechargeRecord recharge = rechargeRecordService.getById(log.getRecordId());
                    if (recharge != null) {
                        vo.setId(log.getId());
                        vo.setRecordId(log.getId());
                        vo.setSymbol(recharge.getSymbol());
                        vo.setAmount(recharge.getAmount());
                        vo.setFromAddress(recharge.getFromAddress());
                        vo.setToAddress(recharge.getToAddress());
                        vo.setStatus(recharge.getType() == 3 ? 4 : recharge.getType());
                        vo.setHash(recharge.getTransHash());
                        vo.setFee(BigDecimal.ZERO);
                        vo.setType(1);
                        vo.setCreateTime(recharge.getCreateTime());
                        vo.setSeries(recharge.getSeries());
                        vos.add(vo);
                    }
                    break;
                case 2:
                    WithdrawRecord withdrawRecord = withdrawRecordService.getById(log.getRecordId());
                    if (withdrawRecord != null) {
                        vo.setId(log.getId());
                        vo.setStatus(withdrawRecord.getStatus() == 1 ? 3 : withdrawRecord.getStatus() == 5 ? 1 : withdrawRecord.getStatus() == 3 ? 2 : 4);
                        vo.setRecordId(log.getRecordId());
                        vo.setSymbol(withdrawRecord.getSymbol());
                        vo.setToAddress(withdrawRecord.getToAddress());
                        vo.setFromAddress("System");
                        vo.setFee(withdrawRecord.getFee());
                        vo.setCreateTime(log.getCreateTime());
                        vo.setAmount(withdrawRecord.getAmount().negate());
                        vo.setHash(withdrawRecord.getTxHash());
                        vo.setType(2);
                        vo.setSeries(withdrawRecord.getSeries());
                        vos.add(vo);
                    }
                    break;
                case 3:
                    //平台充值
                    vo.setId(log.getId());
                    vo.setType(3);
                    vo.setAmount(log.getOperationAmount());
                    vo.setRecordId(log.getRecordId());
                    vo.setCreateTime(log.getCreateTime());
                    vo.setStatus(1);
                    vos.add(vo);
                    break;
                case 4:
                    //平台扣除
                    vo.setId(log.getId());
                    vo.setType(4);
                    vo.setAmount(log.getOperationAmount());
                    vo.setRecordId(log.getRecordId());
                    vo.setCreateTime(log.getCreateTime());
                    vo.setStatus(1);
                    vos.add(vo);
                    break;
                case 5:
                    //GAS费
                    vo.setId(log.getId());
                    vo.setType(5);
                    vo.setAmount(log.getOperationAmount().negate());
                    vo.setRecordId(log.getRecordId());
                    vo.setCreateTime(log.getCreateTime());
                    vo.setStatus(1);
                    vos.add(vo);
                    break;
                case 6:
                    //划转
                    break;
                case 7:
                    //系统划转
                    break;
                case 8:
                    //购买矿机
                case 9:
                    //购买云算力
                case 10:
                    //购买矿机集群
                    Order order = orderService.getById(log.getRecordId());
                    if (order != null) {
                        vo.setId(log.getId());
                        vo.setType(log.getType() == 8 ? 6 : log.getType() == 9 ? 7 : 8);
                        vo.setAmount(order.getTotalAmount().negate());
                        vo.setRecordId(order.getId());
                        vo.setCreateTime(log.getCreateTime());
                        vo.setStatus(1);
                        vos.add(vo);
                    }
                case 11:
                    //邀请返佣
                    RewardRecord rewardRecord = rewardRecordService.getById(log.getRecordId());
                    if (rewardRecord != null) {
                        vo.setId(log.getId());
                        vo.setType(9);
                        vo.setAmount(rewardRecord.getUsdtAmount());
                        vo.setRecordId(rewardRecord.getId());
                        vo.setCreateTime(log.getCreateTime());
                        vo.setStatus(rewardRecord.getStatus() == 1 ? 1 : 2);
                        vos.add(vo);
                    }
                    break;
                case 12:
                    //首次释放
                    vo.setId(log.getId());
                    vo.setType(10);
                    vo.setAmount(log.getOperationAmount());
                    vo.setRecordId(log.getRecordId());
                    vo.setCreateTime(log.getCreateTime());
                    vo.setStatus(1);
                    vos.add(vo);
                    break;
                case 13:
                    //当日线性释放
                    vo.setId(log.getId());
                    vo.setType(11);
                    vo.setAmount(log.getOperationAmount());
                    vo.setRecordId(log.getId());
                    vo.setCreateTime(log.getCreateTime());
                    vo.setStatus(1);
                    vos.add(vo);
                    break;
                default:
                    break;
            }
        }
        return vos;
    }
}
