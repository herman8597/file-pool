package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.RewardConfig;
import io.filpool.pool.param.RewardConfigPageParam;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奖励等级配置 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-11
 */
@Repository
public interface RewardConfigMapper extends BaseMapper<RewardConfig> {

    /**
     * 根据算力（1T=1经验值）获得对应的配置
     * @return
     * */
    @Select("SELECT * FROM `fil_reward_config` WHERE min_size = (SELECT MAX(min_size) FROM `fil_reward_config` WHERE min_size <= #{power})")
    RewardConfig getConfigByPower(@Param("power") BigDecimal power);

    @Select("SELECT * from\n" +
            "(\n" +
            "SELECT `level`,power_one_usdt_rate,power_two_usdt_rate,power_one_power_rate,power_two_power_rate,create_time,1 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,miner_one_usdt_rate,miner_two_usdt_rate,miner_one_power_rate,miner_two_power_rate,create_time,2 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,group_one_usdt_rate,group_two_usdt_rate,group_one_power_rate,group_two_power_rate,create_time,3 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,bzz_one_usdt_rate,bzz_two_usdt_rate,bzz_one_power_rate,bzz_two_power_rate,create_time,4 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")"+
            ")  t LIMIT #{pageIndex},#{pageSize}")
    List<RewardConfig> rewardAllocationList(@Param("pageIndex") Long pageIndex, @Param("pageSize") Long pageSize);

    @Select("SELECT `level`,power_one_usdt_rate,power_two_usdt_rate,power_one_power_rate,power_two_power_rate,create_time,2 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,miner_one_usdt_rate,miner_two_usdt_rate,miner_one_power_rate,miner_two_power_rate,create_time,1 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,group_one_usdt_rate,group_two_usdt_rate,group_one_power_rate,group_two_power_rate,create_time,3 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")\n" +
            "UNION\n" +
            "SELECT `level`,bzz_one_usdt_rate,bzz_two_usdt_rate,bzz_one_power_rate,bzz_two_power_rate,create_time,4 AS goodsType FROM fil_reward_config WHERE `name`IN(\"青铜\",\"白银\",\"黄金\",\"铂金\",\"钻石\")"
    )
    List<RewardConfig> rewardConfigsTotal();

    //修改云算力奖励配置
    @Update("update fil_reward_config set power_one_usdt_rate=#{rewardConfigPageParam.powerOneUsdtRate},power_one_power_rate=#{rewardConfigPageParam.powerOnePowerRate},power_two_power_rate=#{rewardConfigPageParam.powerTwoPowerRate},power_two_usdt_rate=#{rewardConfigPageParam.powerTwoUsdtRate} where level=#{rewardConfigPageParam.level}")
    Boolean updateOne(@Param("rewardConfigPageParam") RewardConfigPageParam rewardConfigPageParam);

    //修改矿机奖励配置
    @Update("update fil_reward_config set miner_one_usdt_rate=#{rewardConfigPageParam.powerOneUsdtRate},miner_one_power_rate=#{rewardConfigPageParam.powerOnePowerRate},miner_two_power_rate=#{rewardConfigPageParam.powerTwoPowerRate},miner_two_usdt_rate=#{rewardConfigPageParam.powerTwoUsdtRate} where level=#{rewardConfigPageParam.level}")
    Boolean updateTwo(@Param("rewardConfigPageParam") RewardConfigPageParam rewardConfigPageParam);

    //修改集群奖励配置
    @Update("update fil_reward_config set group_one_usdt_rate=#{rewardConfigPageParam.powerOneUsdtRate},group_one_power_rate=#{rewardConfigPageParam.powerOnePowerRate},group_two_power_rate=#{rewardConfigPageParam.powerTwoPowerRate},group_two_usdt_rate=#{rewardConfigPageParam.powerTwoUsdtRate} where level=#{rewardConfigPageParam.level}")
    Boolean updateThree(@Param("rewardConfigPageParam") RewardConfigPageParam rewardConfigPageParam);

    //修改节点奖励配置
    @Update("update fil_reward_config set bzz_one_usdt_rate=#{rewardConfigPageParam.powerOneUsdtRate},bzz_one_power_rate=#{rewardConfigPageParam.powerOnePowerRate},bzz_two_power_rate=#{rewardConfigPageParam.powerTwoPowerRate},bzz_two_usdt_rate=#{rewardConfigPageParam.powerTwoUsdtRate} where level=#{rewardConfigPageParam.level}")
    Boolean updateFor(@Param("rewardConfigPageParam") RewardConfigPageParam rewardConfigPageParam);
}
