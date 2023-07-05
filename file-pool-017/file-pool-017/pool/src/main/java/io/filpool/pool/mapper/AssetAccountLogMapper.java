package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.AssetAccountLog;
import io.filpool.pool.param.AssetAccountLogPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 账户资产变化表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-10
 */
@Repository
public interface AssetAccountLogMapper extends BaseMapper<AssetAccountLog> {

    /**
     * 过滤重复记录
     */
    @Select("SELECT t.* FROM (SELECT record_id,max(create_time) as create_time FROM fil_asset_account_log WHERE asset_account_id = #{accountId} GROUP BY record_id) a LEFT JOIN fil_asset_account_log t ON t.record_id=a.record_id and t.create_time=a.create_time ORDER BY create_time DESC LIMIT #{pageIndex},#{pageSize}")
    List<AssetAccountLog> getPageList(@Param("accountId") Long accountId,@Param("pageIndex") Long pageIndex,@Param("pageIndex") Long pageSize);

}
