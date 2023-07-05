package io.filpool.pool.service;

import io.filpool.pool.entity.CommunityRewardLog;
import io.filpool.pool.param.CommunityRewardLogPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 社区(市场)奖励记录表 服务类
 *
 * @author filpool
 * @since 2021-06-01
 */
public interface CommunityRewardLogService extends BaseService<CommunityRewardLog> {

    /**
     * 保存
     *
     * @param communityRewardLog
     * @return
     * @throws Exception
     */
    boolean saveCommunityRewardLog(CommunityRewardLog communityRewardLog) throws Exception;

    /**
     * 修改
     *
     * @param communityRewardLog
     * @return
     * @throws Exception
     */
    boolean updateCommunityRewardLog(CommunityRewardLog communityRewardLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCommunityRewardLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param communityRewardLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<CommunityRewardLog> getCommunityRewardLogPageList(CommunityRewardLogPageParam communityRewardLogPageParam) throws Exception;

}
