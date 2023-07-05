package network.vena.cooperation.adminApi.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.EarningsPledgeDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: earnings_pledge_detail
 * @Author: jeecg-boot
 * @Date:   2020-11-30
 * @Version: V1.0
 */
public interface EarningsPledgeDetailMapper extends BaseMapper<EarningsPledgeDetail> {

    @Update("UPDATE `earnings_pledge_detail` SET mining_pledge_limit=#{miningPledgeLimit}")
    Integer updateMiningPledgeLimit(@Param("miningPledgeLimit") BigDecimal miningPledgeLimit);

}
