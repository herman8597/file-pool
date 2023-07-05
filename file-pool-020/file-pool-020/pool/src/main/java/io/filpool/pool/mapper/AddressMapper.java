package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.Address;
import io.filpool.pool.param.AddressPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 地址表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-08
 */
@Repository
public interface AddressMapper extends BaseMapper<Address> {


}
