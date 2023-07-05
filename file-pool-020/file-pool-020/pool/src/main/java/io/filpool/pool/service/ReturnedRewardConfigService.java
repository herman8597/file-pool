package io.filpool.pool.service;

import io.filpool.pool.entity.ReturnedRewardConfig;
import io.filpool.pool.param.ReturnedRewardConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 020社区奖励 服务类
 *
 * @author filpool
 * @since 2021-05-27
 */
public interface ReturnedRewardConfigService extends BaseService<ReturnedRewardConfig> {

    /**
     * 保存
     *
     * @param returnedRewardConfig
     * @return
     * @throws Exception
     */
    boolean saveReturnedRewardConfig(ReturnedRewardConfig returnedRewardConfig) throws Exception;

    /**
     * 修改
     *
     * @param returnedRewardConfig
     * @return
     * @throws Exception
     */
    boolean updateReturnedRewardConfig(ReturnedRewardConfig returnedRewardConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReturnedRewardConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param returnedRewardConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<ReturnedRewardConfig> getReturnedRewardConfigPageList(ReturnedRewardConfigPageParam returnedRewardConfigPageParam) throws Exception;

}
