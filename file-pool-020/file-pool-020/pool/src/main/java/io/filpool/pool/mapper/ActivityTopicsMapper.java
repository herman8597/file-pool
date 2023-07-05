package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.ActivityTopics;
import io.filpool.pool.param.ActivityTopicsPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 商品活动专题 Mapper 接口
 *
 * @author filpool
 * @since 2021-06-25
 */
@Repository
public interface ActivityTopicsMapper extends BaseMapper<ActivityTopics> {


}
