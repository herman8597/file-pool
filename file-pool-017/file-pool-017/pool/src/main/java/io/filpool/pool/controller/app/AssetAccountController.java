package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.*;
import io.filpool.pool.request.*;
import io.filpool.pool.service.*;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.AssetAccountVo;
import io.filpool.pool.vo.AssetRecordVo;
import io.filpool.pool.vo.CurrencyVo;
import io.filpool.pool.vo.RechargeAddrVo;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.AssetAccountPageParam;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.common.param.IdParam;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户资产表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/asset")
@Module("pool")
@Api(value = "账户资产表API", tags = {"账户资产相关接口"})
public class AssetAccountController extends BaseController {

    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private GlobalConfigService configService;
    @Autowired
    private AssetAccountLogService assetAccountLogService;


    @PostMapping("accounts")
    @ApiOperation("获取用户资产列表")
    @CheckLogin
    public ApiResult<AssetAccountVo> account() throws Exception {
        User user = SecurityUtil.currentLogin();
        if (!assetAccountService.checkAccount(user.getId())) {
            throw new FILPoolException("account.check.failed");
        }
        AssetAccountVo vo = assetAccountService.getAccountAssets(user.getId());
        return ApiResult.ok(vo);
    }

    @PostMapping("getAccountAssets")
    @ApiOperation("获取用户资产详情")
    @CheckLogin
    public ApiResult<AssetAccountVo.AccountVo> getAccountAssets(@RequestBody AccountAssetRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        if (!assetAccountService.checkAccount(user.getId())) {
            throw new FILPoolException("account.check.failed");
        }
        AssetAccountVo.AccountVo accountVo = assetAccountService.getAccount(request.getCurrencyId());
        return ApiResult.ok(accountVo);
    }

    @PostMapping("getCurrencyList")
    @ApiOperation("获取币种列表（充值或者提现）")
    @CheckLogin
    public ApiResult<List<CurrencyVo>> getCurrencyList(@RequestBody CurrencyListRequest request) throws Exception {
        List<CurrencyVo> currencyVos = currencyService.getCurrencyList(request.getIsWithdraw());
        return ApiResult.ok(currencyVos);
    }

    @PostMapping("getRechargeAddr")
    @ApiOperation("获取充值地址")
    @CheckLogin
    public ApiResult<RechargeAddrVo> getRechargeAddr(@RequestBody GetRechargeAddrRequest request) throws Exception {
        RechargeAddrVo vo = addressService.getUserRechargeAddr(request.getSymbol(), request.getChain());
        return ApiResult.ok(vo);
    }

    @PostMapping("getAssetRecords")
    @ApiOperation("获取账户资产记录列表")
    @CheckLogin
    public ApiResult<List<AssetRecordVo>> getAssetRecords(@RequestBody AssetRecordsRequest request) throws Exception {
        List<AssetRecordVo> vos = assetAccountLogService.getAssetRecords(request);
        return ApiResult.ok(vos);
    }

    @PostMapping("getRechargeList")
    @ApiOperation("获取充币记录")
    @CheckLogin
    public ApiResult<List<RechargeRecord>> getRechargeList(@RequestBody RechargeListRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<RechargeRecord> wr = Wrappers.lambdaQuery(RechargeRecord.class).eq(RechargeRecord::getUserId, user.getId()).orderByDesc(RechargeRecord::getCreateTime);
        Page<RechargeRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        if (request.getCurrencyId() != null) {
            //根据币种单位查询历史
            Currency currency = currencyService.getByCache(request.getCurrencyId());
            wr.eq(RechargeRecord::getSymbol, currency.getSymbol());
        }
        page = rechargeRecordService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(page.getRecords());
    }

    @GetMapping("getRechargeDetail/{id}")
    @ApiOperation("获取充值记录详情")
    @CheckLogin
    public ApiResult<RechargeRecord> getRechargeDetail(@ApiParam("充值记录ID") @PathVariable Long id) throws Exception {
        User user = SecurityUtil.currentLogin();
        RechargeRecord byId = rechargeRecordService.getById(id);
        if (byId == null) {
            throw new FILPoolException("illegal.access");
        }
        if (byId.getUserId().longValue() != user.getId().longValue()) {
            throw new FILPoolException("illegal.access");
        }
        return ApiResult.ok(byId);
    }

    @PostMapping("getWithdrawList")
    @ApiOperation("获取提币记录")
    @CheckLogin
    public ApiResult<List<WithdrawRecord>> getWithdrawList(@RequestBody RechargeListRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<WithdrawRecord> wr = Wrappers.lambdaQuery(WithdrawRecord.class).eq(WithdrawRecord::getUserId, user.getId()).orderByDesc(WithdrawRecord::getCreateTime);
        Page<WithdrawRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        if (request.getCurrencyId() != null) {
            //根据币种单位查询历史
            Currency currency = currencyService.getByCache(request.getCurrencyId());
            wr.eq(WithdrawRecord::getSymbol, currency.getSymbol());
        }
        page = withdrawRecordService.getBaseMapper().selectPage(page, wr);
        if (!ObjectUtils.isEmpty(page.getRecords())){
            for (WithdrawRecord withdrawRecord : page.getRecords()){
                withdrawRecord.setAmount(withdrawRecord.getAmount().negate());
            }
        }
        return ApiResult.ok(page.getRecords());
    }

    @GetMapping("getWithdrawDetail/{id}")
    @ApiOperation("获取提币记录详情")
    @CheckLogin
    public ApiResult<WithdrawRecord> getWithdrawDetail(@ApiParam("提币记录ID") @PathVariable Long id) throws Exception {
        User user = SecurityUtil.currentLogin();
        WithdrawRecord byId = withdrawRecordService.getById(id);
        if (byId == null) {
            throw new FILPoolException("illegal.access");
        }
        if (byId.getUserId().longValue() != user.getId().longValue()) {
            throw new FILPoolException("illegal.access");
        }
        byId.setAmount(byId.getAmount().negate());
        return ApiResult.ok(byId);
    }

    @PostMapping("getTransferRecord")
    @ApiOperation("获取划转记录")
    @CheckLogin
    public ApiResult<List<TransferRecord>> getTransRecord(@RequestBody TransferPageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<TransferRecord> wr = Wrappers.lambdaQuery(TransferRecord.class).eq(TransferRecord::getUserId, user.getId())
                .orderByDesc(TransferRecord::getCreateTime);
        if (request.getType() != null && request.getType() > 0) {
            wr.eq(TransferRecord::getType, request.getType());
        }
        Page<TransferRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = transferRecordService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(page.getRecords());
    }

//    @PostMapping("getPledgeRequire")
//    @ApiOperation("获取质押要求")
//    @CheckLogin
//    public ApiResult<BigDecimal> getPledgeRequire() throws Exception {
//        User user = SecurityUtil.currentLogin();
//        BigDecimal amount = null;
//        if (user.getPledgeRequire() != null && user.getPledgeRequire().compareTo(BigDecimal.ZERO) > 0) {
//            //判断用户是否单独配置
//            amount = user.getPledgeRequire();
//        } else {
//            GlobalConfig config = configService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.PLEDGE_REQUIRE).one();
//            if (config == null){
//                throw new FILPoolException("config.not-exits");
//            }
//            amount = new BigDecimal(config.getConfigValue());
//        }
//        return ApiResult.ok(amount);
//    }

    @PostMapping("transfer")
    @ApiOperation("划转")
    @CheckLogin
    public ApiResult<Long> transfer(@RequestBody TransferRequest request) throws Exception {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FILPoolException("transfer.amount.err");
        }
        Long id = transferRecordService.transfer(request);
        return ApiResult.ok(id);
    }
}

