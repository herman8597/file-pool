package io.filpool.pool.service;

import io.filpool.pool.entity.TransferRecord;
import io.filpool.pool.param.TransferRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.TransferRequest;

/**
 * 用户划转记录 服务类
 *
 * @author filpool
 * @since 2021-03-11
 */
public interface TransferRecordService extends BaseService<TransferRecord> {

    /**
     * 保存
     *
     * @param transferRecord
     * @return
     * @throws Exception
     */
    boolean saveTransferRecord(TransferRecord transferRecord) throws Exception;

    /**
     * 修改
     *
     * @param transferRecord
     * @return
     * @throws Exception
     */
    boolean updateTransferRecord(TransferRecord transferRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteTransferRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param transferRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<TransferRecord> getTransferRecordPageList(TransferRecordPageParam transferRecordPageParam) throws Exception;

    /**
     * 划转
     */
    Long transfer(TransferRequest request) throws Exception;

    /**
     * 分页查询质押列表
     */
//    Paging<TransferRecord> getAssetAccountPageList(TransferRecordPageParam transferRecordPageParam);
}
