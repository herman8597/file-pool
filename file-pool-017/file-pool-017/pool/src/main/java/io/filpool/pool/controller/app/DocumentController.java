package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Document;
import io.filpool.pool.request.DocumentPageRequest;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.service.DocumentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.log.annotation.Module;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * 文档中心 控制器
 *
 * @author filpool
 * @since 2021-03-04
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/document")
@Module("pool")
@Api(value = "文档中心API", tags = {"课堂、帮助、项目动态、公告API"})
public class DocumentController extends BaseController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/getDocumentList")
    @ApiOperation("文章列表")
    public ApiResult<List<Document>> getPageList(@RequestBody DocumentPageRequest request) throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        LambdaQueryWrapper<Document> wr = Wrappers.lambdaQuery(Document.class)
                .eq(Document::getLanguage, locale.getLanguage() + "-" + locale.getCountry());
        if (request.getType() != null) {
            wr.eq(Document::getType, request.getType());
        }
        Page<Document> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = documentService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(page.getRecords());
    }
}

