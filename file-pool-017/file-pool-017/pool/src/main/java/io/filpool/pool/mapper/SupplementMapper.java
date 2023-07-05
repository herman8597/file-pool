package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.param.SupplementPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 算力补单 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-29
 */
@Repository
public interface SupplementMapper extends BaseMapper<Supplement> {


}
