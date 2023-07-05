package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.IncomeReleaseLog;
import io.filpool.pool.param.IncomeReleaseLogPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 释放日志表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-22
 */
@Repository
public interface IncomeReleaseLogMapper extends BaseMapper<IncomeReleaseLog> {


}
