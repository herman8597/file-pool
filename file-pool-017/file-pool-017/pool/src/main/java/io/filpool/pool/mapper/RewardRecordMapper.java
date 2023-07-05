package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.RewardRecord;
import io.filpool.pool.param.RewardRecordPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单奖励记录 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-12
 */
@Repository
public interface RewardRecordMapper extends BaseMapper<RewardRecord> {

    /**
     * 用户获取奖励金额
     * */
    @Select("SELECT COALESCE(SUM(usdt_amount),0) FROM fil_reward_record WHERE user_id = #{userId} AND status = 1 AND type IN (1,2)")
    BigDecimal sumTotalAmount(@Param("userId") Long userId);

    /**
     * 用户获取奖励算力
     * */
    @Select("SELECT COALESCE(SUM(power_amount),0) FROM fil_reward_record WHERE user_id = #{userId} AND status = 1 AND type IN (1,2)")
    BigDecimal sumTotalPower(@Param("userId") Long userId);
}
