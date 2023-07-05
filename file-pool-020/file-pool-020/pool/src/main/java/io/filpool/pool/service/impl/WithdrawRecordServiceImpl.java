package io.filpool.pool.service.impl;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.framework.util.PasswordUtil;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.WithdrawRecordMapper;
import io.filpool.pool.request.SysWithdrawAuthRequest;
import io.filpool.pool.service.AssetAccountLogService;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.WithdrawRecordService;
import io.filpool.pool.param.WithdrawRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.GoogleAuthenticatorUtil;
import io.filpool.pool.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提币记录 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class WithdrawRecordServiceImpl extends BaseServiceImpl<WithdrawRecordMapper, WithdrawRecord> implements WithdrawRecordService {

    @Autowired
    private WithdrawRecordMapper withdrawRecordMapper;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private AssetAccountLogService accountLogService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private SysUtilController sysUtilController;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithdrawRecord(WithdrawRecord withdrawRecord) throws Exception {
        return super.save(withdrawRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithdrawRecord(WithdrawRecord withdrawRecord) throws Exception {
        return super.updateById(withdrawRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteWithdrawRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<WithdrawRecord> getWithdrawRecordPageList(WithdrawRecordPageParam withdrawRecordPageParam) throws Exception {
        Page<WithdrawRecord> page = new PageInfo<>(withdrawRecordPageParam, OrderItem.desc(getLambdaColumn(WithdrawRecord::getCreateTime)));
        LambdaQueryWrapper<WithdrawRecord> wrapper = new LambdaQueryWrapper<>();
        IPage<WithdrawRecord> iPage = withdrawRecordMapper.selectPage(page, wrapper);
        return new Paging<WithdrawRecord>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long withdraw(BigDecimal amount, Long currencyId, String address, String payPwd, String chain, String googleCode) throws Exception {
        if (StringUtils.isEmpty(address) || address.length() < 30) {
            throw new FILPoolException("transfer.address.err");
        }
        //获取用户信息
        User user = SecurityUtil.currentLogin();
        String lockKey = "appwithdraw:" + user.getId();
        //获取币种信息
        Currency currency = currencyService.getById(currencyId);
        //判断该用户的该币种是否禁提
        String symbol = currency.getSymbol();
        if ("USDT".equals(symbol)){
            Integer isWithdrawalUsdt = user.getIsWithdrawalUsdt();
            if (isWithdrawalUsdt==0){
                throw new FILPoolException("transfer.address.usdterr");
            }
        }
        if ("FIL".equals(symbol)){
            Integer isWithdrawalFil = user.getIsWithdrawalFil();
            if (isWithdrawalFil==0){
                throw new FILPoolException("transfer.address.usdterr");
            }
        }
        if (redisLock.lock(lockKey, 60)) {
            try {
                if (StringUtils.isEmpty(user.getPayPassword())) {
                    throw new FILPoolException("user.paypwd.not-set");
                }
                if (StringUtils.isEmpty(user.getGoogleSecretKey())) {
                    throw new FILPoolException("google.verify.not-bind");
                }
                String encrypt = PasswordUtil.encrypt(payPwd, user.getSalt());
                if (!StringUtils.equals(encrypt, user.getPayPassword())) {
                    throw new FILPoolException("user.paypwd.err");
                }
                boolean suc = GoogleAuthenticatorUtil.authCode(googleCode, user.getGoogleSecretKey());
                if (!suc) {
                    throw new FILPoolException("verify.code.error");
                }
                //判断余额是否充足
//                Currency currency = currencyService.getById(currencyId);
                if (StringUtils.equals(currency.getSymbol(), "USDT") && StringUtils.equals(chain.toLowerCase(), "trc20")) {
                    //usdt区分链
                    currency = currencyService.lambdaQuery().eq(Currency::getSymbol,"USDT").eq(Currency::getSeries, "TRX").one();
                }
                if (currency == null) {
                    throw new FILPoolException("transfer.currency.non-exits");
                }
                AssetAccount account = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, user.getId()).eq(AssetAccount::getSymbol, currency.getSymbol()).one();
                if (account.getAvailable().compareTo(amount) < 0) {
                    throw new FILPoolException("transfer.account.balance-low");
                }
                if (!currency.getWithdrawStatus()) {
                    throw new FILPoolException("transfer.currency.withdraw-close");
                }
                if (amount.compareTo(currency.getWithdrawFee()) <= 0) {
                    throw new FILPoolException("transfer.currency.min-withdraw");
                }
                //余额充足，冻结余额
                Date now = new Date();
                boolean isUpdate = assetAccountService.lambdaUpdate().eq(AssetAccount::getId, account.getId())
                        .setSql("available = available - " + amount.stripTrailingZeros().toPlainString())
                        .setSql("frozen = frozen + " + amount.stripTrailingZeros().toPlainString())
                        .update();
                if (!isUpdate) {
                    throw new FILPoolException("asset.modification");
                }
                //刷新账户
                account = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, user.getId()).eq(AssetAccount::getSymbol, currency.getSymbol()).one();
                //创建待审核记录
                WithdrawRecord record = new WithdrawRecord();
                record.setAmount(amount);
                record.setCreateTime(now);
                record.setCurrencyId(currency.getId());
                record.setSeries(currency.getSeries());
                record.setFee(currency.getWithdrawFee());
                record.setStatus(1);
                record.setSymbol(currency.getSymbol());
                record.setToAddress(address);
                record.setUserId(user.getId());
                //保存
                saveWithdrawRecord(record);
                //保存资产变动
                accountLogService.saveLog(account, amount.negate(), AccountLogType.TYPE_WITHDRAW, "提币申请冻结", record.getId());
                return record.getId();
            } finally {
                redisLock.unlock(lockKey);
            }
        } else {
            throw new FILPoolException("");
        }
    }

    @Override
    public Paging<WithdrawRecord> getChargeMoneyPageList(WithdrawRecordPageParam withdrawRecordPageParam) {
        Page<WithdrawRecord> page = new PageInfo<>(withdrawRecordPageParam, OrderItem.desc(getLambdaColumn(WithdrawRecord::getCreateTime)));
        LambdaQueryWrapper<WithdrawRecord> wrapper = new LambdaQueryWrapper<>();
        if (withdrawRecordPageParam.getAccount() != null) {
            wrapper.eq(WithdrawRecord::getUserId, sysUtilController.queryUserId(withdrawRecordPageParam.getAccount()));
        }
        if (withdrawRecordPageParam.getSymbol() != null) {
            wrapper.eq(WithdrawRecord::getSymbol, withdrawRecordPageParam.getSymbol());
        }
        if (withdrawRecordPageParam.getToAddress()!=null){
            wrapper.eq(WithdrawRecord::getToAddress,withdrawRecordPageParam.getToAddress());
        }
        if (withdrawRecordPageParam.getStartDate() != null) {
            wrapper.ge(WithdrawRecord::getCreateTime, withdrawRecordPageParam.getStartDate());
        }
        if (withdrawRecordPageParam.getEndDate() != null) {
            wrapper.le(WithdrawRecord::getCreateTime, withdrawRecordPageParam.getEndDate());
        }
        if (withdrawRecordPageParam.getStatus()!=null){
            wrapper.eq(WithdrawRecord::getStatus,withdrawRecordPageParam.getStatus());
        }
        IPage<WithdrawRecord> iPage = withdrawRecordMapper.selectPage(page, wrapper);
        for (WithdrawRecord withdrawRecord : iPage.getRecords()) {
            String account = sysUtilController.queryAccount(withdrawRecord.getUserId());
            if (account != null) {
                withdrawRecord.setAccount(account);
            }
        }
        return new Paging<>(page);
    }
}
