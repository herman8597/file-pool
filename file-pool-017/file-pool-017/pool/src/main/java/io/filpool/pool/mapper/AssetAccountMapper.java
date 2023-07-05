package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.model.AssetsSumVo;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 账户资产表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-10
 */
@Repository
public interface AssetAccountMapper extends BaseMapper<AssetAccount> {

    @Select("SELECT fca.* FROM fil_asset_account fca LEFT JOIN currency c ON fca.currency_id = c.id WHERE user_id = #{userId} AND c.is_enable=1 ORDER BY c.order_params DESC")
    List<AssetAccount> findByUser(@Param("userId") Long userId);

    @Select("SELECT symbol as currencyName,COALESCE(SUM(available),0) as available,COALESCE(SUM(frozen),0) as frozen FROM fil_asset_account GROUP BY symbol")
    List<AssetsSumVo> sumAssets();

    /**
     * 获取有算力用户列表
     * */
    @Select("SELECT * FROM fil_asset_account WHERE user_id in (SELECT user_id FROM fil_power_account WHERE amount > 0) AND currencyId = #{currencyId}")
    List<AssetAccount> getUserByPower(@Param("currencyId") Long currencyId);
}
