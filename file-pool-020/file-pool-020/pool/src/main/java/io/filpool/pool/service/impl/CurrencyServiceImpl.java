package io.filpool.pool.service.impl;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.mapper.CurrencyMapper;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.param.CurrencyPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.vo.CurrencyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 币种表 服务实现类
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@Service
public class CurrencyServiceImpl extends BaseServiceImpl<CurrencyMapper, Currency> implements CurrencyService {

    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCurrency(Currency currency) throws Exception {
        return super.save(currency);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCurrency(Currency currency) throws Exception {
        return super.updateById(currency);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCurrency(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Currency> getCurrencyPageList(CurrencyPageParam currencyPageParam) throws Exception {
        Page<Currency> page = new PageInfo<>(currencyPageParam, OrderItem.desc(getLambdaColumn(Currency::getId)));
        LambdaQueryWrapper<Currency> wrapper = new LambdaQueryWrapper<>();
        //是否可以挖矿
        if (currencyPageParam.getMiningStatus() != null){
            wrapper.eq(Currency::getMiningStatus,currencyPageParam.getMiningStatus());
        }
        //过滤USDT erc20
        wrapper.ne(Currency::getSeries,"ETH");
        IPage<Currency> iPage = currencyMapper.selectPage(page, wrapper);
        return new Paging<Currency>(iPage);
    }

    @Override
    public List<CurrencyVo> getCurrencyList(Boolean isWithdraw) throws Exception {
        List<Currency> list = lambdaQuery()
                .eq(isWithdraw ? Currency::getWithdrawStatus : Currency::getRechargeStatus, true)
                .eq(Currency::getIsEnable, true)
                //过滤USDT erc20
                .ne(Currency::getSeries,"ETH")
                .orderByDesc(Currency::getOrderParams).list();
        List<CurrencyVo> vos = new ArrayList<>();
        for (Currency currency : list) {
            CurrencyVo vo = new CurrencyVo();
            BeanUtils.copyProperties(currency, vo);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<Currency> getMinerCurrency() throws Exception {
        return lambdaQuery()
                .eq(Currency::getMiningStatus,true)
                .eq(Currency::getIsEnable, true)
                //过滤USDT erc20
                .ne(Currency::getSeries,"ETH")
                .orderByDesc(Currency::getOrderParams).list();
    }

    @Override
    public Currency getByCache(Long currencyId) throws Exception {
        String key = "currency:cache:" + currencyId;
        if (redisUtil.exists(key)) {
            return (Currency) redisUtil.get(key);
        } else {
            Currency byId = getById(currencyId);
            if (byId == null) {
                throw new FILPoolException("transfer.currency.non-exits");
            }
            //缓存到redis
            redisUtil.set(key, byId, 60L);
            return byId;
        }
    }
}
