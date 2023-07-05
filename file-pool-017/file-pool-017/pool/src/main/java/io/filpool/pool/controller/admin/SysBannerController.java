package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Banner;
import io.filpool.pool.param.BannerPageParam;
import io.filpool.pool.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * banner 控制器
 *
 * @author filpool
 * @since 2021-03-04
 */
@Slf4j
@RestController
@RequestMapping("/banner")
@Module("pool")
@Api(value = "bannerAPI", tags = {"banner"})
public class SysBannerController extends BaseController {

    @Autowired
    private BannerService bannerService;

    /**
     * 删除banner
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除banner", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除banner", response = ApiResult.class)
    public ApiResult<Boolean> deleteBanner(@PathVariable("id") Long id) throws Exception {
        boolean flag = bannerService.deleteBanner(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取banner详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "banner详情", type = OperationLogType.INFO)
    @ApiOperation(value = "banner详情", response = Banner.class)
    public ApiResult<Banner> getBanner(@PathVariable("id") Long id) throws Exception {
        Banner banner = bannerService.getById(id);
        return ApiResult.ok(banner);
    }

    /**
     * banner分页列表（banner管理）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "banner分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "banner分页列表", response = Banner.class)
    public ApiResult<Paging<Banner>> getBannerPageList( @RequestBody BannerPageParam bannerPageParam) throws Exception {
        Paging<Banner> paging = bannerService.getBannerPageList(bannerPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 添加banner（banner管理-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加banner", type = OperationLogType.ADD)
    @ApiOperation(value = "添加banner", response = ApiResult.class)
    public ApiResult<Boolean> addBanner(@Validated(Add.class) @RequestBody Banner banner) throws Exception {
        boolean flag = bannerService.saveBanner(banner);
        return ApiResult.result(flag);
    }

    /**
     * 修改banner（banner管理-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改banner", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改banner", response = ApiResult.class)
    public ApiResult<Boolean> updateBanner(@Validated(Update.class) @RequestBody Banner banner) throws Exception {
        boolean flag = bannerService.updateBanner(banner);
        return ApiResult.result(flag);
    }



}

