package io.filpool.pool.service.impl;

import io.filpool.pool.entity.ActivityTopics;
import io.filpool.pool.mapper.ActivityTopicsMapper;
import io.filpool.pool.service.ActivityTopicsService;
import io.filpool.pool.param.ActivityTopicsPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品活动专题 服务实现类
 *
 * @author filpool
 * @since 2021-06-25
 */
@Slf4j
@Service
public class ActivityTopicsServiceImpl extends BaseServiceImpl<ActivityTopicsMapper, ActivityTopics> implements ActivityTopicsService {

    @Autowired
    private ActivityTopicsMapper activityTopicsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveActivityTopics(ActivityTopics activityTopics) throws Exception {
        return super.save(activityTopics);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateActivityTopics(ActivityTopics activityTopics) throws Exception {
        return super.updateById(activityTopics);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteActivityTopics(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<ActivityTopics> getActivityTopicsPageList(ActivityTopicsPageParam activityTopicsPageParam) throws Exception {
        Page<ActivityTopics> page = new PageInfo<>(activityTopicsPageParam, OrderItem.desc(getLambdaColumn(ActivityTopics::getCrateTime)));
        LambdaQueryWrapper<ActivityTopics> wrapper = new LambdaQueryWrapper<>();
        if (activityTopicsPageParam.getTopicName()!=null){
            wrapper.eq(ActivityTopics::getTopicName,activityTopicsPageParam.getTopicName());
        }
        if (activityTopicsPageParam.getStatus()!=null){
            wrapper.eq(ActivityTopics::getStatus,activityTopicsPageParam.getStatus());
        }
        IPage<ActivityTopics> iPage = activityTopicsMapper.selectPage(page, wrapper);
        return new Paging<ActivityTopics>(iPage);
    }

}
