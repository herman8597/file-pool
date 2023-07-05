package network.vena.cooperation.adminApi.mapper;

import java.util.List;

import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.adminApi.provider.AuthInvitedPipelineProvider;
import network.vena.cooperation.base.vo.InviteDetailVO;
import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * @Description: auth_invited_pipeline
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface AuthInvitedPipelineMapper extends BaseMapper<AuthInvitedPipeline> {

    @Select("SELECT IFNULL(phone,email) AS account FROM auth_user WHERE api_key = (SELECT invite_api_key FROM `auth_invited_pipeline` WHERE api_key=#{apiKey})")
    String getInviter(String apiKey);

    @Select("SELECT SUM( A.total ) AS inviteCount  FROM ( SELECT count( 1 ) AS total  FROM auth_invited_pipeline WHERE invite_api_key = #{apiKey} UNION ALL SELECT \tcount( 1 ) AS total  \tFROM \tauth_invited_pipeline  \tWHERE invite_api_key \n" +
            "\tIN ( SELECT api_key FROM auth_invited_pipeline WHERE invite_api_key = #{apiKey} )  ) AS A")
    Integer getInvitedCumulative(String apiKey);

    @Select("SELECT  (SELECT account FROM auth_user WHERE api_key=#{apiKey}) AS account," +
            " (SELECT invitation_code FROM auth_invitation_code WHERE api_key=#{apiKey}) AS invitationCode," +
            " (SELECT au.account FROM auth_invited_pipeline AS aip ,auth_user AS au WHERE aip.api_key=#{apiKey} " +
            "AND aip.invite_api_key=au.api_key) AS inviter, (SELECT  SUM(A.total) FROM (SELECT count( 1 ) AS total  FROM auth_invited_pipeline " +
            " WHERE invite_api_key = #{apiKey} UNION ALL SELECT count( 1 ) AS total  FROM auth_invited_pipeline " +
            " WHERE invite_api_key IN ( SELECT api_key FROM auth_invited_pipeline  WHERE invite_api_key = #{apiKey}) ) AS A ) " +
            "AS inviteCount,(SELECT SUM(quantity) AS quantity FROM  `balance_modify_pipeline` WHERE  api_key=#{apiKey} " +
            "AND type=12 AND asset='USDT' ) AS award,(SELECT IFNULL(MAX( BB.`level` ),'-1') AS `level` FROM " +
            "(SELECT `level`  FROM distribution_detail WHERE api_key=#{apiKey} UNION ALL SELECT `level_direct` AS `level` " +
            "FROM distribution_cheat WHERE api_key=#{apiKey} ) AS BB) AS `level` FROM DUAL")
    AuthInvitedPipeline getAccount$code$inviteer$inviteCount$award$level(String apiKey);

    @SelectProvider(type = AuthInvitedPipelineProvider.class, method = "inviteDetailByApiKey")
    List<InviteDetailVO> inviteDetailByApiKey(@Param("Q") QueryParam queryParam);
}
