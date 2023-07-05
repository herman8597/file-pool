package io.filpool.pool.controller.admin;

import com.google.gson.Gson;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.CurrencyConfig;
import io.filpool.pool.entity.GlobalConfig;
import io.filpool.pool.model.PowerPledgeVo;
import io.filpool.pool.request.SysCurrencyConfigRequest;
import io.filpool.pool.request.SysPowerPledgeRequest;
import io.filpool.pool.service.CurrencyConfigService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    @Autowired
    private CurrencyConfigService currencyConfigService;

    /**
     * 获得算力质押设置
     */
    @PostMapping("/getPowerPledge")
    @ApiOperation("获得算力质押设置")
    public ApiResult<PowerPledgeVo> getPowerPledge() throws Exception {
        PowerPledgeVo vo;
        GlobalConfig config = globalConfigService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.POWER_PLEDGE_REQUIRE).one();
        if (config == null) {
            throw new FILPoolException("config.not-exits");
        } else {
            vo = new Gson().fromJson(config.getConfigValue(), PowerPledgeVo.class);
            return ApiResult.ok(vo);
        }
    }

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
        GlobalConfig config = globalConfigService.lambdaQuery().eq(GlobalConfig::getConfigKey, CommonConstant.POWER_PLEDGE_REQUIRE).one();
        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, "FIL").one();
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
        } else {
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
    public ApiResult<Boolean> addServerFee(@RequestBody CurrencyConfig config) throws Exception {
        if (config.getAmount() == null || config.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FILPoolException("illegal.params");
        }
        CurrencyConfig byId = currencyConfigService.getById(config.getId());
        boolean flag;
        if (byId == null){
            flag = currencyConfigService.saveCurrencyConfig(config);
        }else{
            BeanUtils.copyProperties(config,byId);
            flag = currencyConfigService.updateCurrencyConfig(config);
        }
        return ApiResult.ok(flag);
    }

    /**
     * 获得币种配置
     */
    @PostMapping("/getCurrencyConfig")
    @ApiOperation("添加或修改技术服务费率配置")
    public ApiResult<List<CurrencyConfig>> getCurrencyConfig(@RequestBody SysCurrencyConfigRequest request) throws Exception {
        List<CurrencyConfig> configList = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getType,request.getType()).list();
        return ApiResult.ok(configList);
    }
}

