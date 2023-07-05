package io.filpool.pool.service.impl;

import io.filpool.pool.entity.Document;
import io.filpool.pool.mapper.DocumentMapper;
import io.filpool.pool.service.DocumentService;
import io.filpool.pool.param.DocumentPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  服务实现类
 *
 * @author filpool
 * @since 2021-03-30
 */
@Slf4j
@Service
public class DocumentServiceImpl extends BaseServiceImpl<DocumentMapper, Document> implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDocument(Document document) throws Exception {
        return super.save(document);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDocument(Document document) throws Exception {
        return super.updateById(document);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDocument(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Document> getDocumentPageList(DocumentPageParam documentPageParam) throws Exception {
        Page<Document> page = new PageInfo<>(documentPageParam, OrderItem.desc(getLambdaColumn(Document::getCreateTime)));
        LambdaQueryWrapper<Document> wrapper = new LambdaQueryWrapper<>();
        IPage<Document> iPage = documentMapper.selectPage(page, wrapper);
        return new Paging<Document>(iPage);
    }

}
