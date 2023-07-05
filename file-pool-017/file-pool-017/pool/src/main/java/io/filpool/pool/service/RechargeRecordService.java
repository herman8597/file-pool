package io.filpool.pool.service;

import io.filpool.pool.entity.RechargeRecord;
import io.filpool.pool.param.RechargeRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.RechargeRecordRequest;

/**
 * 充值记录 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface RechargeRecordService extends BaseService<RechargeRecord> {

    /**
     * 保存
     *
     * @param rechargeRecord
     * @return
     * @throws Exception
     */
    boolean saveRechargeRecord(RechargeRecord rechargeRecord) throws Exception;

    /**
     * 修改
     *
     * @param rechargeRecord
     * @return
     * @throws Exception
     */
    boolean updateRechargeRecord(RechargeRecord rechargeRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteRechargeRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param rechargeRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<RechargeRecord> getRechargeRecordPageList(RechargeRecordPageParam rechargeRecordPageParam) throws Exception;


    /**
     * 获取分页对象
     *
     * @param rechargeRecordRequest
     * @return
     * @throws Exception
     */
    Paging<RechargeRecord> getChargeMoneyPageList(RechargeRecordRequest rechargeRecordRequest);
}
