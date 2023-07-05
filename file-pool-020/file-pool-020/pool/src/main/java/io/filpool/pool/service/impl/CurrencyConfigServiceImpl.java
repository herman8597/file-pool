package io.filpool.pool.service.impl;

import io.filpool.pool.entity.CurrencyConfig;
import io.filpool.pool.mapper.CurrencyConfigMapper;
import io.filpool.pool.service.CurrencyConfigService;
import io.filpool.pool.param.CurrencyConfigPageParam;
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
 * 币种配置 服务实现类
 *
 * @author filpool
 * @since 2021-05-31
 */
@Slf4j
@Service
public class CurrencyConfigServiceImpl extends BaseServiceImpl<CurrencyConfigMapper, CurrencyConfig> implements CurrencyConfigService {

    @Autowired
    private CurrencyConfigMapper currencyConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCurrencyConfig(CurrencyConfig currencyConfig) throws Exception {
        return super.save(currencyConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCurrencyConfig(CurrencyConfig currencyConfig) throws Exception {
        return super.updateById(currencyConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCurrencyConfig(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<CurrencyConfig> getCurrencyConfigPageList(CurrencyConfigPageParam currencyConfigPageParam) throws Exception {
        Page<CurrencyConfig> page = new PageInfo<>(currencyConfigPageParam, OrderItem.desc(getLambdaColumn(CurrencyConfig::getCreateTime)));
        LambdaQueryWrapper<CurrencyConfig> wrapper = new LambdaQueryWrapper<>();
        IPage<CurrencyConfig> iPage = currencyConfigMapper.selectPage(page, wrapper);
        return new Paging<CurrencyConfig>(iPage);
    }

}
