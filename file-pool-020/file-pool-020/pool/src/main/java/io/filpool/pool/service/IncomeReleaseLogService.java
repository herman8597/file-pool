package io.filpool.pool.service;

import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.IncomeReleaseLog;
import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.param.IncomeReleaseLogPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

import java.math.BigDecimal;

/**
 * 释放日志表 服务类
 *
 * @author filpool
 * @since 2021-03-22
 */
public interface IncomeReleaseLogService extends BaseService<IncomeReleaseLog> {

    /**
     * 保存
     *
     * @param incomeReleaseLog
     * @return
     * @throws Exception
     */
    boolean saveIncomeReleaseLog(IncomeReleaseLog incomeReleaseLog) throws Exception;

    /**
     * 修改
     *
     * @param incomeReleaseLog
     * @return
     * @throws Exception
     */
    boolean updateIncomeReleaseLog(IncomeReleaseLog incomeReleaseLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteIncomeReleaseLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param incomeReleaseLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<IncomeReleaseLog> getIncomeReleaseLogPageList(IncomeReleaseLogPageParam incomeReleaseLogPageParam) throws Exception;

    //保存日志
    Long saveLog(IncomeReleaseRecord record,Boolean isFirst) throws Exception;
}
