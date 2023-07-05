package io.filpool.pool.service;

import io.filpool.pool.entity.RewardConfig;
import io.filpool.pool.param.RewardConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 奖励等级配置 服务类
 *
 * @author filpool
 * @since 2021-03-11
 */
public interface RewardConfigService extends BaseService<RewardConfig> {

    /**
     * 保存
     *
     * @param rewardConfig
     * @return
     * @throws Exception
     */
    boolean saveRewardConfig(RewardConfig rewardConfig) throws Exception;

    /**
     * 修改
     *
     * @param rewardConfig
     * @return
     * @throws Exception
     */
    boolean updateRewardConfig(RewardConfig rewardConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteRewardConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param rewardConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<RewardConfig> getRewardConfigPageList(RewardConfigPageParam rewardConfigPageParam) throws Exception;

}
