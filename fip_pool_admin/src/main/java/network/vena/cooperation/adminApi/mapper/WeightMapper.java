package network.vena.cooperation.adminApi.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import network.vena.cooperation.adminApi.entity.Weight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: weight
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface WeightMapper extends BaseMapper<Weight> {

    @Select(value = "SELECT sum( IF(unit='GB',quantity/1000,quantity)) FROM `weight` where type in (1,7) And status =1")
    BigDecimal sumQuantity();

    @Select(value = "SELECT sum(payment_quantity) FROM `weight` where type in (1,7) And status =1 ")
    BigDecimal sumPaymentQuantity();

    @Select(value = "SELECT * FROM `weight` where left_days>0 AND `status` =1 AND `type` > 0 AND api_key=#{apiKey} AND date_add( create_time, INTERVAL left_days DAY ) > #{date} ")
    List<Weight> listValidHashrate(@Param("apiKey") String apiKey, @Param("date") Date date);

    @Select(value = "SELECT id,api_key,quantity,service_charge_rate,related_name,type FROM `weight` where left_days>0 AND `status` =1 AND `type` =#{type} AND api_key=#{apiKey} AND date_add( create_time, INTERVAL left_days DAY ) > #{date} ")
    List<Weight> listValidHashrateBytype(@Param("apiKey") String apiKey, @Param("date") Date date, @Param("type") Integer type);

    @Select(value = "SELECT count(id) FROM `weight` WHERE `status`=1")
    Long countTotal();

    @Select(value = "SELECT sum(IF(unit='TB',quantity*1000,quantity)) FROM `weight` where api_key=#{apiKey} and `status` =1  ")
    BigDecimal sumSelfQuantityByApiKey(@Param("apiKey") String apiKey);

/*    @Select(value = "<script> SELECT sum(IF(unit='TB',quantity*1000,quantity)) FROM `weight` where api_key in" +
            " <foreach item='item' index='index' collection='apiKeys' open='(' separator=',' close=')'> " +
            "#{item} </foreach> and `status` =1 </script> ")*/

    @Select({
            "<script>",
            "select",
            "sum(IF(unit='TB',quantity*1000,quantity))",
            "from `weight`",
            "where type=1 and status=1 and api_key in " ,
            "<foreach collection='apiKeys' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    BigDecimal sumQuantityInApiKeys(@Param("apiKeys") List<String> list);

    @Select({
            "<script>",
            "select",
            "sum(IF(unit='TB',quantity*1000,quantity))",
            "from `weight`",
            "where type=1 and status=1 and power_type=1 and api_key in " ,
            "<foreach collection='apiKeys' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
        //004累计统计fil算力
    BigDecimal sumQuantityInApiKeysFil(@Param("apiKeys") List<String> list);

    @Select({
            "<script>",
            "select",
            "sum(IF(unit='TB',quantity*1000,quantity))",
            "from `weight`",
            "where type=1 and status=1 and power_type=2 and api_key in " ,
            "<foreach collection='apiKeys' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
        //004累计统计xch算力
    BigDecimal sumQuantityInApiKeysXch(@Param("apiKeys") List<String> list);


    @Select(value = "SELECT sum(IF(unit='TB',quantity*1000,quantity)) FROM `weight` where api_key=#{apiKey} and `status` =1 and `type`=1 ")
    BigDecimal sumPurchaseQuantityByApiKey(@Param("apiKey") String apiKey);

    //统计用户fil算力
    @Select(value = "SELECT sum(IF(unit='TB',quantity*1000,quantity)) FROM `weight` where api_key=#{apiKey} and `status` =1 and `type`=1 and power_type=1")
    BigDecimal sumPurchaseQuantityByApiKeyFil(@Param("apiKey") String apiKey);

    //统计用户xch算力
    @Select(value = "SELECT sum(IF(unit='TB',quantity*1000,quantity)) FROM `weight` where api_key=#{apiKey} and `status` =1 and `type`=1 and power_type=2")
    BigDecimal sumPurchaseQuantityByApiKeyXch(@Param("apiKey") String apiKey);


    @Select(value = "SELECT * FROM `weight` where api_key=#{apiKey} and `status` =1 and `type`=8 ")
    List<Weight> getGeneralizeByApiKey(@Param("apiKey") String apiKey);

    @Select("SELECT * from weight WHERE api_key = #{apiKey} AND status = 1 AND type=#{type} AND power_type=1 AND date_add( create_time, INTERVAL left_days DAY ) > #{date} order by create_time desc")
    List<Weight> getWeightAllByApiKeyAndType(@Param("apiKey") String apiKey,@Param("type") Integer type,@Param("date") Date date);

    @Select("SELECT * from weight  WHERE api_key = #{apiKey} AND status = 1 AND type=#{type} AND power_type=1 AND service_charge_rate=0.149 AND date_add( create_time, INTERVAL left_days DAY ) > #{date} order by create_time desc")
    List<Weight> getMinimumWeightAllByApiKeyAndType(@Param("apiKey") String apiKey,@Param("type") Integer type,@Param("date") Date date);

    @Select("SELECT * from weight  WHERE api_key = #{apiKey} AND status = 1 AND type=#{type} AND power_type=1 AND service_charge_rate=0.2 AND date_add( create_time, INTERVAL left_days DAY ) > #{date} order by create_time desc")
    List<Weight> getBeBeneathWeightAllByApiKeyAndType(@Param("apiKey") String apiKey,@Param("type") Integer type,@Param("date") Date date);

    @Select("SELECT sum(IF(unit='GB',quantity/1000,quantity)) FROM `weight` WHERE api_key=#{apiKey} AND power_type=2 AND `status`=1")
    BigDecimal sumQuantityTwo(@Param("apiKey")String apiKey);

    @Select("SELECT sum(IF(unit='GB',quantity/1000,quantity)) FROM weight WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time) = 1 AND type in(1,10) AND status=1")
    BigDecimal sumSalesVolumeYesterday();
}



