package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.About;
import io.filpool.pool.request.AboutRequest;
import io.filpool.pool.service.impl.AboutServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * 平台信息表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/about")
@Module("pool")
@Api(value = "平台信息表API", tags = {"平台信息"})
public class AboutController extends BaseController {

    @Autowired
    private AboutServiceImpl aboutService;

    /**
     * 根据类型获得平台信息
     */
    @PostMapping("/getAboutByType")
    @ApiOperation(value = "根据类型获得平台信息", response = About.class)
    public ApiResult<About> getAboutPageList(@RequestBody AboutRequest request) throws Exception {
        if (request.getType() == null){
            throw new FILPoolException("illegal.params");
        }
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        List<About> abouts = aboutService.lambdaQuery()
                .orderByDesc(About::getCreateTime)
                .eq(About::getType,request.getType())
                .eq(About::getLanguage,locale.getLanguage()+"-"+locale.getCountry()).list();
        return ApiResult.ok(ObjectUtils.isEmpty(abouts)?null:abouts.get(0));
    }

}

