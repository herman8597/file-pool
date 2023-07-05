package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.RewardConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

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

}
