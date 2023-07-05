package io.filpool.pool.service;

import io.filpool.pool.entity.SupplementDeduct;
import io.filpool.pool.param.SupplementDeductPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 算力补单扣除记录 服务类
 *
 * @author filpool
 * @since 2021-04-02
 */
public interface SupplementDeductService extends BaseService<SupplementDeduct> {

    /**
     * 保存
     *
     * @param supplementDeduct
     * @return
     * @throws Exception
     */
    boolean saveSupplementDeduct(SupplementDeduct supplementDeduct) throws Exception;

    /**
     * 修改
     *
     * @param supplementDeduct
     * @return
     * @throws Exception
     */
    boolean updateSupplementDeduct(SupplementDeduct supplementDeduct) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSupplementDeduct(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param supplementDeductQueryParam
     * @return
     * @throws Exception
     */
    Paging<SupplementDeduct> getSupplementDeductPageList(SupplementDeductPageParam supplementDeductPageParam) throws Exception;

}
