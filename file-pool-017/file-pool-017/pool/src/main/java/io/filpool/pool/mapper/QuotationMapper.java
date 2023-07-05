package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Quotation;
import io.filpool.pool.param.QuotationPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 行情信息 Mapper 接口
 *
 * @author filpool
 * @since 2021-04-14
 */
@Repository
public interface QuotationMapper extends BaseMapper<Quotation> {


}
