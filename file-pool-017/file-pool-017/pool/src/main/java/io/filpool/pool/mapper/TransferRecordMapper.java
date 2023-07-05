package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.TransferRecord;
import io.filpool.pool.param.TransferRecordPageParam;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;

/**
 * 用户划转记录 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-11
 */
@Repository
public interface TransferRecordMapper extends BaseMapper<TransferRecord> {


}
