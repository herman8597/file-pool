package io.filpool.pool.service.impl;

import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.IncomeReleaseLog;
import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.mapper.IncomeReleaseLogMapper;
import io.filpool.pool.service.IncomeReleaseLogService;
import io.filpool.pool.param.IncomeReleaseLogPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 释放日志表 服务实现类
 *
 * @author filpool
 * @since 2021-03-22
 */
@Slf4j
@Service
public class IncomeReleaseLogServiceImpl extends BaseServiceImpl<IncomeReleaseLogMapper, IncomeReleaseLog> implements IncomeReleaseLogService {

    @Autowired
    private IncomeReleaseLogMapper incomeReleaseLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveIncomeReleaseLog(IncomeReleaseLog incomeReleaseLog) throws Exception {
        return super.save(incomeReleaseLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateIncomeReleaseLog(IncomeReleaseLog incomeReleaseLog) throws Exception {
        return super.updateById(incomeReleaseLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteIncomeReleaseLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<IncomeReleaseLog> getIncomeReleaseLogPageList(IncomeReleaseLogPageParam incomeReleaseLogPageParam) throws Exception {
        Page<IncomeReleaseLog> page = new PageInfo<>(incomeReleaseLogPageParam, OrderItem.desc(getLambdaColumn(IncomeReleaseLog::getCreateTime)));
        LambdaQueryWrapper<IncomeReleaseLog> wrapper = new LambdaQueryWrapper<>();
        IPage<IncomeReleaseLog> iPage = incomeReleaseLogMapper.selectPage(page, wrapper);
        return new Paging<IncomeReleaseLog>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveLog(IncomeReleaseRecord record,Boolean isFirst) throws Exception {
        Date now = new Date();
        IncomeReleaseLog releaseLog = new IncomeReleaseLog();
        releaseLog.setReleaseAmount(isFirst?record.getFirstAmount():record.getOnceAmount());
        releaseLog.setType(isFirst?1:2);
        releaseLog.setCreateTime(now);
        releaseLog.setAssetAccountId(record.getAssetAccountId());
        releaseLog.setUserId(record.getUserId());
        releaseLog.setRecordId(record.getId());
        save(releaseLog);
        return releaseLog.getId();
    }
}
