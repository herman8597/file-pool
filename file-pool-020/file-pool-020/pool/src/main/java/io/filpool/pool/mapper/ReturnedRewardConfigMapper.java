package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.ReturnedRewardConfig;
import io.filpool.pool.param.ReturnedRewardConfigPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 020社区奖励 Mapper 接口
 *
 * @author filpool
 * @since 2021-05-27
 */
@Repository
public interface ReturnedRewardConfigMapper extends BaseMapper<ReturnedRewardConfig> {

    /**
     * 根据经验值获取该用户的社区等级
     * @return
     * */
    @Select("SELECT * FROM `returned_reward_config` WHERE experience = (SELECT MAX(experience) FROM `returned_reward_config` WHERE experience <= #{communityExperience}) and goods_type=#{goodsType}")
    ReturnedRewardConfig getConfigByPower(@Param("communityExperience") BigDecimal communityExperience,@Param("goodsType") Integer goodsType);

    /**
     * 根据经验值获取该用户的社区等级2
     * @return
     * */
    @Select("SELECT * FROM `returned_reward_config` WHERE experience = (SELECT MAX(experience) FROM `returned_reward_config` WHERE experience <= #{communityExperience}) LIMIT 1")
    ReturnedRewardConfig getConfigByPowerTwo(@Param("communityExperience") BigDecimal communityExperience);

}
