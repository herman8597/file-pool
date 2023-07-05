package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Banner;
import io.filpool.pool.param.BannerPageParam;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * banner 控制器
 *
 * @author filpool
 * @since 2021-03-04
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/banner")
@Module("pool")
@Api(value = "bannerAPI", tags = {"banner"})
public class BannerController extends BaseController {

    @Autowired
    private BannerService bannerService;

    /**
     * banner分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "banner分页列表", response = Banner.class)
    public ApiResult<List<Banner>> getBannerPageList() throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        LambdaQueryWrapper<Banner> wr = Wrappers.lambdaQuery(Banner.class)
                .eq(Banner::getIsEnable, true)
                .eq(Banner::getLanguage, locale.getLanguage() + "-" + locale.getCountry())
                .orderByDesc(Banner::getCreateTime);
        Page<Banner> page = new Page<>(1, 5);
        page = bannerService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(page.getRecords());
    }

}

