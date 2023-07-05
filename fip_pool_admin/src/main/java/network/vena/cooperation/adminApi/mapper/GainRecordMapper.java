package network.vena.cooperation.adminApi.mapper;

import java.util.List;

import network.vena.cooperation.base.vo.UpdateBalance;
import org.apache.ibatis.annotations.Param;
import network.vena.cooperation.adminApi.entity.GainRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: gain_record
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
public interface GainRecordMapper extends BaseMapper<GainRecord> {

    @Select("SELECT d.account,c.api_key,c.available from (SELECT a.api_key,b.gain_total - a.total as \"available\" from (SELECT api_key,SUM(quantity) as \"total\" from balance_modify_pipeline WHERE asset = 'XCH' AND type in (13,28) GROUP BY api_key) a INNER JOIN (SELECT api_key,SUM(amount) as \"gain_total\" from gain_record WHERE asset = 'XCH' GROUP BY api_key)b ON a.api_key = b.api_key) c INNER JOIN auth_user d ON c.api_key = d.api_key AND c.available > 0")
    List<UpdateBalance> query();
}
