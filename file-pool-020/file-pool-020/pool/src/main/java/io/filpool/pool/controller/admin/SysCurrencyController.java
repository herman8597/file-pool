package io.filpool.pool.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.CurrencyConfig;
import io.filpool.pool.entity.Series;
import io.filpool.pool.param.CurrencyPageParam;
import io.filpool.pool.service.BzzConfigService;
import io.filpool.pool.service.CurrencyConfigService;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.impl.SeriesServiceImpl;
import io.filpool.pool.vo.CommunityRewardLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 币种表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping("sys/currency")
@Module("pool")
@Api(value = "币种表API", tags = {"币种表"})
public class SysCurrencyController extends BaseController {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private SeriesServiceImpl seriesService;
    @Autowired
    private CurrencyConfigService currencyConfigService;
    @Autowired
    private BzzConfigService bzzConfigService;

    /**
     * 添加币种表（币种列表-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加币种表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加币种表", response = ApiResult.class)
    public ApiResult<Boolean> addCurrency(@Validated(Add.class) @RequestBody Currency currency) throws Exception {
        currency.setCreateTime(new Date());
        Long seriesId = currency.getSeriesId();
        Series one = seriesService.lambdaQuery().eq(Series::getId, seriesId).one();
        if (ObjectUtil.isNotEmpty(one)){
            currency.setSeries(one.getName()).setSeriesId(one.getId());
        }
        boolean flag = currencyService.saveCurrency(currency);
        return ApiResult.result(flag);
    }

    /**
     * 修改币种表（币种列表-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改币种表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改币种表", response = ApiResult.class)
    public ApiResult<Boolean> updateCurrency(@Validated(Update.class) @RequestBody Currency currency) throws Exception {
        currency.setCreateTime(new Date());
        boolean flag = currencyService.updateCurrency(currency);
        return ApiResult.result(flag);
    }

    /**
     * 删除币种表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除币种表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除币种表", response = ApiResult.class)
    public ApiResult<Boolean> deleteCurrency(@PathVariable("id") Long id) throws Exception {
        boolean flag = currencyService.deleteCurrency(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取币种表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "币种表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "币种表详情", response = Currency.class)
    public ApiResult<Currency> getCurrency(@PathVariable("id") Long id) throws Exception {
        Currency currency = currencyService.getById(id);
        return ApiResult.ok(currency);
    }

    /**
     * 币种表分页列表（币种列表）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "币种表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "币种表分页列表", response = Currency.class)
    public ApiResult<Paging<Currency>> getCurrencyPageList(@Validated @RequestBody CurrencyPageParam currencyPageParam) throws Exception {
        Paging<Currency> paging = currencyService.getCurrencyPageList(currencyPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 查询 series系列（series系列）
     */
    @PostMapping("/getSeries")
    @OperationLog(name = "查询 series系列", type = OperationLogType.PAGE)
    @ApiOperation(value = "查询 series系列", response = Currency.class)
    public ApiResult<?> getSeries() {
        List<Series> list = seriesService.lambdaQuery().list();
        return ApiResult.ok(list);
    }

    /**
     * 币种表分页列表（币种列表）
     */
    @PostMapping("/getCurrency")
    @OperationLog(name = "币种列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "币种列表", response = Currency.class)
    public ApiResult<?> getCurrency() throws Exception {
        List<Currency> list = currencyService.list();
        return ApiResult.ok(list);
    }


    /***
     *
     * 其它参数配置-成长经验值
     *
     */
    @PostMapping("/growUpExperience")
    @OperationLog(name = "其它参数配置-成长经验值")
    @ApiOperation(value = "其它参数配置-成长经验值")
    public ApiResult<Paging<CurrencyConfig>> growUpExperience(){
        List<CurrencyConfig> list = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getType, 1).list();
        Page<CurrencyConfig> page = new Page<>();
        page.setTotal(list.size());
        page.setRecords(list);
        return ApiResult.ok(new Paging<>(page));
    }

    /***
     *
     * 其它参数配置-成长经验值-编辑
     *
     */
    @PostMapping("/growUpExperienceEdit")
    @OperationLog(name = "其它参数配置-成长经验值-编辑")
    @ApiOperation(value = "其它参数配置-成长经验值-编辑")
    public ApiResult<?> growUpExperienceEdit(@RequestBody CurrencyConfig currencyConfig){
        boolean b = currencyConfigService.saveOrUpdate(currencyConfig);
        return ApiResult.ok(b);
    }


    /***
     *
     * 其它参数配置-全局技术服务费
     *
     */
    @PostMapping("/globalTechnicalServiceFee")
    @OperationLog(name = "其它参数配置-全局技术服务费")
    @ApiOperation(value = "其它参数配置-全局技术服务费")
    public ApiResult<Paging<CurrencyConfig>> globalTechnicalServiceFee(){
        List<CurrencyConfig> list = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getType, 2).list();
        Page<CurrencyConfig> page = new Page<>();
        page.setTotal(list.size());
        page.setRecords(list);
        return ApiResult.ok(new Paging<>(page));
    }

    /***
     *
     * 其它参数配置-全局技术服务费-编辑
     *
     */
    @PostMapping("/globalTechnicalServiceFeeEdit")
    @OperationLog(name = "其它参数配置-全局技术服务费-编辑")
    @ApiOperation(value = "其它参数配置-全局技术服务费-编辑")
    public ApiResult<?> globalTechnicalServiceFeeEdit(@RequestBody CurrencyConfig currencyConfig){
        boolean b = currencyConfigService.saveOrUpdate(currencyConfig);
        return ApiResult.ok(b);
    }

    /***
     *
     * 其它参数配置-BZZ数据
     *
     */
    @PostMapping("/otherBzzDate")
    @OperationLog(name = "其它参数配置-BZZ数据")
    @ApiOperation(value = "其它参数配置-BZZ数据")
    public ApiResult<Paging<BzzConfig>> otherBzzDate(){
        List<BzzConfig> list = bzzConfigService.list();
        Page<BzzConfig> objectPage = new Page<>();
        objectPage.setTotal(list.size());
        objectPage.setRecords(list);
        return ApiResult.ok(new Paging<>(objectPage));
    }


    /***
     *
     * 其它参数配置-BZZ数据-修改(新增)
     *
     */
    @PostMapping("/otherBzzDateUpdate")
    @OperationLog(name = "其它参数配置-BZZ数据-修改(新增)")
    @ApiOperation(value = "其它参数配置-BZZ数据-修改(新增)")
    public ApiResult<?> otherBzzDateUpdate(@RequestBody BzzConfig bzzConfig){
        boolean b = bzzConfigService.saveOrUpdate(bzzConfig);
        return ApiResult.ok(b);
    }

}

