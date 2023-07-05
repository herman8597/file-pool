package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.param.CurrencyPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 币种表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-08
 */
@Repository
public interface CurrencyMapper extends BaseMapper<Currency> {


}
