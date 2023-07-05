package io.filpool.pool.controller.admin;

import io.filpool.pool.entity.Series;
import io.filpool.pool.service.SeriesService;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.SeriesPageParam;
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
 * 币种系列表 控制器
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@RestController
@RequestMapping("sys/series")
@Module("pool")
@Api(value = "币种系列表API", tags = {"币种系列表"})
public class SeriesController extends BaseController {

    @Autowired
    private SeriesService seriesService;

    /**
     * 添加币种系列表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加币种系列表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加币种系列表", response = ApiResult.class)
    public ApiResult<Boolean> addSeries(@Validated(Add.class) @RequestBody Series series) throws Exception {
        boolean flag = seriesService.saveSeries(series);
        return ApiResult.result(flag);
    }

    /**
     * 修改币种系列表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改币种系列表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改币种系列表", response = ApiResult.class)
    public ApiResult<Boolean> updateSeries(@Validated(Update.class) @RequestBody Series series) throws Exception {
        boolean flag = seriesService.updateSeries(series);
        return ApiResult.result(flag);
    }

    /**
     * 删除币种系列表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除币种系列表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除币种系列表", response = ApiResult.class)
    public ApiResult<Boolean> deleteSeries(@PathVariable("id") Long id) throws Exception {
        boolean flag = seriesService.deleteSeries(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取币种系列表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "币种系列表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "币种系列表详情", response = Series.class)
    public ApiResult<Series> getSeries(@PathVariable("id") Long id) throws Exception {
        Series series = seriesService.getById(id);
        return ApiResult.ok(series);
    }

    /**
     * 币种系列表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "币种系列表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "币种系列表分页列表", response = Series.class)
    public ApiResult<Paging<Series>> getSeriesPageList(@Validated @RequestBody SeriesPageParam seriesPageParam) throws Exception {
        Paging<Series> paging = seriesService.getSeriesPageList(seriesPageParam);
        return ApiResult.ok(paging);
    }

}

