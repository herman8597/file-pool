package io.filpool.pool.service;

import io.filpool.pool.entity.About;
import io.filpool.pool.param.AboutPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 平台信息表 服务类
 *
 * @author filpool
 * @since 2021-03-02
 */
public interface AboutService extends BaseService<About> {

    /**
     * 保存
     *
     * @param about
     * @return
     * @throws Exception
     */
    boolean saveAbout(About about) throws Exception;

    /**
     * 修改
     *
     * @param about
     * @return
     * @throws Exception
     */
    boolean updateAbout(About about) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAbout(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param aboutQueryParam
     * @return
     * @throws Exception
     */
    Paging<About> getAboutPageList(AboutPageParam aboutPageParam) throws Exception;

}
