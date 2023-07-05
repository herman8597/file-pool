package network.vena.cooperation.adminApi.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.BalanceModifyPipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: balance_modify_pipeline
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface BalanceModifyPipelineMapper extends BaseMapper<BalanceModifyPipeline> {

    @Select("SELECT sum(quantity) FROM `balance_modify_pipeline` WHERE type =12 AND `status` =1  AND api_key=#{apiKey}")
    BigDecimal sumRewardsDistribution(@Param("apiKey") String apiKey);

    @Select(" SELECT SUM(quantity) from balance_modify_pipeline WHERE api_key=#{apiKey} and type =12")
    BigDecimal getDistribution(String apiKey);

    @Select(" SELECT * from balance_modify_pipeline WHERE api_key=#{apiKey} and type =12")
    List<BalanceModifyPipeline> getRebateRecord(String apiKey);



}
