package io.filpool.pool.service.impl;

import io.filpool.pool.entity.IncomeLog;
import io.filpool.pool.mapper.IncomeLogMapper;
import io.filpool.pool.service.IncomeLogService;
import io.filpool.pool.param.IncomeLogPageParam;
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
 * 挖矿收益日志 服务实现类
 *
 * @author filpool
 * @since 2021-03-24
 */
@Slf4j
@Service
public class IncomeLogServiceImpl extends BaseServiceImpl<IncomeLogMapper, IncomeLog> implements IncomeLogService {

    @Autowired
    private IncomeLogMapper incomeLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveIncomeLog(IncomeLog incomeLog) throws Exception {
        return super.save(incomeLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateIncomeLog(IncomeLog incomeLog) throws Exception {
        return super.updateById(incomeLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteIncomeLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<IncomeLog> getIncomeLogPageList(IncomeLogPageParam incomeLogPageParam) throws Exception {
        Page<IncomeLog> page = new PageInfo<>(incomeLogPageParam, OrderItem.desc(getLambdaColumn(IncomeLog::getCreateTime)));
        LambdaQueryWrapper<IncomeLog> wrapper = new LambdaQueryWrapper<>();
        IPage<IncomeLog> iPage = incomeLogMapper.selectPage(page, wrapper);
        return new Paging<IncomeLog>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveLog(Long userId, Integer type, Long recordId, BigDecimal amount,String symbol) throws Exception {
        IncomeLog log = new IncomeLog();
        log.setType(type);
        log.setRecordId(recordId);
        log.setCreateTime(new Date());
        log.setUserId(userId);
        log.setAmount(amount);
        log.setPowerSymbol(symbol);
        saveIncomeLog(log);
        return true;
    }
}
