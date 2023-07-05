package network.vena.cooperation.adminApi.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.DistributionConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: distribution_config
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface DistributionConfigMapper extends BaseMapper<DistributionConfig> {

    @Select("SELECT * FROM distribution_config  WHERE `key` = #{earnings}")
    List<DistributionConfig> getConfByKey(String earnings);

    //@Select("SELECT IFNULL(level_limit,level_direct) FROM `distribution_cheat`  WHERE api_key=#{apiKey}")
    @Select("SELECT MAX(A.`level`) AS `level` FROM (SELECT `level` FROM distribution_detail WHERE api_key=#{apiKey} UNION ALL SELECT `level_direct` FROM distribution_cheat WHERE api_key=#{apiKey} ) AS A")
    Integer getLevel(String apiKey);
}
