package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.param.BzzConfigPageParam;
import io.filpool.pool.service.BzzConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  控制器
 *
 * @author filpool
 * @since 2021-06-23
 */
@Slf4j
@RestController
@RequestMapping("/sys/bzzConfig")
@Module("pool")
@Api(value = "API", tags = {""})
public class SysBzzConfigController extends BaseController {

    @Autowired
    private BzzConfigService bzzConfigService;

    /**
     * 添加
     */
    @PostMapping("/add")
    @OperationLog(name = "添加", type = OperationLogType.ADD)
    @ApiOperation(value = "添加", response = ApiResult.class)
    public ApiResult<Boolean> addBzzConfig(@Validated(Add.class) @RequestBody BzzConfig bzzConfig) throws Exception {
        boolean flag = bzzConfigService.saveBzzConfig(bzzConfig);
        return ApiResult.result(flag);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @OperationLog(name = "修改", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改", response = ApiResult.class)
    public ApiResult<Boolean> updateBzzConfig(@Validated(Update.class) @RequestBody BzzConfig bzzConfig) throws Exception {
        boolean flag = bzzConfigService.updateBzzConfig(bzzConfig);
        return ApiResult.result(flag);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除", response = ApiResult.class)
    public ApiResult<Boolean> deleteBzzConfig(@PathVariable("id") Long id) throws Exception {
        boolean flag = bzzConfigService.deleteBzzConfig(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "详情", type = OperationLogType.INFO)
    @ApiOperation(value = "详情", response = BzzConfig.class)
    public ApiResult<BzzConfig> getBzzConfig(@PathVariable("id") Long id) throws Exception {
        BzzConfig bzzConfig = bzzConfigService.getById(id);
        return ApiResult.ok(bzzConfig);
    }

    /**
     * 分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "分页列表", response = BzzConfig.class)
    public ApiResult<Paging<BzzConfig>> getBzzConfigPageList(@Validated @RequestBody BzzConfigPageParam bzzConfigPageParam) throws Exception {
        Paging<BzzConfig> paging = bzzConfigService.getBzzConfigPageList(bzzConfigPageParam);
        return ApiResult.ok(paging);
    }

}

