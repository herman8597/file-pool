package io.filpool.scheduled;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.Address;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.RechargeRecord;
import io.filpool.pool.service.AddressService;
import io.filpool.pool.service.AssetAccountLogService;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.impl.RechargeRecordServiceImpl;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.CloudApiRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取新充值记录任务
 */
@Component
@Slf4j
public class RechargeRecordTask {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RechargeRecordServiceImpl rechargeRecordService;
    @Autowired
    private AssetAccountService accountService;
    @Autowired
    private AssetAccountLogService accountLogService;
    @Autowired
    private CloudApiRpc cloudApiRpc;
    @Autowired
    private RedisUtil redisUtil;
    private static final String LOCK_KEY = "RechargeRecordTaskLock";

    @XxlJob("rechargeRecordTask")
    public void refreshRecord() throws Exception {
        if (redisUtil.exists(LOCK_KEY)) {
            log.info("还有任务在扫描中");
            return;
        }
        try {
            //加锁，防止多次任务乱入
            redisUtil.set(LOCK_KEY, System.currentTimeMillis(), 1000L);
            List<Address> addressList = addressService.list();
            Date now = new Date();
            //查出所有的交易记录哈希
            List<String> hashs = rechargeRecordService.lambdaQuery().select(RechargeRecord::getId, RechargeRecord::getTransHash).list()
                    .stream().map(RechargeRecord::getTransHash).collect(Collectors.toList());
            for (Address address : addressList) {
//                RechargeRecord record = rechargeRecordService.getBaseMapper().getLastRecord(address.getAddress());
//                Date lastTime = address.getCreateTime();
                Date lastTime = rechargeRecordService.getBaseMapper().getLastRecordTime(address.getAddress());
                if (lastTime == null) {
                    lastTime = address.getCreateTime();
                }
                accountService.checkAccount(address.getUserId());
                List<RechargeRecord> newRecords = cloudApiRpc.getNewRecharge(address.getAddress(), address.getSecret(), lastTime);
                if (!ObjectUtils.isEmpty(newRecords))
//                    RechargeRecord firstRecord = newRecords.get(0);
//                    String symbol = firstRecord.getSymbol().equals("USDT-TRX") ? "USDT" : firstRecord.getSymbol();
//                    AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getUserId, address.getUserId())
//                            .eq(AssetAccount::getSymbol, symbol).one();
                    for (RechargeRecord newOne : newRecords) {
                        //判断交易哈希是否已存在
                        if (ObjectUtils.isEmpty(hashs) || !hashs.contains(newOne.getTransHash())) {
                            //保存新收款记录
                            String symbol = newOne.getSymbol().equals("USDT-TRX") ? "USDT" : newOne.getSymbol();
                            AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getUserId, address.getUserId())
                                    .eq(AssetAccount::getSymbol, symbol).one();
                            if (account == null) {
                                continue;
                            }
                            try {
                                saveRecord(newOne, address, account, now);
                            } catch (Exception e) {
                                log.error("保存充值记录失败", e);
                            }
                        }

                    }

            }
        } finally {
            redisUtil.remove(LOCK_KEY);
        }
        XxlJobHelper.handleSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(RechargeRecord newOne, Address address, AssetAccount account, Date now) throws Exception {
        newOne.setUserId(address.getUserId());
        newOne.setSeries(address.getSeries());
        newOne.setSeriesId(address.getSeriesId());
        if (newOne.getSymbol().equals("USDT-TRX") || newOne.getSymbol().equals("USDT-ETH")) {
            newOne.setSymbol("USDT");
        } else {
            newOne.setSymbol(newOne.getSymbol());
        }
        rechargeRecordService.saveRechargeRecord(newOne);
        //增加金额
        boolean update = accountService.lambdaUpdate()
                .setSql("available = available + " + newOne.getAmount().stripTrailingZeros().toPlainString())
                .set(AssetAccount::getUpdateTime, now)
                .eq(AssetAccount::getId, account.getId()).update();
        if (!update) {
            //更新失败，抛异常回滚
            throw new RuntimeException("更新用户余额失败");
        }
        account = accountService.lambdaQuery().eq(AssetAccount::getId, account.getId()).one();
        //保存记录
        accountLogService.saveLog(account, newOne.getAmount(), AccountLogType.TYPE_RECHARGE, "充币成功", newOne.getId());
    }
}
