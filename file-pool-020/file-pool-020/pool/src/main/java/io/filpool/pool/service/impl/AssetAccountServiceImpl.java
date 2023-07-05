package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.AssetAccountLog;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.AssetAccountMapper;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.param.AssetAccountPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.util.BigDecimalUtil;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.AssetAccountExchangeVo;
import io.filpool.pool.vo.AssetAccountVo;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账户资产表 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class AssetAccountServiceImpl extends BaseServiceImpl<AssetAccountMapper, AssetAccount> implements AssetAccountService {

    @Autowired
    private AssetAccountMapper assetAccountMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AssetAccountLogServiceImpl assetAccountLogService;
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAssetAccount(AssetAccount assetAccount) throws Exception {
        return super.save(assetAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAssetAccount(AssetAccount assetAccount) throws Exception {
        return super.updateById(assetAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAssetAccount(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<AssetAccount> getAssetAccountPageList(AssetAccountPageParam assetAccountPageParam) throws Exception {
        Page<AssetAccount> page = new PageInfo<>(assetAccountPageParam, OrderItem.desc(getLambdaColumn(AssetAccount::getCreateTime)));
        LambdaQueryWrapper<AssetAccount> wrapper = new LambdaQueryWrapper<>();
        IPage<AssetAccount> iPage = assetAccountMapper.selectPage(page, wrapper);
        return new Paging<AssetAccount>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean checkAccount(Long userId) throws Exception {
        User user = userService.getById(userId);
        String key = "CheckAccount:" + user.getId();
        try {
            //加锁，防止生成多个账户
            if (redisUtil.lock(key, 3000)) {
//                List<CloudAccount> accounts = lambdaQuery().eq(CloudAccount::getUserId, user.getId()).list();
                List<Currency> list = currencyService.lambdaQuery()
                        //过滤USDT erc20
                        .ne(Currency::getSeries, "ETH").list();
                Date now = new Date();
                //判断是否生成了对应的账户
                for (Currency currency : list) {
                    //通过币种名称判断，防止重复建USDT资金账户
                    int size = lambdaQuery().eq(AssetAccount::getUserId, user.getId()).eq(AssetAccount::getSymbol, currency.getSymbol()).count();
                    if (size == 0) {
                        //生成新的账户
                        AssetAccount one = new AssetAccount();
                        one.setAvailable(BigDecimal.ZERO);
                        one.setFrozen(BigDecimal.ZERO);
                        one.setPledge(BigDecimal.ZERO);
                        one.setCreateTime(now);
                        one.setUpdateTime(now);
                        one.setCurrencyId(currency.getId());
                        one.setImg(currency.getImg());
                        one.setSymbol(currency.getSymbol());
                        one.setUserId(user.getId());
                        saveAssetAccount(one);
                    }
                }
                return Boolean.TRUE;
            }
        } finally {
            redisUtil.unlock(key);
        }
        return Boolean.FALSE;
    }

    @Override
    public AssetAccountVo.AccountVo getAccount(Long currencyId) throws Exception {
        User user = SecurityUtil.currentLogin();
        checkAccount(user.getId());
        AssetAccount account = lambdaQuery().eq(AssetAccount::getCurrencyId, currencyId).eq(AssetAccount::getUserId, user.getId()).one();
        AssetAccountVo.AccountVo vo = new AssetAccountVo.AccountVo();
        vo.setAvailable(account.getAvailable());
        vo.setCurrencyId(account.getCurrencyId());
        vo.setFrozen(account.getFrozen());
        vo.setPledge(account.getPledge());
        if (StringUtils.equals(account.getSymbol(), "FIL")) {
            vo.setMinerFrozen(incomeReleaseRecordService.getBaseMapper().getFrozenIncome(user.getId(),account.getSymbol()));
        } else {
            vo.setMinerFrozen(BigDecimal.ZERO);
        }
        vo.setTotal(account.getAvailable().add(account.getFrozen()).add(account.getPledge()).add(vo.getMinerFrozen()));
        vo.setImg(account.getImg());
        vo.setId(account.getId());
        vo.setSymbol(account.getSymbol());
        vo.setUpdateTime(account.getUpdateTime());
        Currency byId = currencyService.getByCache(account.getCurrencyId());
        vo.setName(byId.getName());
        return vo;
    }

    @Override
    public AssetAccountVo getAccountAssets(Long userId) throws Exception {
        User user = userService.getById(userId);
        List<AssetAccount> accounts = baseMapper.findByUser(user.getId());

        AssetAccountVo result = new AssetAccountVo();
        List<AssetAccountVo.AccountVo> accountVos = new ArrayList<>();
        for (AssetAccount account : accounts) {
            AssetAccountVo.AccountVo vo = getAccount(account.getCurrencyId());
//            AssetAccountVo.AccountVo vo = new AssetAccountVo.AccountVo();
//            vo.setAvailable(account.getAvailable());
//            vo.setCurrencyId(account.getCurrencyId());
//            vo.setFrozen(account.getFrozen());
//            vo.setPledge(account.getPledge());
//            if (StringUtils.equals(account.getSymbol(),"FIL")) {
//                vo.setMinerFroze(incomeReleaseRecordService.getBaseMapper().getFrozenIncome(user.getId()));
//            }else{
//                vo.setMinerFroze(BigDecimal.ZERO);
//            }
//            vo.setTotal(account.getAvailable().add(account.getFrozen()).add(account.getPledge()).add(vo.getMinerFroze()));
//            vo.setImg(account.getImg());
//            vo.setId(account.getId());
//            vo.setSymbol(account.getSymbol());
//            vo.setUpdateTime(account.getUpdateTime());
//            Currency byId = currencyService.getByCache(account.getCurrencyId());
//            vo.setName(byId.getName());
            accountVos.add(vo);
        }
        result.setAssets(accountVos);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean exchangeAccount(AssetAccountExchangeVo assetAccountExchangeVo) throws FILPoolException {
        //查询用户当前的资产
        AssetAccount one = lambdaQuery().eq(AssetAccount::getUserId, assetAccountExchangeVo.getUserId()).eq(AssetAccount::getSymbol, assetAccountExchangeVo.getSymbol()).one();
        boolean update = false;
        boolean save = false;
        if (ObjectUtil.isNotEmpty(one)) {
            if (one.getAvailable().add(assetAccountExchangeVo.getOperationAmount()).compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            //存储资金变动后的金额
            one.setAvailable(BigDecimalUtil.add(one.getAvailable(), assetAccountExchangeVo.getOperationAmount()));
            one.setUpdateTime(new Date());
            update = lambdaUpdate().set(AssetAccount::getUpdateTime, new Date()).setSql("available = available + " + assetAccountExchangeVo.getOperationAmount().stripTrailingZeros().toPlainString())
                    .eq(AssetAccount::getId, one.getId()).update();
//            update = lambdaUpdate().update(one);
        }
        //新增资金变动记录
        if (update) {
            AssetAccountLog assetAccountLog = new AssetAccountLog();
            assetAccountLog.setCreateTime(new Date());
            assetAccountLog.setOperationAmount(assetAccountExchangeVo.getOperationAmount());
            assetAccountLog.setAvailable(one.getAvailable());
            assetAccountLog.setFrozen(one.getFrozen());
            assetAccountLog.setUserId(one.getUserId());
            assetAccountLog.setAssetAccountId(one.getId());
            assetAccountLog.setType(assetAccountExchangeVo.getType());
            assetAccountLog.setRemark(assetAccountExchangeVo.getRemark());
            assetAccountLog.setPledge(one.getPledge());
            save = assetAccountLogService.save(assetAccountLog);
        }
        return save;
    }

}
