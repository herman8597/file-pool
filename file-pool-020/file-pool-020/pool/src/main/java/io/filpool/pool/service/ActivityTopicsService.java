package io.filpool.pool.service;

import io.filpool.pool.entity.ActivityTopics;
import io.filpool.pool.param.ActivityTopicsPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 商品活动专题 服务类
 *
 * @author filpool
 * @since 2021-06-25
 */
public interface ActivityTopicsService extends BaseService<ActivityTopics> {

    /**
     * 保存
     *
     * @param activityTopics
     * @return
     * @throws Exception
     */
    boolean saveActivityTopics(ActivityTopics activityTopics) throws Exception;

    /**
     * 修改
     *
     * @param activityTopics
     * @return
     * @throws Exception
     */
    boolean updateActivityTopics(ActivityTopics activityTopics) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteActivityTopics(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param activityTopicsQueryParam
     * @return
     * @throws Exception
     */
    Paging<ActivityTopics> getActivityTopicsPageList(ActivityTopicsPageParam activityTopicsPageParam) throws Exception;

}
