package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.PowerOrder;
import io.filpool.pool.vo.UserPowerVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 算力订单表 Mapper 接口
 *
 * @author filpool
 * @since 2021-04-01
 */
@Repository
public interface PowerOrderMapper extends BaseMapper<PowerOrder> {

    /**
     * 统计用户团队业绩 自购+直推+间推
     *
     * @param userId
     * @return
     */
    @Select("SELECT COALESCE(SUM(amount),0) FROM fil_power_order WHERE type != 4 AND user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id = #{userId}) or invite_user_id = #{userId})")
    BigDecimal sumTeamPower(@Param("userId") Long userId);

    /**
     * 统计用户有效算力
     * @param userId
     * @return
     */
    @Select("SELECT COALESCE(SUM(amount),0) FROM fil_power_order WHERE is_effect = 1 and user_id = #{userId} and power_symbol = #{symbol}")
    BigDecimal sumEffectAmount(@Param("userId") Long userId,@Param("symbol") String symbol);

    /**
     * 统计用户有效算力
     * @param userId
     * @return
     */
    @Select("SELECT COALESCE(SUM(amount),0) AS amount,2 AS powerType FROM fil_power_order WHERE is_effect = 1 and user_id = #{userId} and power_symbol = 'FIL'\n" +
            "UNION\n" +
            "SELECT COALESCE(SUM(amount),0) AS amount,1 AS powerType FROM fil_power_order WHERE is_effect = 1 and user_id = #{userId} and power_symbol = 'XCH'\n" +
            "UNION\n" +
            "SELECT COALESCE(SUM(amount),0) AS amount,3 AS powerType FROM fil_power_order WHERE is_effect = 1 and user_id = #{userId} and power_symbol = 'BZZ'\n" +
            "\n")
    List<UserPowerVo> sumEffectAmountTwo(@Param("userId") Long userId);

    /**
     * 获取平台普通用户有效总算力
     */
    @Select("SELECT COALESCE(SUM(amount),0) FROM fil_power_order WHERE user_id IN(SELECT id FROM fil_user WHERE type = 0) AND is_effect = 1 AND power_symbol = #{symbol}")
    BigDecimal sumSystemPower(@Param("symbol") String symbol);

    /**
     * 查询有算力普通用户列表
     * */
    @Select("SELECT user_id FROM fil_power_order WHERE user_id IN(SELECT id FROM fil_user WHERE type = 0) AND is_effect = 1 AND power_symbol = #{symbol} GROUP BY user_id")
    List<Long> getUserIdList(@Param("symbol") String symbol);


    /**
     * 获取平台有效总算力
     */
    @Select("SELECT COALESCE(SUM(amount),0) FROM fil_power_order WHERE user_id IN(SELECT id FROM fil_user) AND is_effect = 1 AND power_symbol = #{symbol}")
    BigDecimal sumSystemPowerBySymbol(@Param("symbol") String symbol);
}
