package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.CurrencyConfig;
import io.filpool.pool.param.CurrencyConfigPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 币种配置 Mapper 接口
 *
 * @author filpool
 * @since 2021-05-31
 */
@Repository
public interface CurrencyConfigMapper extends BaseMapper<CurrencyConfig> {


}
