package network.vena.cooperation.adminApi.mapper;

import java.util.List;

import network.vena.cooperation.adminApi.entity.OperatingIncome;
import network.vena.cooperation.base.vo.InviteDetailVO;
import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.AuthUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface AuthUserMapper extends BaseMapper<AuthUser> {

    @Select("SELECT account FROM auth_user where api_key= (SELECT invite_api_key FROM `auth_invited_pipeline` WHERE api_key=#{apiKey})")
    String getInviterAccount(String apiKey);

    @Select("SELECT account FROM `auth_user` WHERE api_key=#{apiKey}")
    String getAccount(String apiKey);

    @Select("SELECT * FROM (SELECT account AS account FROM `auth_user` WHERE api_key=#{apiKey}) AS account," +
            "(SELECT account AS subordinateAccount FROM `auth_user` WHERE api_key=#{superior}) AS BB," +
            "(SELECT account AS originAccount FROM `auth_user` WHERE api_key=#{Origin}) AS CC")
    OperatingIncome getAccount$SubordinateAccount$OriginAccount(@Param("apiKey") String apiKey, @Param("superior") String superior, @Param("Origin")String Origin);

    @Select("SELECT account,phone FROM `auth_user` WHERE api_key=#{apiKey}")
    AuthUser getAccountAndPhone(String apiKey);

    @Select("SELECT account,phone,default_account_no FROM `auth_user` WHERE api_key=#{apiKey}")
    AuthUser getAccountAndPhoneAndDefaultAccountNo(String apiKey);

    @Select("SELECT api_key FROM `auth_user` WHERE account=#{account}")
    String getApiKeysByAccount(String account);

    @Select("SELECT (SELECT account FROM auth_user WHERE api_key=#{apiKey}) AS account,(SELECT IFNULL(MAX( BB.`level` ),'-1')  " +
            "FROM ( SELECT `level` FROM distribution_detail WHERE api_key = #{apiKey} UNION ALL SELECT `level_direct` AS `level` " +
            "FROM distribution_cheat WHERE api_key = #{apiKey} ) AS BB) AS `level`,(SELECT SUM(IF(unit='GB',quantity/1000,quantity)) " +
            "FROM weight WHERE api_key=#{apiKey} AND `status`=1 AND type in (1,7)) AS selfPurchase,(SELECT SUM(quantity) FROM `balance_modify_pipeline` " +
            "WHERE api_key=#{apiKey} AND type=1) AS paymentAmount FROM DUAL")
    InviteDetailVO getAccount$Level$SelfPurchase$PaymentAmount(String apikey);

    @Update("UPDATE  `auth_user` SET auth_status=#{auditStatus} where api_key=#{apiKey}")
    Integer updateAuthStatus(@Param("apiKey") String apiKey,@Param("auditStatus") Integer auditStatus);


}
