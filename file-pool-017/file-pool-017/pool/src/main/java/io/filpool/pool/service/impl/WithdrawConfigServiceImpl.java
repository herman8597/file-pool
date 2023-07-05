package io.filpool.pool.service.impl;

import io.filpool.pool.entity.WithdrawConfig;
import io.filpool.pool.mapper.WithdrawConfigMapper;
import io.filpool.pool.service.WithdrawConfigService;
import io.filpool.pool.param.WithdrawConfigPageParam;
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
 * 提币地址配置 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class WithdrawConfigServiceImpl extends BaseServiceImpl<WithdrawConfigMapper, WithdrawConfig> implements WithdrawConfigService {

    @Autowired
    private WithdrawConfigMapper withdrawConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithdrawConfig(WithdrawConfig withdrawConfig) throws Exception {
        return super.save(withdrawConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithdrawConfig(WithdrawConfig withdrawConfig) throws Exception {
        return super.updateById(withdrawConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteWithdrawConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<WithdrawConfig> getWithdrawConfigPageList(WithdrawConfigPageParam withdrawConfigPageParam) throws Exception {
        Page<WithdrawConfig> page = new PageInfo<>(withdrawConfigPageParam, OrderItem.desc(getLambdaColumn(WithdrawConfig::getId)));
        LambdaQueryWrapper<WithdrawConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<WithdrawConfig> iPage = withdrawConfigMapper.selectPage(page, wrapper);
        return new Paging<WithdrawConfig>(iPage);
    }

}
