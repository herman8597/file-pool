package io.filpool.pool.service.impl;

import com.google.gson.Gson;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.AddressMapper;
import io.filpool.pool.service.*;
import io.filpool.pool.param.AddressPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.CloudApiRpc;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.RechargeAddrVo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 地址表 服务实现类
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@Service
public class AddressServiceImpl extends BaseServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CloudApiRpc apiRpc;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAddress(Address address) throws Exception {
        return super.save(address);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAddress(Address address) throws Exception {
        return super.updateById(address);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAddress(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Address> getAddressPageList(AddressPageParam addressPageParam) throws Exception {
        Page<Address> page = new PageInfo<>(addressPageParam, OrderItem.desc(getLambdaColumn(Address::getCreateTime)));
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        IPage<Address> iPage = addressMapper.selectPage(page, wrapper);
        return new Paging<Address>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RechargeAddrVo getUserRechargeAddr(String symbol,String chain) throws Exception {
        if (StringUtils.isEmpty(symbol)) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        //生成账户
        User user = SecurityUtil.currentLogin();
        assetAccountService.checkAccount(user.getId());
        AssetAccount one = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, user.getId()).eq(AssetAccount::getSymbol, symbol.toUpperCase()).one();
        if (one == null) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        Currency currency;
        if (StringUtils.equals(symbol,"USDT") && !StringUtils.isEmpty(chain) && StringUtils.equals(chain, "trc20")) {
            //trc20链
            currency = currencyService.lambdaQuery().eq(Currency::getSeries,"TRX").one();
        } else {
            currency = currencyService.getByCache(one.getCurrencyId());
        }
        if (!currency.getRechargeStatus()) {
            throw new FILPoolException("transfer.currency.charge-close");
        }
        //判断是否有对应系列的地址
        Address address = lambdaQuery().eq(Address::getSeriesId, currency.getSeriesId()).eq(Address::getUserId, user.getId()).one();
        if (address == null) {
            address = apiRpc.generateWallet(currency.getSeries());
            address.setSeries(currency.getSeries());
            address.setSeriesId(currency.getSeriesId());
            address.setUserId(user.getId());
            address.setCreateTime(new Date());
//            address.setSymbol(currency.getSymbol());
            saveAddress(address);
            //插入地址id
            one.setAddressId(address.getId());
            assetAccountService.updateAssetAccount(one);
        }
        Series series = seriesService.getById(currency.getSeriesId());
        RechargeAddrVo vo = new RechargeAddrVo();
        vo.setAddress(address.getAddress());
        vo.setConfirmNumber(series.getConfirm());
        vo.setMinRechargeAmount(currency.getMinRechargeAmount());
        vo.setSymbol(currency.getSymbol());
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean checkAddress(Long userId, String series) throws Exception {
        User user = userService.getById(userId);
        String key = "checkAddress:" + user.getId();
        try {
            //加锁，防止生成多个地址
            if (redisUtil.lock(key, 3000)) {
                Date now = new Date();
                Currency currency = currencyService.lambdaQuery().eq(Currency::getSeries,series).one();
                //判断是否生成了对应的地址
                int size = lambdaQuery().eq(Address::getUserId, user.getId()).eq(Address::getSeries,series).count();
                if (size == 0){
                    //生成地址
                    Address address = apiRpc.generateWallet(currency.getSeries());
                    address.setSeries(currency.getSeries());
                    address.setSeriesId(currency.getSeriesId());
                    address.setUserId(user.getId());
                    address.setCreateTime(now);
//                    address.setSymbol(currency.getSymbol());
                    saveAddress(address);
                }
                return Boolean.TRUE;
            }
        } finally {
            redisUtil.unlock(key);
        }
        return Boolean.FALSE;
    }
}
