package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Series;
import io.filpool.pool.param.SeriesPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 币种系列表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-10
 */
@Repository
public interface SeriesMapper extends BaseMapper<Series> {


}
