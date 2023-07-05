package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.AppVersion;
import io.filpool.pool.param.AppVersionPageParam;
import io.filpool.pool.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 版本更新表 控制器
 *
 * @author filpool
 * @since 2021-03-30
 */
@Slf4j
@RestController
@RequestMapping("sys/appVersion")
@Module("pool")
@Api(value = "版本更新表API", tags = {"版本更新表"})
public class SysAppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 删除版本更新表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除版本更新表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除版本更新表", response = ApiResult.class)
    public ApiResult<Boolean> deleteAppVersion(@PathVariable("id") Long id) throws Exception {
        boolean flag = appVersionService.deleteAppVersion(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取版本更新表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "版本更新表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "版本更新表详情", response = AppVersion.class)
    public ApiResult<AppVersion> getAppVersion(@PathVariable("id") Long id) throws Exception {
        AppVersion appVersion = appVersionService.getById(id);
        return ApiResult.ok(appVersion);
    }

    /**
     * 版本更新表分页列表（版本配置列表）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "版本更新表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "版本更新表分页列表", response = AppVersion.class)
    public ApiResult<Paging<AppVersion>> getAppVersionPageList(@Validated @RequestBody AppVersionPageParam appVersionPageParam) throws Exception {
        Paging<AppVersion> paging = appVersionService.getAppVersionPageList(appVersionPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 添加版本更新表（版本配置列表-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加版本更新表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加版本更新表", response = ApiResult.class)
    public ApiResult<Boolean> addAppVersion(@Validated(Add.class) @RequestBody AppVersion appVersion) throws Exception {
        appVersion.setCreateTime(new Date());
        boolean flag = appVersionService.saveAppVersion(appVersion);
        return ApiResult.result(flag);
    }

    /**
     * 修改版本更新表（版本配置列表-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改版本更新表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改版本更新表", response = ApiResult.class)
    public ApiResult<Boolean> updateAppVersion(@Validated(Update.class) @RequestBody AppVersion appVersion) throws Exception {
        boolean flag = appVersionService.updateAppVersion(appVersion);
        return ApiResult.result(flag);
    }
}

