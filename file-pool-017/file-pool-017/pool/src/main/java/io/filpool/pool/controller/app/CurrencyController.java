package io.filpool.pool.controller.app;

import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.CurrencyPageParam;
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
 * 币种表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX +"/currency")
@Module("pool")
@Api(value = "币种表API", tags = {"币种表"})
public class CurrencyController extends BaseController {

    @Autowired
    private CurrencyService currencyService;

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
     * 币种表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "币种表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "币种表分页列表", response = Currency.class)
    public ApiResult<Paging<Currency>> getCurrencyPageList(@Validated @RequestBody CurrencyPageParam currencyPageParam) throws Exception {
        Paging<Currency> paging = currencyService.getCurrencyPageList(currencyPageParam);
        return ApiResult.ok(paging);
    }

}

