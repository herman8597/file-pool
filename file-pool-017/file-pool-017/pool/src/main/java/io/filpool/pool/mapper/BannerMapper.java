package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Banner;
import io.filpool.pool.param.BannerPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * banner Mapper 接口
 *
 * @author filpool
 * @since 2021-03-04
 */
@Repository
public interface BannerMapper extends BaseMapper<Banner> {


}
