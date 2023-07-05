package io.filpool.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.param.IncomeReleaseRecordPageParam;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 待释放记录表 Mapper 接口
 *
 * @author filpool
 * @since 2021-03-22
 */
@Repository
public interface IncomeReleaseRecordMapper extends BaseMapper<IncomeReleaseRecord> {

    /**
     * 查询用户资金账户当日线性释放总额
     * */
    @Select("SELECT COALESCE(SUM(once_amount),0) FROM fil_income_release_record WHERE days >= 1 AND asset_account_id = #{assetAccountId}")
    BigDecimal getTotalReleaseAmount(@Param("assetAccountId") Long assetAccountId);

    /**
     * 查询待释放资金账户列表
     * */
    @Select("SELECT asset_account_id FROM fil_income_release_record WHERE days >= 1 AND create_time < #{toDay} GROUP BY asset_account_id")
    List<Long> getAssetAccountList(@Param("toDay") Date toDay);


    /**
     * 统计用户累计挖矿收益
     */
    @Select("SELECT COALESCE(SUM(total_amount),0) FROM fil_income_release_record WHERE user_id=#{userId}")
    BigDecimal getTotalIncome(@Param("userId") Long userId) throws Exception;

    /**
     * 统计用户当前冻结收益
     */
    @Select("SELECT COALESCE(SUM(frozen_amount),0) FROM fil_income_release_record WHERE user_id=#{userId}")
    BigDecimal getFrozenIncome(@Param("userId") Long userId) throws Exception;

    /**
     * 统计用户累计释放收益
     */
    @Select("SELECT COALESCE(SUM(release_amount),0) FROM fil_income_release_record WHERE user_id=#{userId}")
    BigDecimal getReleaseIncome(@Param("userId") Long userId) throws Exception;

    /**
     * 统计用户今日挖矿收益
     * */
    @Select("SELECT COALESCE(SUM(total_amount),0) FROM fil_income_release_record WHERE user_id =#{userId} AND create_time >= #{todayStart}")
    BigDecimal getTodayIncome(@Param("userId") Long userId,@Param("todayStart") Date todayStart) throws Exception;

    /**
     * 统计用户今日释放收益
     * */
    @Select("SELECT COALESCE(SUM(release_amount),0) FROM fil_income_release_log WHERE user_id =#{userId} AND create_time >= #{todayStart}")
    BigDecimal getTodayRelease(@Param("userId") Long userId,@Param("todayStart") Date todayStart) throws Exception;
}
