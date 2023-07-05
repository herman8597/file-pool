package io.filpool.pool.service;

import io.filpool.pool.entity.RealNameRecord;
import io.filpool.pool.param.RealNameRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 实名认证信息 服务类
 *
 * @author filpool
 * @since 2021-03-02
 */
public interface RealNameRecordService extends BaseService<RealNameRecord> {

    /**
     * 保存
     *
     * @param realNameRecord
     * @return
     * @throws Exception
     */
    boolean saveRealNameRecord(RealNameRecord realNameRecord) throws Exception;

    /**
     * 修改
     *
     * @param realNameRecord
     * @return
     * @throws Exception
     */
    boolean updateRealNameRecord(RealNameRecord realNameRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteRealNameRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param realNameRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<RealNameRecord> getRealNameRecordPageList(RealNameRecordPageParam realNameRecordPageParam) throws Exception;

}
