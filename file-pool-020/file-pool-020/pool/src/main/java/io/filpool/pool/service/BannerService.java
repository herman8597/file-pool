package io.filpool.pool.service;

import io.filpool.pool.entity.Banner;
import io.filpool.pool.param.BannerPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * banner 服务类
 *
 * @author filpool
 * @since 2021-03-04
 */
public interface BannerService extends BaseService<Banner> {

    /**
     * 保存
     *
     * @param banner
     * @return
     * @throws Exception
     */
    boolean saveBanner(Banner banner) throws Exception;

    /**
     * 修改
     *
     * @param banner
     * @return
     * @throws Exception
     */
    boolean updateBanner(Banner banner) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBanner(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param bannerQueryParam
     * @return
     * @throws Exception
     */
    Paging<Banner> getBannerPageList(BannerPageParam bannerPageParam) throws Exception;

}
