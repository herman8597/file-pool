package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.param.CurrencyPageParam;
import io.filpool.pool.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加币种表（币种列表-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加币种表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加币种表", response = ApiResult.class)
    public ApiResult<Boolean> addCurrency(@Validated(Add.class) @RequestBody Currency currency) throws Exception {
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

}

