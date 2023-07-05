package network.vena.cooperation.adminApi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import network.vena.cooperation.adminApi.entity.UserIdInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: user_id_info
 * @Author: jeecg-boot
 * @Date: 2020-07-01
 * @Version: V1.0
 */
public interface UserIdInfoMapper extends BaseMapper<UserIdInfo> {

    @Select("SELECT * FROM user_id_info WHERE api_key=#{apiKey} AND audit_status =1 LIMIT 1")
    UserIdInfo findByApiKey(@Param("apiKey") String apiKey);

}
