package io.filpool.pool.service.impl;

import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.mapper.BzzConfigMapper;
import io.filpool.pool.service.BzzConfigService;
import io.filpool.pool.param.BzzConfigPageParam;
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
 *  服务实现类
 *
 * @author filpool
 * @since 2021-06-24
 */
@Slf4j
@Service
public class BzzConfigServiceImpl extends BaseServiceImpl<BzzConfigMapper, BzzConfig> implements BzzConfigService {

    @Autowired
    private BzzConfigMapper bzzConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBzzConfig(BzzConfig bzzConfig) throws Exception {
        return super.save(bzzConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBzzConfig(BzzConfig bzzConfig) throws Exception {
        return super.updateById(bzzConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBzzConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<BzzConfig> getBzzConfigPageList(BzzConfigPageParam bzzConfigPageParam) throws Exception {
        Page<BzzConfig> page = new PageInfo<>(bzzConfigPageParam, OrderItem.desc(getLambdaColumn(BzzConfig::getCreateTime)));
        LambdaQueryWrapper<BzzConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<BzzConfig> iPage = bzzConfigMapper.selectPage(page, wrapper);
        return new Paging<BzzConfig>(iPage);
    }

}
