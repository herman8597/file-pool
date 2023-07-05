package network.vena.cooperation.adminApi.mapper;

import java.math.BigDecimal;

import network.vena.cooperation.base.vo.AwardVo;
import network.vena.cooperation.adminApi.entity.DistributionDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: distribution_detail
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface DistributionDetailMapper extends BaseMapper<DistributionDetail> {

    @Select("SELECT grandchildren_purchase+children_purchase as hashrateTotal, grandchildren_purchase,children_purchase FROM `distribution_detail` where api_key=#{apiKey}")
    AwardVo distributionDetail(String apiKey);

    @Select("SELECT SUM(IF(unit='GB',quantity/1000,quantity)) AS total  FROM `weight`where api_key=#{apiKey} AND `status` =1")
    BigDecimal sumHashrateTotal(String apiKey);
}
