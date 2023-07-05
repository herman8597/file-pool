package io.filpool.pool.service;

import io.filpool.pool.entity.Series;
import io.filpool.pool.param.SeriesPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 币种系列表 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface SeriesService extends BaseService<Series> {

    /**
     * 保存
     *
     * @param series
     * @return
     * @throws Exception
     */
    boolean saveSeries(Series series) throws Exception;

    /**
     * 修改
     *
     * @param series
     * @return
     * @throws Exception
     */
    boolean updateSeries(Series series) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSeries(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param seriesQueryParam
     * @return
     * @throws Exception
     */
    Paging<Series> getSeriesPageList(SeriesPageParam seriesPageParam) throws Exception;

}
