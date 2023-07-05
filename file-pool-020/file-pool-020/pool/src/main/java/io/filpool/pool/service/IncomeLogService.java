package io.filpool.pool.service;

import io.filpool.pool.entity.IncomeLog;
import io.filpool.pool.param.IncomeLogPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

import java.math.BigDecimal;

/**
 * 挖矿收益日志 服务类
 *
 * @author filpool
 * @since 2021-03-24
 */
public interface IncomeLogService extends BaseService<IncomeLog> {

    /**
     * 保存
     *
     * @param incomeLog
     * @return
     * @throws Exception
     */
    boolean saveIncomeLog(IncomeLog incomeLog) throws Exception;

    /**
     * 修改
     *
     * @param incomeLog
     * @return
     * @throws Exception
     */
    boolean updateIncomeLog(IncomeLog incomeLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteIncomeLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param incomeLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<IncomeLog> getIncomeLogPageList(IncomeLogPageParam incomeLogPageParam) throws Exception;

    boolean saveLog(Long userId, Integer type, Long recordId, BigDecimal amount,String symbol) throws Exception;
}
