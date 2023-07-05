package io.filpool.scheduled;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.DateUtil;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.service.AssetAccountLogService;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.service.IncomeLogService;
import io.filpool.pool.service.IncomeReleaseLogService;
import io.filpool.pool.service.impl.IncomeReleaseRecordServiceImpl;
import io.filpool.pool.util.AccountLogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 挖矿线性释放
 */
@Component
@Slf4j
public class MiningReleaseTask {
    @Autowired
    private AssetAccountService accountService;
    @Autowired
    private AssetAccountLogService accountLogService;
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;
    @Autowired
    private IncomeReleaseLogService incomeReleaseLogService;
    @Autowired
    private IncomeLogService incomeLogService;

    @XxlJob("miningReleaseTask")
    public void release() {
        //查出需今日释放的
        Date todayStart = DateUtil.getTodayStart();
        //传入的日期参数
        String params = XxlJobHelper.getJobParam();
        if (!ObjectUtils.isEmpty(params)) {
            try {
                todayStart = new SimpleDateFormat("yyyy-MM-dd").parse(params);
                XxlJobHelper.log("参数 {}", params);
            } catch (ParseException e) {
                log.error("参数转换异常", e);
                XxlJobHelper.log(e);
                XxlJobHelper.handleFail("参数转换异常");
                return;
            }
        }
        //查询今日待线性释放的资金账户列表
        List<Long> accountIds = incomeReleaseRecordService.getBaseMapper().getAssetAccountList(todayStart);
        log.info("accountIds:{}:", accountIds);
        for (Long accountId : accountIds) {
            try {
                releaseAccount(accountId, todayStart);
            } catch (Exception e) {
                log.error("线性释放错误", e);
                XxlJobHelper.log(e);
            }
        }
        XxlJobHelper.handleSuccess();
    }

    /**
     * 释放
     */
    @Transactional(rollbackFor = Exception.class)
    public void releaseAccount(Long accountId, Date todayStart) throws Exception {
        //查询该资金账户挖矿待释放记录
        List<IncomeReleaseRecord> releaseRecords = incomeReleaseRecordService.lambdaQuery().le(IncomeReleaseRecord::getCreateTime, todayStart)
                .ge(IncomeReleaseRecord::getDays, 1).eq(IncomeReleaseRecord::getAssetAccountId, accountId)
                .list();
        BigDecimal lineReleaseAmount = BigDecimal.ZERO;
        for (IncomeReleaseRecord record : releaseRecords) {
            //进行线性释放
            if (record.getDays() == 1) {
                //最后一天将所有冻结释放
                lineReleaseAmount = lineReleaseAmount.add(record.getFrozenAmount());
            } else {
                lineReleaseAmount = lineReleaseAmount.add(record.getOnceAmount());
            }
            saveLineReleaseRecord(record);
        }
        releaseTotalAmount(accountId, lineReleaseAmount);
    }

    /**
     * 线性释放
     */
    private void saveLineReleaseRecord(IncomeReleaseRecord record) throws Exception {
        Date now = new Date();
        record.setDays(record.getDays() - 1);
        record.setUpdateTime(now);
        BigDecimal onceAmount = record.getOnceAmount();
        if (record.getDays() == 1) {
            //最后一天将所有冻结释放
            onceAmount = record.getFrozenAmount();
        }
        record.setFrozenAmount(record.getFrozenAmount().subtract(onceAmount));
        record.setReleaseAmount(record.getReleaseAmount().add(onceAmount));
        //更新待释放记录金额
        incomeReleaseRecordService.updateIncomeReleaseRecord(record);
        //保存释放日志
        incomeReleaseLogService.saveLog(record, false);
    }

    private void releaseTotalAmount(Long accountId, BigDecimal totalLineAmount) throws Exception {
        //获取用户资金账户当日线性释放总和
        Date now = new Date();
        AssetAccount account = accountService.getById(accountId);
        if (totalLineAmount.compareTo(BigDecimal.ZERO) > 0) {
            //线性释放
            boolean update = accountService.lambdaUpdate()
                    .setSql("available = available + " + totalLineAmount.stripTrailingZeros().toPlainString())
                    .set(AssetAccount::getUpdateTime, now)
                    .eq(AssetAccount::getId, account.getId()).update();
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户余额失败");
            }
            //线性释放总和无对应记录id
            Long releaseId = accountLogService.saveLog(account, totalLineAmount, AccountLogType.TYPE_MINING_RELEASE_LINE, "当日线性释放总和", 0L);
            incomeLogService.saveLog(account.getUserId(), 4, releaseId, totalLineAmount);
        }
    }
}
