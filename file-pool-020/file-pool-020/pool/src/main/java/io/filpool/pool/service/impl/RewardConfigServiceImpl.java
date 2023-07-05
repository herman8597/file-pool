package io.filpool.pool.service.impl;

import io.filpool.pool.entity.RewardConfig;
import io.filpool.pool.mapper.RewardConfigMapper;
import io.filpool.pool.service.RewardConfigService;
import io.filpool.pool.param.RewardConfigPageParam;
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
 * 奖励等级配置 服务实现类
 *
 * @author filpool
 * @since 2021-03-11
 */
@Slf4j
@Service
public class RewardConfigServiceImpl extends BaseServiceImpl<RewardConfigMapper, RewardConfig> implements RewardConfigService {

    @Autowired
    private RewardConfigMapper rewardConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRewardConfig(RewardConfig rewardConfig) throws Exception {
        return super.save(rewardConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRewardConfig(RewardConfig rewardConfig) throws Exception {
        return super.updateById(rewardConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRewardConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<RewardConfig> getRewardConfigPageList(RewardConfigPageParam rewardConfigPageParam) throws Exception {
        Page<RewardConfig> page = new PageInfo<>(rewardConfigPageParam, OrderItem.desc(getLambdaColumn(RewardConfig::getId)));
        LambdaQueryWrapper<RewardConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<RewardConfig> iPage = rewardConfigMapper.selectPage(page, wrapper);
        return new Paging<RewardConfig>(iPage);
    }

}
