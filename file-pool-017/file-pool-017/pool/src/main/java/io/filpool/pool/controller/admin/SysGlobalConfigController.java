package io.filpool.pool.controller.admin;

import com.google.gson.Gson;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.GlobalConfig;
import io.filpool.pool.model.PowerPledgeVo;
import io.filpool.pool.request.SysPowerPledgeRequest;
import io.filpool.pool.request.SysServerFeeRequest;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.GlobalConfigService;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.GlobalConfigPageParam;
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

/**
 * 全局配置 控制器
 *
 * @author filpool
 * @since 2021-03-11
 */
@Slf4j
@RestController
@RequestMapping("sys/globalConfig")
@Module("pool")
@Api(value = "全局配置API", tags = {"全局配置"})
public class SysGlobalConfigController extends BaseController {

    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private CurrencyService currencyService;

    /**
     * 添加算力质押设置
     */
    @PostMapping("/savePowerPledge")
    @ApiOperation("添加或修改算力质押设置")
    public ApiResult<Boolean> addPowerPledgeConfig(@RequestBody SysPowerPledgeRequest request) throws Exception {
        PowerPledgeVo vo;
        if (request.getTbNeedAmount() == null || request.getTbGasFee() == null) {
            throw new FILPoolException("illegal.params");
        }
        GlobalConfig config = globalConfigService.lambdaQuery().eq(GlobalConfig::getConfigKey,CommonConstant.POWER_PLEDGE_REQUIRE).one();
        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol,"FIL").one();
        if (config == null) {
            config = new GlobalConfig();
            vo = new PowerPledgeVo();
            vo.setTbNeedAmount(request.getTbNeedAmount());
            vo.setTbGasFee(request.getTbGasFee());
            vo.setSymbol("FIL");
            vo.setCurrencyId(currency.getId());
            config.setConfigKey(CommonConstant.POWER_PLEDGE_REQUIRE);
            config.setConfigValue(new Gson().toJson(vo));
            return ApiResult.result(globalConfigService.saveGlobalConfig(config));
        }else{
            vo = new Gson().fromJson(config.getConfigValue(), PowerPledgeVo.class);
            vo.setTbNeedAmount(request.getTbNeedAmount());
            vo.setTbGasFee(request.getTbGasFee());
            vo.setSymbol("FIL");
            vo.setCurrencyId(currency.getId());
            config.setConfigValue(new Gson().toJson(vo));
            return ApiResult.result(globalConfigService.updateGlobalConfig(config));
        }
    }


    /**
     * 添加技术服务费率
     */
    @PostMapping("/saveServerFee")
    @ApiOperation("添加或修改技术服务费率配置")
    public ApiResult<Boolean> addServerFee(@RequestBody SysServerFeeRequest request) throws Exception {

        if (request.getFee() == null) {
            throw new FILPoolException("illegal.params");
        }
        GlobalConfig config = globalConfigService.lambdaQuery().eq(GlobalConfig::getConfigKey,CommonConstant.SERVICE_FEE).one();
        if (config == null) {
            //添加配置
            config = new GlobalConfig();
            config.setConfigKey(CommonConstant.SERVICE_FEE);
            config.setConfigValue(request.getFee().toPlainString());
            return ApiResult.result(globalConfigService.saveGlobalConfig(config));
        }else{
            config.setConfigValue(request.getFee().toPlainString());
            return ApiResult.ok(globalConfigService.updateGlobalConfig(config));
        }
    }
}

