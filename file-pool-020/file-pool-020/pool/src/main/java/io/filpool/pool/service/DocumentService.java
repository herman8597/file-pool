package io.filpool.pool.service;

import io.filpool.pool.entity.Document;
import io.filpool.pool.param.DocumentPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 *  服务类
 *
 * @author filpool
 * @since 2021-03-30
 */
public interface DocumentService extends BaseService<Document> {

    /**
     * 保存
     *
     * @param document
     * @return
     * @throws Exception
     */
    boolean saveDocument(Document document) throws Exception;

    /**
     * 修改
     *
     * @param document
     * @return
     * @throws Exception
     */
    boolean updateDocument(Document document) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteDocument(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param documentQueryParam
     * @return
     * @throws Exception
     */
    Paging<Document> getDocumentPageList(DocumentPageParam documentPageParam) throws Exception;

}
