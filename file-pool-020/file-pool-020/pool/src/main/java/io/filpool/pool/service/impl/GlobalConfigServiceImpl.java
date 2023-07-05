package io.filpool.pool.service.impl;

import io.filpool.pool.entity.GlobalConfig;
import io.filpool.pool.mapper.GlobalConfigMapper;
import io.filpool.pool.service.GlobalConfigService;
import io.filpool.pool.param.GlobalConfigPageParam;
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
 * 全局配置 服务实现类
 *
 * @author filpool
 * @since 2021-03-11
 */
@Slf4j
@Service
public class GlobalConfigServiceImpl extends BaseServiceImpl<GlobalConfigMapper, GlobalConfig> implements GlobalConfigService {

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveGlobalConfig(GlobalConfig globalConfig) throws Exception {
        return super.save(globalConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGlobalConfig(GlobalConfig globalConfig) throws Exception {
        return super.updateById(globalConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteGlobalConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<GlobalConfig> getGlobalConfigPageList(GlobalConfigPageParam globalConfigPageParam) throws Exception {
        Page<GlobalConfig> page = new PageInfo<>(globalConfigPageParam, OrderItem.desc(getLambdaColumn(GlobalConfig::getId)));
        LambdaQueryWrapper<GlobalConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<GlobalConfig> iPage = globalConfigMapper.selectPage(page, wrapper);
        return new Paging<GlobalConfig>(iPage);
    }

}
