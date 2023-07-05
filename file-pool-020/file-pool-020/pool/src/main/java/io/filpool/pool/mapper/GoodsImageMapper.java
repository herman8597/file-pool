package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.GoodsImage;
import io.filpool.pool.param.GoodsImagePageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 商品封面图 Mapper 接口
 *
 * @author filpool
 * @since 2021-04-06
 */
@Repository
public interface GoodsImageMapper extends BaseMapper<GoodsImage> {


}
