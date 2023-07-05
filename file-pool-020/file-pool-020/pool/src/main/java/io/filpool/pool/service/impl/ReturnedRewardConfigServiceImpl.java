package io.filpool.pool.service.impl;

import io.filpool.pool.entity.ReturnedRewardConfig;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
import io.filpool.pool.service.ReturnedRewardConfigService;
import io.filpool.pool.param.ReturnedRewardConfigPageParam;
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
 * 020社区奖励 服务实现类
 *
 * @author filpool
 * @since 2021-05-27
 */
@Slf4j
@Service
public class ReturnedRewardConfigServiceImpl extends BaseServiceImpl<ReturnedRewardConfigMapper, ReturnedRewardConfig> implements ReturnedRewardConfigService {

    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveReturnedRewardConfig(ReturnedRewardConfig returnedRewardConfig) throws Exception {
        return super.save(returnedRewardConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateReturnedRewardConfig(ReturnedRewardConfig returnedRewardConfig) throws Exception {
        return super.updateById(returnedRewardConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteReturnedRewardConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<ReturnedRewardConfig> getReturnedRewardConfigPageList(ReturnedRewardConfigPageParam returnedRewardConfigPageParam) throws Exception {
        Page<ReturnedRewardConfig> page = new PageInfo<>(returnedRewardConfigPageParam, OrderItem.desc(getLambdaColumn(ReturnedRewardConfig::getCreateTime)));
        LambdaQueryWrapper<ReturnedRewardConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<ReturnedRewardConfig> iPage = returnedRewardConfigMapper.selectPage(page, wrapper);
        return new Paging<ReturnedRewardConfig>(iPage);
    }

}
