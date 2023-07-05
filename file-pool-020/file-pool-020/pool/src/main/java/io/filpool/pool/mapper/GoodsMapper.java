package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.param.GoodsPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 商品表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-08
 */
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {


}
