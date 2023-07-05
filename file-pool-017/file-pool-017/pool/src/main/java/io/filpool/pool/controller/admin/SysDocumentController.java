package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.log.annotation.Module;
import io.filpool.pool.entity.Document;
import io.filpool.pool.request.SysPageRequest;
import io.filpool.pool.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 文档管理 控制器
 *
 * @author filpool
 * @since 2021-03-20
 */
@Slf4j
@RestController
@RequestMapping("sys/document")
@Module("pool")
@Api(value = "文档管理API", tags = {"文档管理"})
public class SysDocumentController {
    @Autowired
    private DocumentService documentService;

    /**
     * 添加项目动态表（文档中心）
     */
    @PostMapping("/getPageList")
    @ApiOperation("获取文档列表")
    ApiResult<Paging<Document>> getPageList(@RequestBody SysPageRequest request) throws Exception {
        LambdaQueryWrapper<Document> wr = Wrappers.lambdaQuery(Document.class).orderByDesc(Document::getRank).orderByDesc(Document::getCreateTime);
        if (!StringUtils.isEmpty(request.getTitle())) {
            wr.like(Document::getTitle, request.getTitle());
        }
        if (!StringUtils.isEmpty(request.getLanguage())) {
            wr.eq(Document::getLanguage, request.getLanguage());
        }
        if (request.getStartTime() != null) {
            wr.ge(Document::getCreateTime, request.getStartTime());
        }
        if (request.getEndTime() != null) {
            wr.le(Document::getCreateTime, request.getEndTime());
        }
        if (request.getIsEnable() != null) {
            wr.eq(Document::getIsEnable, request.getIsEnable());
        }
        Page<Document> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = documentService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(new Paging<>(page));
    }

    /**
     * 添加项目动态表（文档中心-新增-编辑）
     */
    @PostMapping("saveOrUpdate")
    @ApiOperation("新增或者更新")
    public ApiResult<Boolean> saveOrUpdate(@RequestBody Document document) throws Exception {
        if (document.getType() == null || document.getType() <= 0) {
            return ApiResult.fail("类型不能为空");
        }
        if (StringUtils.isEmpty(document.getTitle())) {
            return ApiResult.fail("标题不能为空");
        }
        if (document.getIsEnable() == null) {
            return ApiResult.fail("请设置是否启用");
        }
        if (document.getId() != null) {
            Document byId = documentService.getById(document.getId());
            if (byId == null) {
                return ApiResult.fail("文档不存在");
            }
            BeanUtils.copyProperties(document, byId);
            document.setUpdateTime(new Date());
            documentService.updateDocument(document);
        } else {
            document.setCreateTime(new Date());
            document.setUpdateTime(new Date());
            documentService.saveDocument(document);
        }
        return ApiResult.ok(true);
    }
}
