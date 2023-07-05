package io.filpool.pool.service.impl;

import io.filpool.pool.entity.Series;
import io.filpool.pool.mapper.SeriesMapper;
import io.filpool.pool.service.SeriesService;
import io.filpool.pool.param.SeriesPageParam;
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
 * 币种系列表 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class SeriesServiceImpl extends BaseServiceImpl<SeriesMapper, Series> implements SeriesService {

    @Autowired
    private SeriesMapper seriesMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSeries(Series series) throws Exception {
        return super.save(series);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSeries(Series series) throws Exception {
        return super.updateById(series);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSeries(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Series> getSeriesPageList(SeriesPageParam seriesPageParam) throws Exception {
        Page<Series> page = new PageInfo<>(seriesPageParam, OrderItem.desc(getLambdaColumn(Series::getId)));
        LambdaQueryWrapper<Series> wrapper = new LambdaQueryWrapper<>();
        IPage<Series> iPage = seriesMapper.selectPage(page, wrapper);
        return new Paging<Series>(iPage);
    }

}
