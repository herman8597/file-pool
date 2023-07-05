package io.filpool.pool.service;

import io.filpool.pool.entity.WithdrawConfig;
import io.filpool.pool.param.WithdrawConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 提币地址配置 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface WithdrawConfigService extends BaseService<WithdrawConfig> {

    /**
     * 保存
     *
     * @param withdrawConfig
     * @return
     * @throws Exception
     */
    boolean saveWithdrawConfig(WithdrawConfig withdrawConfig) throws Exception;

    /**
     * 修改
     *
     * @param withdrawConfig
     * @return
     * @throws Exception
     */
    boolean updateWithdrawConfig(WithdrawConfig withdrawConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteWithdrawConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param withdrawConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<WithdrawConfig> getWithdrawConfigPageList(WithdrawConfigPageParam withdrawConfigPageParam) throws Exception;

}
