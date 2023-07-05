package io.filpool.pool.service;

import io.filpool.pool.entity.Quotation;
import io.filpool.pool.param.QuotationPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 行情信息 服务类
 *
 * @author filpool
 * @since 2021-04-14
 */
public interface QuotationService extends BaseService<Quotation> {

    /**
     * 保存
     *
     * @param quotation
     * @return
     * @throws Exception
     */
    boolean saveQuotation(Quotation quotation) throws Exception;

    /**
     * 修改
     *
     * @param quotation
     * @return
     * @throws Exception
     */
    boolean updateQuotation(Quotation quotation) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteQuotation(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param quotationQueryParam
     * @return
     * @throws Exception
     */
    Paging<Quotation> getQuotationPageList(QuotationPageParam quotationPageParam) throws Exception;

}
