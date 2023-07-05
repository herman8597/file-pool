package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.About;
import io.filpool.pool.param.AboutPageParam;
import io.filpool.pool.service.AboutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 平台信息表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("sys/about")
@Module("pool")
@Api(value = "平台信息表API", tags = {"平台信息(系统后台)"})
public class SysAboutController extends BaseController {

    @Autowired
    private AboutService aboutService;

    /**
     * 添加平台信息表（平台信息-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加平台信息表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加平台信息表", response = ApiResult.class)
    public ApiResult<Boolean> addAbout(@Validated(Add.class) @RequestBody About about) throws Exception {
        if (about.getIsEnable() == null) {
            about.setIsEnable(true);
        }
        if (about.getIsEnable()) {
            String language = about.getLanguage();
            //先将全部该语言的平台信息改为禁用
            boolean update = aboutService.lambdaUpdate().set(About::getIsEnable, 0).eq(About::getLanguage, language).eq(About::getType, about.getType()).update();
            if (update) {
                about.setCreateTime(new Date());
                about.setUpdateTime(new Date());
                boolean flag = aboutService.saveAbout(about);
                return ApiResult.result(flag);
            }
        } else {
            about.setCreateTime(new Date());
            boolean flag = aboutService.saveAbout(about);
            return ApiResult.result(flag);
        }
        return ApiResult.result(false);
    }

    /**
     * 修改平台信息表（平台信息-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改平台信息表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改平台信息表", response = ApiResult.class)
    public ApiResult<Boolean> updateAbout(@Validated(Update.class) @RequestBody About about) throws Exception {
        about.setUpdateTime(new Date());
        boolean flag = aboutService.updateAbout(about);
        return ApiResult.result(flag);
    }

    /**
     * 删除平台信息表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除平台信息表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除平台信息表", response = ApiResult.class)
    public ApiResult<Boolean> deleteAbout(@PathVariable("id") Long id) throws Exception {
        boolean flag = aboutService.deleteAbout(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取平台信息表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "平台信息表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "平台信息表详情", response = About.class)
    public ApiResult<About> getAbout(@PathVariable("id") Long id) throws Exception {
        About about = aboutService.getById(id);
        return ApiResult.ok(about);
    }

    /**
     * 平台信息表分页列表（平台信息）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "平台信息表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "平台信息表分页列表", response = About.class)
    public ApiResult<Paging<About>> getAboutPageList(@Validated @RequestBody AboutPageParam aboutPageParam) throws Exception {
        Paging<About> paging = aboutService.getAboutPageList(aboutPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 平台信息表分页列表（平台信息-删除）
     */
    @PostMapping("/aboutDel")
    @OperationLog(name = "平台信息-删除", type = OperationLogType.PAGE)
    @ApiOperation(value = "平台信息-删除", response = About.class)
    public ApiResult<Boolean> aboutDel(@Validated @RequestBody AboutPageParam aboutPageParam) throws Exception {
        boolean b = aboutService.removeById(aboutPageParam.getId());
        return ApiResult.ok(b);
    }

}

