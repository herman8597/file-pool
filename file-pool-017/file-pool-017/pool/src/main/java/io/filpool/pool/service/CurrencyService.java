package io.filpool.pool.service;

import io.filpool.pool.entity.Currency;
import io.filpool.pool.param.CurrencyPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.vo.CurrencyVo;

import java.util.List;

/**
 * 币种表 服务类
 *
 * @author filpool
 * @since 2021-03-08
 */
public interface CurrencyService extends BaseService<Currency> {

    /**
     * 保存
     *
     * @param currency
     * @return
     * @throws Exception
     */
    boolean saveCurrency(Currency currency) throws Exception;

    /**
     * 修改
     *
     * @param currency
     * @return
     * @throws Exception
     */
    boolean updateCurrency(Currency currency) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCurrency(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param currencyQueryParam
     * @return
     * @throws Exception
     */
    Paging<Currency> getCurrencyPageList(CurrencyPageParam currencyPageParam) throws Exception;

    List<CurrencyVo> getCurrencyList(Boolean isWithdraw) throws Exception;

    Currency getByCache(Long currencyId) throws Exception;
}
