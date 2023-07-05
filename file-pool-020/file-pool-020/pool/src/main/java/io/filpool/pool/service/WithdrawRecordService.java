package io.filpool.pool.service;

import io.filpool.pool.entity.WithdrawRecord;
import io.filpool.pool.param.WithdrawRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.SysWithdrawAuthRequest;

import java.math.BigDecimal;

/**
 * 提币记录 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface WithdrawRecordService extends BaseService<WithdrawRecord> {

    /**
     * 保存
     *
     * @param withdrawRecord
     * @return
     * @throws Exception
     */
    boolean saveWithdrawRecord(WithdrawRecord withdrawRecord) throws Exception;

    /**
     * 修改
     *
     * @param withdrawRecord
     * @return
     * @throws Exception
     */
    boolean updateWithdrawRecord(WithdrawRecord withdrawRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteWithdrawRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param withdrawRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<WithdrawRecord> getWithdrawRecordPageList(WithdrawRecordPageParam withdrawRecordPageParam) throws Exception;

    Long withdraw(BigDecimal amount,Long currencyId,String address,String payPwd,String chain,String googleCode) throws Exception;

    Paging<WithdrawRecord> getChargeMoneyPageList(WithdrawRecordPageParam withdrawRecordPageParam);
}
