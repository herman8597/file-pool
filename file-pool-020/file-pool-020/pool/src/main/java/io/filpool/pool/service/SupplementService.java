package io.filpool.pool.service;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.entity.SupplementDeduct;
import io.filpool.pool.param.SupplementDeductPageParam;
import io.filpool.pool.param.SupplementPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.SysDeductSupRequest;

/**
 * 算力补单 服务类
 *
 * @author filpool
 * @since 2021-03-29
 */
public interface SupplementService extends BaseService<Supplement> {

    /**
     * 保存
     *
     * @param supplement
     * @return
     * @throws Exception
     */
    boolean saveSupplement(Supplement supplement) throws Exception;

    /**
     * 修改
     *
     * @param supplement
     * @return
     * @throws Exception
     */
    boolean updateSupplement(Supplement supplement) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSupplement(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param supplementQueryParam
     * @return
     * @throws Exception
     */
    Paging<Supplement> getSupplementPageList(SupplementPageParam supplementPageParam) throws Exception;

    boolean audit(Supplement request) throws Exception;

    boolean deduct(SysDeductSupRequest request) throws Exception;

    Paging<SupplementDeduct> getSupplementDeductPageList(SupplementDeductPageParam supplementDeductPageParam);
}
