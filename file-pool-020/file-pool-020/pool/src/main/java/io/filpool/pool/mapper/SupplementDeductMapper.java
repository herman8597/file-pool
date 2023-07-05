package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.SupplementDeduct;
import io.filpool.pool.param.SupplementDeductPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 算力补单扣除记录 Mapper 接口
 *
 * @author filpool
 * @since 2021-04-02
 */
@Repository
public interface SupplementDeductMapper extends BaseMapper<SupplementDeduct> {


}
