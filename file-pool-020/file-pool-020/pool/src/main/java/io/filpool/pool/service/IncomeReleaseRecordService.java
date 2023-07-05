package io.filpool.pool.service;

import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.param.IncomeReleaseRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 待释放记录表 服务类
 *
 * @author filpool
 * @since 2021-03-30
 */
public interface IncomeReleaseRecordService extends BaseService<IncomeReleaseRecord> {

    /**
     * 保存
     *
     * @param incomeReleaseRecord
     * @return
     * @throws Exception
     */
    boolean saveIncomeReleaseRecord(IncomeReleaseRecord incomeReleaseRecord) throws Exception;

    /**
     * 修改
     *
     * @param incomeReleaseRecord
     * @return
     * @throws Exception
     */
    boolean updateIncomeReleaseRecord(IncomeReleaseRecord incomeReleaseRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteIncomeReleaseRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param incomeReleaseRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<IncomeReleaseRecord> getIncomeReleaseRecordPageList(IncomeReleaseRecordPageParam incomeReleaseRecordPageParam) throws Exception;

}
