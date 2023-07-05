package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.InviteRecord;

import io.filpool.pool.param.InviteRecordPageParam;
import io.filpool.pool.request.InviteRecordRequest;
import io.filpool.pool.vo.RewardDescVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 邀请关系表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-02
 */
@Repository
public interface InviteRecordMapper extends BaseMapper<InviteRecord> {

    /**
     * 统计累计推荐用户个数
     *
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(id) FROM fil_invite_record WHERE invite_user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id = #{userId}) or invite_user_id = #{userId}")
    Integer countInviteTotalNumber(@Param("userId") Long userId);

    /**
     * 统计邀请用户中购买人数
     *
     * */
    @Select("SELECT COUNT(distinct user_id) FROM fil_order WHERE status = 2 AND user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id = #{userId}) or invite_user_id = #{userId})")
    Integer countBuyNumber(@Param("userId") Long userId);


    /**
     * 统计直推用户个数
     *
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(id) FROM fil_invite_record WHERE invite_user_id = #{userId}")
    Integer countInviteOneNumber(@Param("userId") Long userId);

    /**
     * 统计间推用户个数
     *
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(id) FROM fil_invite_record WHERE invite_user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id = #{userId})")
    Integer countInviteTowNumber(@Param("userId") Long userId);

    @Select("SELECT *,2 as relation FROM fil_invite_record WHERE invite_user_id in(SELECT user_id FROM fil_invite_record WHERE invite_user_id = #{id})\n" +
            "UNION\n" +
            "SELECT *,1 as relation FROM fil_invite_record WHERE invite_user_id =#{id}")
    List<InviteRecord> queryAllUser(Long id);

    List<InviteRecord> queryInviteRecord(@Param("InviteRecordRequest") InviteRecordRequest inviteRecordRequest);

    List<RewardDescVo> queryRewardDescFour(@Param("InviteRecordPageParam") InviteRecordPageParam inviteRecordPageParam);

    List<RewardDescVo> queryRewardDescFourTotal(@Param("InviteRecordPageParam") InviteRecordPageParam inviteRecordPageParam);
}
