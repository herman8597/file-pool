package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.RechargeRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 充值记录 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-10
 */
@Repository
public interface RechargeRecordMapper extends BaseMapper<RechargeRecord> {
//    @Select("SELECT * FROM fil_recharge_record WHERE create_time=(SELECT MAX(create_time) WHERE to_address = #{address})")
//    RechargeRecord getLastRecord(@Param("address") String address);

    /**
     * 查询最近的充值记录时间
     *
     * @param address 充值地址
     * @return 充值时间
     */
    @Select("SELECT MAX(create_time) FROM fil_recharge_record WHERE to_address = #{address}")
    Date getLastRecordTime(@Param("address") String address);
}
