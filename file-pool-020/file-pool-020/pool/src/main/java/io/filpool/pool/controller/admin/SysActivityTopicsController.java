package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.ActivityTopics;
import io.filpool.pool.param.ActivityTopicsPageParam;
import io.filpool.pool.service.ActivityTopicsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品活动专题 控制器
 *
 * @author filpool
 * @since 2021-06-25
 */
@Slf4j
@RestController
@RequestMapping("/sys/activityTopics")
@Module("pool")
@Api(value = "商品活动专题API", tags = {"商品活动专题"})
public class SysActivityTopicsController extends BaseController {

    @Autowired
    private ActivityTopicsService activityTopicsService;

    /**
     * 添加商品活动专题
     */
    @PostMapping("/add")
    @OperationLog(name = "添加商品活动专题", type = OperationLogType.ADD)
    @ApiOperation(value = "添加商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> addActivityTopics(@Validated(Add.class) @RequestBody ActivityTopics activityTopics) throws Exception {
        boolean flag = activityTopicsService.saveActivityTopics(activityTopics);
        return ApiResult.result(flag);
    }

    /**
     * 修改商品活动专题
     */
    @PostMapping("/update")
    @OperationLog(name = "修改商品活动专题", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> updateActivityTopics(@Validated(Update.class) @RequestBody ActivityTopics activityTopics) throws Exception {
        boolean flag = activityTopicsService.updateActivityTopics(activityTopics);
        return ApiResult.result(flag);
    }

    /**
     * 删除商品活动专题
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除商品活动专题", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> deleteActivityTopics(@PathVariable("id") Long id) throws Exception {
        boolean flag = activityTopicsService.deleteActivityTopics(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取商品活动专题详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "商品活动专题详情", type = OperationLogType.INFO)
    @ApiOperation(value = "商品活动专题详情", response = ActivityTopics.class)
    public ApiResult<ActivityTopics> getActivityTopics(@PathVariable("id") Long id) throws Exception {
        ActivityTopics activityTopics = activityTopicsService.getById(id);
        return ApiResult.ok(activityTopics);
    }

    /**
     * 商品活动专题分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "商品活动专题分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品活动专题分页列表", response = ActivityTopics.class)
    public ApiResult<Paging<ActivityTopics>> getActivityTopicsPageList(@Validated @RequestBody ActivityTopicsPageParam activityTopicsPageParam) throws Exception {
        Paging<ActivityTopics> paging = activityTopicsService.getActivityTopicsPageList(activityTopicsPageParam);
        return ApiResult.ok(paging);
    }

    /***
     *
     * 批量刪除
     */
    @PostMapping("/batchDel")
    @OperationLog(name = "批量刪除活動", type = OperationLogType.PAGE)
    @ApiOperation(value = "批量刪除活動", response = ActivityTopics.class)
    public ApiResult<?> batchDel(@RequestParam(name = "ids",required = true) String ids) throws Exception {
        activityTopicsService.removeByIds(Arrays.asList(ids.split(",")));
        return ApiResult.ok("批量删除成功！");
    }

    /***
     *
     * 批量刪除
     */
    @PostMapping("/delById")
    @OperationLog(name = "批量刪除活動", type = OperationLogType.PAGE)
    @ApiOperation(value = "批量刪除活動", response = ActivityTopics.class)
    public ApiResult<?> delById(@RequestParam(name = "id",required = true) String id) throws Exception {
        activityTopicsService.removeById(id);
        return ApiResult.ok("删除成功！");
    }


}

