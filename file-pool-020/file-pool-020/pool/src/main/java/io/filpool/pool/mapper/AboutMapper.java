package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.About;
import io.filpool.pool.param.AboutPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 平台信息表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-02
 */
@Repository
public interface AboutMapper extends BaseMapper<About> {
}
