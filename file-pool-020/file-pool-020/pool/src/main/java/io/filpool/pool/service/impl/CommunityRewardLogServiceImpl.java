package io.filpool.pool.service.impl;

import io.filpool.pool.entity.CommunityRewardLog;
import io.filpool.pool.mapper.CommunityRewardLogMapper;
import io.filpool.pool.service.CommunityRewardLogService;
import io.filpool.pool.param.CommunityRewardLogPageParam;
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
 * 社区(市场)奖励记录表 服务实现类
 *
 * @author filpool
 * @since 2021-06-01
 */
@Slf4j
@Service
public class CommunityRewardLogServiceImpl extends BaseServiceImpl<CommunityRewardLogMapper, CommunityRewardLog> implements CommunityRewardLogService {

    @Autowired
    private CommunityRewardLogMapper communityRewardLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCommunityRewardLog(CommunityRewardLog communityRewardLog) throws Exception {
        return super.save(communityRewardLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCommunityRewardLog(CommunityRewardLog communityRewardLog) throws Exception {
        return super.updateById(communityRewardLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCommunityRewardLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<CommunityRewardLog> getCommunityRewardLogPageList(CommunityRewardLogPageParam communityRewardLogPageParam) throws Exception {
        Page<CommunityRewardLog> page = new PageInfo<>(communityRewardLogPageParam, OrderItem.desc(getLambdaColumn(CommunityRewardLog::getCreateTime)));
        LambdaQueryWrapper<CommunityRewardLog> wrapper = new LambdaQueryWrapper<>();
        IPage<CommunityRewardLog> iPage = communityRewardLogMapper.selectPage(page, wrapper);
        return new Paging<CommunityRewardLog>(iPage);
    }

}
