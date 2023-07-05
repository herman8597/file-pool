package io.filpool.pool.service.impl;

import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.MiningRecordMapper;
import io.filpool.pool.request.SysMiningRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.param.MiningRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.AccountLogType;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平台挖矿记录表 服务实现类
 *
 * @author filpool
 * @since 2021-03-22
 */
@Slf4j
@Service
public class MiningRecordServiceImpl extends BaseServiceImpl<MiningRecordMapper, MiningRecord> implements MiningRecordService {
    @Autowired
    private MiningRecordMapper miningRecordMapper;
    @Autowired
    private AssetAccountServiceImpl accountService;
    @Autowired
    private AssetAccountLogService accountLogService;
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;
    @Autowired
    private IncomeReleaseLogService incomeReleaseLogService;
    //    @Autowired
//    private GlobalConfigService configService;
    @Autowired
    private CurrencyServiceImpl currencyService;
    @Autowired
    private IncomeLogService incomeLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private PowerOrderServiceImpl powerOrderService;
    @Autowired
    private CurrencyConfigService currencyConfigService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveMiningRecord(MiningRecord miningRecord) throws Exception {
        return super.save(miningRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMiningRecord(MiningRecord miningRecord) throws Exception {
        return super.updateById(miningRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMiningRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<MiningRecord> getMiningRecordPageList(MiningRecordPageParam miningRecordPageParam) throws Exception {
        Page<MiningRecord> page = new PageInfo<>(miningRecordPageParam, OrderItem.desc(getLambdaColumn(MiningRecord::getCreateTime)));
        LambdaQueryWrapper<MiningRecord> wrapper = new LambdaQueryWrapper<>();
        if (miningRecordPageParam.getStartDate() != null) {
            wrapper.ge(MiningRecord::getCreateTime, miningRecordPageParam.getStartDate());
        }
        if (miningRecordPageParam.getEndDate() != null) {
            wrapper.le(MiningRecord::getCreateTime, miningRecordPageParam.getEndDate());
        }
        IPage<MiningRecord> iPage = miningRecordMapper.selectPage(page, wrapper);
        return new Paging<MiningRecord>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addMiningRecord(SysMiningRequest request) throws Exception {
        MiningRecord record = new MiningRecord();
        //挖矿币种
        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, request.getSymbol())
                .eq(Currency::getMiningStatus,true).one();
        if (currency == null) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        Date now = new Date();
        record.setAmount(request.getAmount());
//        record.setStartTime(request.getStartTime());
//        record.setEndTime(request.getEndTime());
        record.setCurrencyId(currency.getId());
        record.setSymbol(currency.getSymbol());
        record.setCreateTime(now);
        record.setUserNumber(0);
        record.setRemark(request.getRemark());
        BigDecimal totalPower;
        //参与用户
        List<Long> userIdList = new ArrayList<>();
        if (request.getIsAgent() != null && request.getIsAgent()) {
            //代理商
            User one = userService.lambdaQuery().eq(User::getMobile, request.getAgentAccount())
                    .or().eq(User::getEmail, request.getAgentAccount()).eq(User::getType, 1).one();
            totalPower = powerOrderService.getBaseMapper().sumEffectAmount(one.getId(),currency.getSymbol());
            userIdList.add(one.getId());
            record.setUserNumber(1);
            record.setType(2);
            record.setPower(totalPower);
            record.setAgentAccount(request.getAgentAccount());
            record.setAgentUserId(one.getId());
        } else {
            //普通用户
            //全网有效算力
            totalPower = powerOrderService.getBaseMapper().sumSystemPower(currency.getSymbol());
            record.setPower(totalPower);
            record.setType(1);
            //可参与分币用户
            userIdList.addAll(powerOrderService.getBaseMapper().getUserIdList(currency.getSymbol()));
            //参与人数
            record.setUserNumber(userIdList.size());
        }
        //保存记录
        saveMiningRecord(record);
        //全网无算力则不需释放
        if (totalPower.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        //计算每TB应分配挖矿币种数量  挖矿数量/全网算力
        BigDecimal tbAmount = request.getAmount().divide(totalPower, 8, RoundingMode.DOWN);
        //获取对应币种技术服务费比例
        CurrencyConfig config = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId, currency.getId())
                .eq(CurrencyConfig::getType, 2).one();
//        GlobalConfig config = configService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.SERVICE_FEE).one();
        CurrencyConfig releaseConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId, currency.getId())
                .eq(CurrencyConfig::getType, 3).one();
//        GlobalConfig releaseConfig = configService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.RELEASE_DAY).one();
        int day = releaseConfig != null ? releaseConfig.getAmount().intValue() : 180;
        if (config == null) {
            throw new FILPoolException("config.not-exits");
        }
        for (Long userId : userIdList) {
            BigDecimal fee = config.getAmount();
            BigDecimal userFee = userService.getServiceFee(userId, currency.getSymbol());
            if (userFee != null && userFee.compareTo(BigDecimal.ZERO) > 0) {
                //判断用户是否单独设置手续费率
                fee = userFee;
            }
            accountService.checkAccount(userId);
            AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getUserId, userId)
                    .eq(AssetAccount::getCurrencyId, currency.getId()).one();
            //用户可用算力
            BigDecimal userPower = powerOrderService.getBaseMapper().sumEffectAmount(userId,currency.getSymbol()).setScale(8, BigDecimal.ROUND_DOWN);
            //每个tb数量*手续费率*用户算力数量
            BigDecimal totalIncome = tbAmount.multiply(userPower).multiply(BigDecimal.ONE.subtract(fee)).setScale(8, BigDecimal.ROUND_DOWN);
            //首次释放
            BigDecimal firstIncome;
            //线性冻结
            BigDecimal frozenIncome;
            BigDecimal onceAmount;
            if (day > 0) {
                firstIncome = totalIncome.multiply(new BigDecimal("0.25")).setScale(8, BigDecimal.ROUND_DOWN);
                frozenIncome = totalIncome.subtract(firstIncome).setScale(8, BigDecimal.ROUND_DOWN);
                onceAmount = frozenIncome.divide(new BigDecimal(Integer.toString(day)), 8, RoundingMode.DOWN);
            } else {
                firstIncome = totalIncome;
                frozenIncome = BigDecimal.ZERO;
                onceAmount = BigDecimal.ZERO;
            }
            //保存待释放记录
            IncomeReleaseRecord releaseRecord = new IncomeReleaseRecord();
            releaseRecord.setTotalAmount(totalIncome);
            releaseRecord.setOnceAmount(onceAmount);
            releaseRecord.setDays(day);
            releaseRecord.setUserId(account.getUserId());
            releaseRecord.setCreateTime(now);
            releaseRecord.setUpdateTime(now);
            releaseRecord.setAssetAccountId(account.getId());
            releaseRecord.setReleaseAmount(firstIncome);
            releaseRecord.setFrozenAmount(frozenIncome.setScale(8, BigDecimal.ROUND_DOWN));
            releaseRecord.setMiningRecordId(record.getId());
            releaseRecord.setSymbol(record.getSymbol());
            releaseRecord.setTotalDays(day);
            releaseRecord.setFirstAmount(firstIncome);
            releaseRecord.setServiceFee(fee);
            releaseRecord.setMiningType(record.getType());
            releaseRecord.setUserPower(userPower);
            incomeReleaseRecordService.saveIncomeReleaseRecord(releaseRecord);
            //直接释放25%,产生资金记录
            accountService.lambdaUpdate()
                    .setSql("available = available + " + firstIncome.stripTrailingZeros().toPlainString())
                    .set(AssetAccount::getUpdateTime, now)
                    .eq(AssetAccount::getId, account.getId()).update();
            //刷新账户
            account = accountService.getById(releaseRecord.getAssetAccountId());
            accountLogService.saveLog(account, firstIncome, AccountLogType.TYPE_MINING_RELEASE_FIRST, "挖矿首次释放", releaseRecord.getId());
            //保存释放记录
            incomeReleaseLogService.saveLog(releaseRecord, true);
            if (day > 0) {
                //产生一条首次释放记录
                incomeLogService.saveLog(account.getUserId(), 2, record.getId(), firstIncome, record.getSymbol());
                //产生一条冻结收益记录
                incomeLogService.saveLog(account.getUserId(), 3, record.getId(), frozenIncome, record.getSymbol());
            }
            //产生一条挖矿收益
            incomeLogService.saveLog(account.getUserId(), 1, record.getId(), totalIncome, record.getSymbol());
        }
        //.multiply(BigDecimal.ONE.subtract(fee))
        return true;
    }
//    public boolean addMiningRecord(SysMiningRequest request) throws Exception {
//        MiningRecord record = new MiningRecord();
//        //币种
//        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, "FIL").one();
//        if (currency == null) {
//            throw new FILPoolException("transfer.currency.non-exits");
//        }
//        BigDecimal totalPower = powerAccountService.getBaseMapper().sumSystemPower();
//        //计算平台算力总份额
//        Date now = new Date();
//        record.setAmount(request.getAmount());
//        record.setStartTime(request.getStartTime());
//        record.setEndTime(request.getEndTime());
//        record.setPower(totalPower);
//        record.setCurrencyId(currency.getId());
//        record.setSymbol(currency.getSymbol());
//        record.setCreateTime(now);
//        record.setType(1);
//        record.setUserNumber(0);
//        record.setRemark(request.getRemark());
//        //保存记录
//        saveMiningRecord(record);
//        if (totalPower.compareTo(BigDecimal.ZERO) <= 0) {
//            //全网无算力则不需释放
//            return true;
//        }
//        //获取技术服务费比例
//        GlobalConfig config = configService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.SERVICE_FEE).one();
//        if (config == null) {
//            throw new FILPoolException("config.not-exits");
//        }
//        BigDecimal configFee = new BigDecimal(config.getConfigValue());
//        //计算每TB应分配挖矿币种数量  挖矿数量/全网算力
//        BigDecimal tbAmount = request.getAmount().divide(totalPower, 8, RoundingMode.DOWN);
//        //.multiply(BigDecimal.ONE.subtract(fee))
//        //用户算力记录分组
//        List<Long> userIdList = powerAccountLogService.getBaseMapper().getUserIdList();
//        //参与人数
//        record.setUserNumber(userIdList.size());
//        updateMiningRecord(record);
//        for (Long userId : userIdList) {
//            BigDecimal fee = configFee;
//            BigDecimal userFee = userService.getServiceFee(userId);
//            if (userFee != null && userFee.compareTo(BigDecimal.ZERO)>0){
//                //判断用户是否单独设置手续费率
//                fee = userFee;
//            }
//            accountService.checkAccount(userId);
//            AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getUserId, userId)
//                    .eq(AssetAccount::getCurrencyId, currency.getId()).one();
//            BigDecimal totalReleaseAmount = BigDecimal.ZERO;
//            BigDecimal totalFrozenAmount = BigDecimal.ZERO;
//            //查询该用户所有算力订单记录
//            List<PowerAccountLog> accountLogList = powerAccountLogService.lambdaQuery().eq(PowerAccountLog::getUserId, userId).list();
//            for (PowerAccountLog accountLog : accountLogList) {
//                //每个tb数量*手续费*订单tb数量
//                BigDecimal totalIncome = tbAmount.multiply(BigDecimal.ONE.subtract(fee)).multiply(accountLog.getOperationPower());
//                BigDecimal firstIncome = totalIncome.multiply(new BigDecimal("0.25"));
//                //线性冻结
//                BigDecimal frozenIncome = totalIncome.subtract(firstIncome);
//                //保存待释放记录
//                IncomeReleaseRecord releaseRecord = new IncomeReleaseRecord();
//                releaseRecord.setTotalAmount(totalIncome);
//                releaseRecord.setAssetAccountId(account.getId());
//                releaseRecord.setUserId(account.getUserId());
//                releaseRecord.setCreateTime(now);
//                releaseRecord.setUpdateTime(now);
//                releaseRecord.setReleaseAmount(BigDecimal.ZERO);
//                releaseRecord.setFrozenAmount(totalIncome);
//                releaseRecord.setMiningRecordId(record.getId());
//                releaseRecord.setSymbol(record.getSymbol());
//                releaseRecord.setRecordId(accountLog.getRecordId());
//                releaseRecord.setRecordType(accountLog.getType());
//                releaseRecord.setFirstAmount(firstIncome);
//                releaseRecord.setOrderPower(accountLog.getOperationPower());
//                releaseRecord.setServiceFee(fee);
//                releaseRecord.setMiningType(record.getType());
//                if (accountLog.getType() == 3) {
//                    Order order = orderService.getById(accountLog.getRecordId());
//                    //设置释放天数为订单释放天数
//                    releaseRecord.setTotalDays(order.getReleaseDays());
//                    releaseRecord.setDays(order.getReleaseDays());
//                    releaseRecord.setOnceAmount(frozenIncome.divide(new BigDecimal(order.getReleaseDays()), 8, RoundingMode.DOWN).setScale(8, RoundingMode.DOWN));
//                } else {
//                    //非购买订单无释放天数，默认180天
//                    releaseRecord.setTotalDays(180);
//                    //当前固定180天,实际应该按订单设置天数
//                    releaseRecord.setDays(180);
//                    releaseRecord.setOnceAmount(frozenIncome.divide(new BigDecimal(180), 8, RoundingMode.DOWN).setScale(8, RoundingMode.DOWN));
//                }
//                incomeReleaseRecordService.save(releaseRecord);
//                //总冻结即为总收益
//                totalFrozenAmount = totalFrozenAmount.add(totalIncome);
//            }
//            //产生一条挖矿收益
//            incomeLogService.saveLog(account.getUserId(), 1, record.getId(),totalReleaseAmount.add(totalFrozenAmount));
//            //产生一条冻结收益记录
//            incomeLogService.saveLog(account.getUserId(), 3, record.getId(),totalFrozenAmount);
//        }
//        return true;
//    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean addAgentMiningRecord(Date startTime, Date endTime, User user, String remark, BigDecimal amount,String userAccount) throws Exception {
//        MiningRecord record = new MiningRecord();
//        //币种
//        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, "FIL").one();
//        if (currency == null) {
//            throw new FILPoolException("transfer.currency.non-exits");
//        }
//        List<PowerAccountLog> accountLogList = powerAccountLogService.lambdaQuery().eq(PowerAccountLog::getUserId, user.getId()).list();
//        if (ObjectUtils.isEmpty(accountLogList)){
//            throw new FILPoolException("user.order.is-null");
//        }
//        powerAccountService.checkAccount(user.getId());
//        PowerAccount powerAccount = powerAccountService.getAccount(user.getId());
//        accountService.checkAccount(user.getId());
//        AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getUserId, user.getId())
//                .eq(AssetAccount::getCurrencyId, currency.getId()).one();
//        //计算平台算力总份额
//        Date now = new Date();
//        record.setAmount(amount);
//        record.setStartTime(startTime);
//        record.setEndTime(endTime);
//        record.setPower(powerAccount.getAmount());
//        record.setCurrencyId(currency.getId());
//        record.setSymbol(currency.getSymbol());
//        record.setCreateTime(now);
//        record.setType(2);
//        record.setRemark(remark);
//        record.setAgentAccount(userAccount);
//        record.setAgentUserId(user.getId());
//        record.setUserNumber(1);
//        //保存记录
//        saveMiningRecord(record);
//        //获取技术服务费比例
//        GlobalConfig config = configService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.SERVICE_FEE).one();
//        if (config == null) {
//            throw new FILPoolException("config.not-exits");
//        }
//        BigDecimal configFee = new BigDecimal(config.getConfigValue());
//        BigDecimal userFee = user.getServerCharge();
//        BigDecimal fee = configFee;
//        if (userFee!=null && userFee.compareTo(BigDecimal.ZERO)>0){
//            //判断用户是否单独设置手续费率
//            fee = userFee;
//        }
//        //计算每TB应分配挖矿币种数量  挖矿数量/用户算力*手续费
//        BigDecimal tbAmount = amount.divide(powerAccount.getAmount(), 8, RoundingMode.DOWN)
//                .multiply(BigDecimal.ONE.subtract(fee));
//        BigDecimal totalFrozenAmount = BigDecimal.ZERO;
//        for (PowerAccountLog accountLog : accountLogList) {
//            //每个tb分币数量*订单tb数量
//            BigDecimal totalIncome = tbAmount.multiply(accountLog.getOperationPower());
//            BigDecimal firstIncome = totalIncome.multiply(new BigDecimal("0.25"));
//            //线性冻结
//            BigDecimal frozenIncome = totalIncome.subtract(firstIncome);
//            //保存待释放记录
//            IncomeReleaseRecord releaseRecord = new IncomeReleaseRecord();
//            releaseRecord.setTotalAmount(totalIncome);
//            releaseRecord.setAssetAccountId(account.getId());
//            releaseRecord.setUserId(account.getUserId());
//            releaseRecord.setCreateTime(now);
//            releaseRecord.setUpdateTime(now);
//            releaseRecord.setReleaseAmount(BigDecimal.ZERO);
//            releaseRecord.setFrozenAmount(totalIncome);
//            releaseRecord.setMiningRecordId(record.getId());
//            releaseRecord.setSymbol(record.getSymbol());
//            releaseRecord.setRecordId(accountLog.getRecordId());
//            releaseRecord.setRecordType(accountLog.getType());
//            releaseRecord.setFirstAmount(firstIncome);
//            releaseRecord.setOrderPower(accountLog.getOperationPower());
//            releaseRecord.setServiceFee(fee);
//            releaseRecord.setMiningType(record.getType());
//            if (accountLog.getType() == 3) {
//                Order order = orderService.getById(accountLog.getRecordId());
//                //设置释放天数为订单释放天数
//                releaseRecord.setTotalDays(order.getReleaseDays());
//                releaseRecord.setDays(order.getReleaseDays());
//                releaseRecord.setOnceAmount(frozenIncome.divide(new BigDecimal(order.getReleaseDays()), 8, RoundingMode.DOWN).setScale(8, RoundingMode.DOWN));
//            } else {
//                //非购买订单无释放天数，默认180天
//                releaseRecord.setTotalDays(180);
//                //当前固定180天,实际应该按订单设置天数
//                releaseRecord.setDays(180);
//                releaseRecord.setOnceAmount(frozenIncome.divide(new BigDecimal(180), 8, RoundingMode.DOWN).setScale(8, RoundingMode.DOWN));
//            }
//            incomeReleaseRecordService.save(releaseRecord);
//            //总冻结即为总收益
//            totalFrozenAmount = totalFrozenAmount.add(totalIncome);
//        }
//        //产生一条挖矿收益记录
//        incomeLogService.saveLog(account.getUserId(), 1, record.getId(),totalFrozenAmount);
//        //产生一条冻结收益记录
//        incomeLogService.saveLog(account.getUserId(), 3, record.getId(),totalFrozenAmount);
//        return true;
//    }
}
