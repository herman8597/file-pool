package io.filpool.pool.controller.admin;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.pool.entity.*;
import io.filpool.pool.excel.UserExcel;
import io.filpool.pool.excel.WithdrawMoneyExcel;
import io.filpool.pool.model.WithdrawVo;
import io.filpool.pool.param.WithdrawRecordPageParam;
import io.filpool.pool.request.RechargeRecordRequest;
import io.filpool.pool.request.SysWithdrawAuthRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.CloudApiRpc;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping( "/sys/withdraw")
@Api(value = "提现控制器", tags = {"提现相关API(系统后台)"})
@ApiSupport(order = 2)
public class SysWithdrawController extends BaseController {

    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private AssetAccountLogService accountLogService;
    @Autowired
    private RedisLock redisLock;
    @Value("${spring.profiles.active}")
    private String active;
    @Autowired
    private CloudApiRpc cloudApiRpc;
    @Autowired
    private AddressService addressService;

//    @PostMapping("withConfigs")
//    @RequiresPermissions("sys:withdraw:withConfigs")
//    @ApiOperation("获取提现及手续费地址列表")
//    @OperationLog(name = "获取提现及手续费地址列表", type = OperationLogType.LIST)
//    public ApiResult<Paging<WithdrawConfigVo>> withConfigs(@RequestBody PageRequest request) throws Exception {
//        Paging<WithdrawConfigVo> paging = new Paging<>();
//        paging.setPageIndex(request.getPageIndex());
//        paging.setPageSize(request.getPageSize());
//        WithdrawConfigPageParam pageParam = new WithdrawConfigPageParam();
//        pageParam.setPageIndex(request.getPageIndex());
//        pageParam.setPageSize(request.getPageSize());
//
//        LambdaQueryWrapper<WithdrawConfig> wrapper = Wrappers.lambdaQuery(WithdrawConfig.class);
//        wrapper.select(WithdrawConfig::getAddress, WithdrawConfig::getId, WithdrawConfig::getSeriesId, WithdrawConfig::getType)
//                .orderByAsc(WithdrawConfig::getId);
//        PageInfo<WithdrawConfig> pageInfo = withdrawConfigService.getBaseMapper().selectPage(new PageInfo<>(pageParam), wrapper);
//        paging.setTotal(pageInfo.getTotal());
//        List<WithdrawConfigVo> vos = new ArrayList<>();
//        for (WithdrawConfig record : pageInfo.getRecords()) {
//            WithdrawConfigVo vo = new WithdrawConfigVo();
//            BeanUtils.copyProperties(record, vo);
//            Series byId = seriesService.getById(record.getSeriesId());
//            vo.setSeries(byId.getName());
//            vos.add(vo);
//        }
//        paging.setRecords(vos);
//        return ApiResult.ok(paging);
//    }

//    @PostMapping("saveOrUpdateConfig")
//    @RequiresPermissions("sys:withdraw:withConfigsChange")
//    @ApiOperation("保存或者修改提现地址配置")
//    @OperationLog(name = "获取提现及手续费地址列表", type = OperationLogType.UPDATE)
//    public ApiResult<Boolean> saveOrUpdateConfig(@Validated @RequestBody WithdrawConfig withdrawConfig) throws Exception {
//        boolean b = withdrawConfigService.saveOrUpdate(withdrawConfig);
//        return ApiResult.ok(b);
//    }

    /**
     *
     * 提币订单（提币订单）
     */
    @PostMapping("/getWithdrawMoneyPageList")
    @OperationLog(name = "提币订单", type = OperationLogType.PAGE)
    @ApiOperation(value = "提币订单", response = RechargeRecord.class)
    public Paging<WithdrawRecord> getWithdrawMoneyPageList(@Validated @RequestBody WithdrawRecordPageParam withdrawRecordPageParam){
        Paging<WithdrawRecord> paging = withdrawRecordService.getChargeMoneyPageList(withdrawRecordPageParam);
        return paging;
    }

    /**
     *
     * 提币订单（提币订单-导出）
     */
    @PostMapping("/withdrawMoneyPageListExport")
    @OperationLog(name = "提币订单-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "提币订单-导出", response = RechargeRecord.class)
    public void withdrawMoneyPageListExport(@Validated @RequestBody WithdrawRecordPageParam withdrawRecordPageParam, HttpServletResponse response) throws Exception {
        Paging<WithdrawRecord> paging = withdrawRecordService.getChargeMoneyPageList(withdrawRecordPageParam);

        //将数据循环存到导出实体类中
        List<WithdrawMoneyExcel> excelWithdrawList = new ArrayList<>();

        for (WithdrawRecord withdrawRecord:paging.getRecords()) {
            WithdrawMoneyExcel withdrawMoneyExcel = new WithdrawMoneyExcel();
            BeanUtils.copyProperties(withdrawRecord,withdrawMoneyExcel);
            excelWithdrawList.add(withdrawMoneyExcel);
        }
        FileUtil.exportExcel(excelWithdrawList,WithdrawMoneyExcel.class,"提币订单",response);
    }



    /**
     * 提币订单详情（提币订单-详情）
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "提币订单详情", type = OperationLogType.INFO)
    @ApiOperation(value = "提币订单详情", response = AssetAccount.class)
    public ApiResult<WithdrawRecord> getAssetAccount(@PathVariable("id") Long id) throws Exception {
        WithdrawRecord withdrawRecord = withdrawRecordService.getById(id);
        return ApiResult.ok(withdrawRecord);
    }


    /**
     *
     * 提币订单（提币订单-审核）
     */
    @PostMapping("auth")
    @ApiOperation("审核提币")
    @OperationLog(name = "审核提币", type = OperationLogType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> auth(@RequestBody SysWithdrawAuthRequest request) throws Exception {
        WithdrawRecord record = withdrawRecordService.getById(request.getWithdrawId());
        if (record == null) {
            return ApiResult.fail("找不到该记录");
        }
        if (record.getStatus() != 1) {
            return ApiResult.fail("提币已审核，请勿重复操作");
        }
        Currency currency = currencyService.getById(record.getCurrencyId());
        if (currency == null) {
            return ApiResult.fail("提币币种不存在");
        }
        Series series = seriesService.getById(currency.getSeriesId());
        if (series == null) {
            return ApiResult.fail("提币币种系列不存在");
        }
        if (!currency.getWithdrawStatus()) {
            return ApiResult.fail("该币种暂停提现");
        }
        AssetAccount assetAccount = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, record.getUserId()).eq(AssetAccount::getSymbol, record.getSymbol()).one();
        if (assetAccount == null) {
            return ApiResult.fail("用户提币账户不存在");
        }
        if (assetAccount.getFrozen().compareTo(record.getAmount()) < 0) {
            return ApiResult.fail("用户提币账户异常");
        }
        addressService.checkAddress(record.getUserId(),series.getName());
        Address address = addressService.lambdaQuery().eq(Address::getUserId,record.getUserId())
                .eq(Address::getSeriesId,series.getId()).one();
        if (address == null) {
            return ApiResult.fail("用户地址异常");
        }
        AssetAccountLog log = accountLogService.lambdaQuery().eq(AssetAccountLog::getAssetAccountId,assetAccount.getId())
                .eq(AssetAccountLog::getType,2)
                .eq(AssetAccountLog::getRecordId,record.getId()).one();
        if (log == null) {
            return ApiResult.fail("找不到该记录");
        }
        String key = "sys_withdraw:" + record.getId();
        if (redisLock.lock(key, 60)) {
            try {
                if (request.getIsPass()) {
                    //查询提币地址余额
                    String hash;
                    String symbol = currency.getSymbol();
                    if (active.equals("dev") /*|| active.equals("test")*/) {
                        //测试环境和本地环境  直接通过
                        hash = "测试提现";
                    } else {
                        if (StringUtils.equals(currency.getSeries(),"TRX")){
                            //erc20的就是USDT trc20为USDT-TRX
                            symbol = currency.getSymbol()+"-"+currency.getSeries();
                        }
                        ApiResult<WithdrawVo> result = cloudApiRpc.transfer(address.getAddress(),address.getSecret(), record.getToAddress(), symbol,record.getAmount(),record.getId());
                        if (result.getCode() == 200){
                            hash = result.getData().getTransHash();
                        }else if (StringUtils.equals(result.getMessage(),"该交易流水号已处理")){
                            hash = cloudApiRpc.getRecordByPid(record.getId());
                        }else{
                            hash = "";
                        }
                    }
                    if (StringUtils.isEmpty(hash)) {
                        return ApiResult.fail("转账失败");
                    }
                    //减去冻结
                    assetAccount.setFrozen(assetAccount.getFrozen().subtract(record.getAmount()));
                    assetAccount.setUpdateTime(new Date());
                    assetAccountService.updateAssetAccount(assetAccount);
                    //更改提币记录
                    record.setStatus(5);
                    record.setTxHash(hash);
                    record.setAuthTime(new Date());
                    withdrawRecordService.updateWithdrawRecord(record);
                    //记录账户变动
                    log.setFrozen(assetAccount.getFrozen());
                    log.setAvailable(assetAccount.getAvailable());
                    log.setCreateTime(new Date());
                    log.setRemark("提币审核通过:" + hash);
                    accountLogService.updateAssetAccountLog(log);
//                    accountLogService.saveLog(assetAccount, record.getAmount().negate(), AccountLogType.TYPE_WITHDRAW, "提币审核通过:" + hash,record.getId());
                    return ApiResult.ok(true);
                } else {
                    //冻结返回
                    assetAccount.setFrozen(assetAccount.getFrozen().subtract(record.getAmount()));
                    assetAccount.setAvailable(assetAccount.getAvailable().add(record.getAmount()));
                    assetAccount.setUpdateTime(new Date());
                    assetAccountService.updateAssetAccount(assetAccount);
                    //拒绝提现
                    record.setStatus(3);
                    record.setAuthTime(new Date());
                    withdrawRecordService.updateWithdrawRecord(record);
                    log.setFrozen(assetAccount.getFrozen());
                    log.setAvailable(assetAccount.getAvailable());
                    log.setCreateTime(new Date());
                    log.setRemark("提币审核拒绝");
                    accountLogService.updateAssetAccountLog(log);
//                    accountLogService.saveLog(assetAccount, record.getAmount(), AccountLogType.TYPE_WITHDRAW, "提币审核拒绝",record.getId());
                    return ApiResult.ok(true);
                }
            } finally {
                redisLock.unlock(key);
            }
        } else {
            return ApiResult.fail("提币操作中，请勿重复操作");
        }
    }
}
