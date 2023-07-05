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
     * 用户获取对应币种奖励算力
     * */
    @Select("SELECT COALESCE(SUM(power_amount),0) FROM fil_reward_record WHERE user_id = #{userId} AND status = 1 AND type IN (1,2) AND power_symbol = #{symbol}")
    BigDecimal sumTotalPower(@Param("userId") Long userId,@Param("symbol") String symbol);


    /**
     *   查询该用户给上级返了多少usdt
     * */
    @Select("SELECT COALESCE(SUM(usdt_amount),0) FROM fil_reward_record WHERE down_user_id = #{userId} and user_id=#{inviteUserId} AND status = 1 AND type IN (1,2)")
    BigDecimal sumSuperiorTotalAmount(@Param("userId") Long userId,@Param("inviteUserId") Long inviteUserId);

    /**
     * 查询该用户给上级返了多少算力
     * */
    @Select("SELECT COALESCE(SUM(power_amount),0) FROM fil_reward_record WHERE down_user_id = #{userId} and user_id=#{inviteUserId} AND status = 1 AND type IN (1,2)")
    BigDecimal sumSuperiorTotalPower(@Param("userId") Long userId,@Param("inviteUserId") Long inviteUserId);
}
