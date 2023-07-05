package network.vena.cooperation.adminApi.mapper;

import java.math.BigDecimal;
import java.util.List;

import network.vena.cooperation.adminApi.vo.BalanceKouChu;
import network.vena.cooperation.adminApi.vo.PledgeKouChuTwo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.Balance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface BalanceMapper extends BaseMapper<Balance> {

    @Select("SELECT d.account,c.api_key,c.sub_total from (SELECT a.api_key,b.gain_total - a.total as \"sub_total\" from (SELECT api_key,SUM(IF(type = 4,-(quantity),quantity)) as \"total\" from balance_modify_pipeline WHERE asset = 'XCH' AND type != 17 GROUP BY api_key) a INNER JOIN (SELECT api_key,SUM(available + frozen) as \"gain_total\" from balance WHERE asset = 'XCH' GROUP BY api_key)b ON a.api_key = b.api_key) c INNER JOIN auth_user d ON c.api_key = d.api_key AND c.sub_total >0")
    List<BalanceKouChu> kouchu();

    @Select("SELECT a.account,s.* from (SELECT t.*,t.sum_total -1 as 'daikouchu',f.mining_pledge as 'zhiyazongshu',7.305 * (t.sum_total - 1) as 'daikouchuzongshu',f.mining_pledge - (7.305 * (t.sum_total - 1)) as 'kouchuhoushengyu' from (SELECT api_key,IF(power_type=1,'FIL',IF(power_type = 2,'XCH','other')) as 'power_asset',SUM(IF(unit = 'GB',quantity / 1000,quantity)) as 'sum_total' FROM `weight` WHERE create_time BETWEEN DATE('2020-10-11 00:00:00') AND DATE('2020-10-31 00:00:00') AND `status`=1 AND type=1 GROUP BY api_key,power_type) t INNER JOIN earnings_pledge_detail f on t.api_key = f.api_key) s INNER JOIN auth_user a on s.api_key = a.api_key")
    List<PledgeKouChuTwo> queryAll();

    @Update("update earnings_pledge_detail set mining_pledge=mining_pledge-#{daikouchuzongshu} where api_key=#{apiKey} and asset=#{powerAsset}")
    Integer earningsReleaseLogKouChu(@Param("apiKey") String apiKey, @Param("powerAsset") String powerAsset, @Param("daikouchuzongshu") BigDecimal daikouchuzongshu);

    @Update("update earnings_pledge_detail set mining_pledge_limit=0 where api_key=#{apiKey} and asset='FIL'")
    Integer updateEarning(@Param("apiKey") String apiKey);
}
