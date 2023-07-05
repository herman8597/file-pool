package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.AppVersion;
import io.filpool.pool.param.AppVersionPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 版本更新表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-30
 */
@Repository
public interface AppVersionMapper extends BaseMapper<AppVersion> {
    @Select("SELECT * FROM app_version WHERE version_type=#{versionType} ORDER BY create_time DESC LIMIT 1")
    AppVersion getNewestVersion(@Param("versionType") Integer versionType);
}
