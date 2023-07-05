package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.CommunityRewardLog;
import io.filpool.pool.param.CommunityRewardLogPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * 社区(市场)奖励记录表 Mapper 接口
 *
 * @author filpool
 * @since 2021-06-01
 */
@Repository
public interface CommunityRewardLogMapper extends BaseMapper<CommunityRewardLog> {

    @Select("SELECT * FROM fil_community_reward_log WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time) = 1 AND reward_user_id=#{userId}")
    List<CommunityRewardLog> topCommunityReward(@Param("userId") Long userId);
}
