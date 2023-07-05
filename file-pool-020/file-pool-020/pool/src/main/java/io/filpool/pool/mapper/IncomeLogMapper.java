package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.IncomeLog;
import io.filpool.pool.param.IncomeLogPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 挖矿收益日志 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-24
 */
@Repository
public interface IncomeLogMapper extends BaseMapper<IncomeLog> {


}
