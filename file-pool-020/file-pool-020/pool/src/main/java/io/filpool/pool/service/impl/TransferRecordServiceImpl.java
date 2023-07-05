package io.filpool.pool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.PageInfo;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.framework.util.PasswordUtil;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.TransferRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.TransferRecordMapper;
import io.filpool.pool.param.TransferRecordPageParam;
import io.filpool.pool.request.TransferRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户划转记录 服务实现类
 *
 * @author filpool
 * @since 2021-03-11
 */
@Slf4j
@Service
public class TransferRecordServiceImpl extends BaseServiceImpl<TransferRecordMapper, TransferRecord> implements TransferRecordService {

    @Autowired
    private TransferRecordMapper transferRecordMapper;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private AssetAccountLogService assetAccountLogService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private UserService userService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTransferRecord(TransferRecord transferRecord) throws Exception {
        return super.save(transferRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTransferRecord(TransferRecord transferRecord) throws Exception {
        return super.updateById(transferRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTransferRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<TransferRecord> getTransferRecordPageList(TransferRecordPageParam transferRecordPageParam) throws Exception {
        Page<TransferRecord> page = new PageInfo<>(transferRecordPageParam, OrderItem.desc(getLambdaColumn(TransferRecord::getCreateTime)));
        LambdaQueryWrapper<TransferRecord> wrapper = new LambdaQueryWrapper<>();
        IPage<TransferRecord> iPage = transferRecordMapper.selectPage(page, wrapper);
        return new Paging<TransferRecord>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long transfer(TransferRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        String lockKey = "apptransfer:" + user.getId();
        if (redisLock.lock(lockKey, 60)) {
            try {
                //校验密码
                if (StringUtils.isEmpty(user.getPayPassword())) {
                    throw new FILPoolException("user.paypwd.not-set");
                }
                String encrypt = PasswordUtil.encrypt(request.getPayPwd(), user.getSalt());
                if (!StringUtils.equals(encrypt, user.getPayPassword())) {
                    throw new FILPoolException("user.paypwd.err");
                }
                Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, "FIL").one();
                if (currency == null) {
                    throw new FILPoolException("transfer.currency.non-exits");
                }
                AssetAccount account = assetAccountService.lambdaQuery().eq(AssetAccount::getSymbol, currency.getSymbol())
                        .eq(AssetAccount::getUserId, user.getId()).one();
                if (account == null) {
                    throw new FILPoolException("illegal.access");
                }
                //判断余额
                if (request.getType() == 1 && account.getAvailable().compareTo(request.getAmount()) < 0) {
                    throw new FILPoolException("transfer.account.balance-low");
                }
                if (request.getType() == 2 && account.getPledge().compareTo(request.getAmount()) < 0) {
                    throw new FILPoolException("transfer.account.balance-low");
                }
                //划转
                account.setAvailable(request.getType() == 1 ? account.getAvailable().subtract(request.getAmount()) :
                        account.getAvailable().add(request.getAmount()));
                account.setPledge(request.getType() == 1 ? account.getPledge().add(request.getAmount())
                        : account.getPledge().subtract(request.getAmount()));
                //添加资金变动记录
                Date now = new Date();
                account.setUpdateTime(now);
                assetAccountService.updateAssetAccount(account);
                //创建划转记录
                TransferRecord record = new TransferRecord();
                record.setAmount(request.getAmount());
                record.setType(request.getType());
                record.setOperationType(2);
                record.setCreateTime(now);
                record.setCurrencyId(currency.getId());
                record.setSymbol(currency.getSymbol());
                record.setUserId(user.getId());
                saveTransferRecord(record);
                assetAccountLogService.saveLog(account,request.getAmount(), AccountLogType.TYPE_TRANSFER,request.getType() == 1?"可用转质押":"质押转可用",record.getId());
                return record.getId();
            } finally {
                redisLock.unlock(lockKey);
            }
        } else {
            throw new FILPoolException("");
        }
    }

    //质押列表查询
   /* @Override
    public Paging<TransferRecord> getAssetAccountPageList(TransferRecordPageParam transferRecordPageParam) {
        Page<TransferRecord> page = new PageInfo<>(transferRecordPageParam, OrderItem.desc(getLambdaColumn(TransferRecord::getCreateTime)));
        LambdaQueryWrapper<TransferRecord> wrapper = Wrappers.lambdaQuery();

        //通过用户账号过滤数据
        String account = transferRecordPageParam.getAccount();
        if (account!=null){
            List<User> list = userService.lambdaQuery().like(User::getMobile, transferRecordPageParam.getAccount()).or(x -> x.like(User::getEmail, transferRecordPageParam.getAccount())).list();
            if (ObjectUtils.isEmpty(list)){
                return new Paging<>(page);
            }
            List<Long> ids = list.stream().map(User::getId).collect(Collectors.toList());
            wrapper.in(TransferRecord::getUserId,ids);
        }

        IPage<TransferRecord> iPage = transferRecordMapper.selectPage(page, wrapper);
        for (TransferRecord transferRecord:iPage.getRecords()) {
            //通过用户id查询用户的账号和用户的质押要求
            User one = userService.lambdaQuery().eq(User::getId, transferRecord.getUserId()).one();
            //通过id查询该用户的质押账户总金额
            AssetAccount two = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, transferRecord.getUserId()).one();
            if (ObjectUtils.isNotEmpty(one)){
                if (StringUtils.isNotBlank(one.getMobile())){
                    transferRecord.setAccount(one.getMobile());
                }else{
                    transferRecord.setAccount(one.getEmail());
                }
                //质押要求
                transferRecord.setPledgeRequire(one.getPledgeRequire());
            }
            if (ObjectUtils.isNotEmpty(two)){
                transferRecord.()
            }
        }

        return null;
    }*/



}
