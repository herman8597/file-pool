package io.filpool.pool.service;

import io.filpool.pool.entity.CurrencyConfig;
import io.filpool.pool.param.CurrencyConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 币种配置 服务类
 *
 * @author filpool
 * @since 2021-05-31
 */
public interface CurrencyConfigService extends BaseService<CurrencyConfig> {

    /**
     * 保存
     *
     * @param currencyConfig
     * @return
     * @throws Exception
     */
    boolean saveCurrencyConfig(CurrencyConfig currencyConfig) throws Exception;

    /**
     * 修改
     *
     * @param currencyConfig
     * @return
     * @throws Exception
     */
    boolean updateCurrencyConfig(CurrencyConfig currencyConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCurrencyConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param currencyConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<CurrencyConfig> getCurrencyConfigPageList(CurrencyConfigPageParam currencyConfigPageParam) throws Exception;

}
