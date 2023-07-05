package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.param.AreaCodePageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 国家地区表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-02
 */
@Repository
public interface AreaCodeMapper extends BaseMapper<AreaCode> {


}
