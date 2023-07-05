package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Order;
import io.filpool.pool.param.OrderPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 订单表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-08
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {


}
