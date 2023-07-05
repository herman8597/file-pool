package network.vena.cooperation.adminApi.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.TotalRevenueLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: total_revenue_log
 * @Author: jeecg-boot
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface TotalRevenueLogMapper extends BaseMapper<TotalRevenueLog> {

    @Select("SELECT * FROM `total_revenue_log` where DATE_FORMAT(grant_date,'%Y-%m-%d')=DATE_FORMAT(#{date},'%Y-%m-%d')")
    TotalRevenueLog getByDate(@Param("date") Date date);

}
