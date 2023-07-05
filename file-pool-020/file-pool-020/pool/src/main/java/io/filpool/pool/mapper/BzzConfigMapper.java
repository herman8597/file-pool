package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.param.BzzConfigPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 *  Mapper 接口
 *
 * @author filpool
 * @since 2021-06-24
 */
@Repository
public interface BzzConfigMapper extends BaseMapper<BzzConfig> {


}
