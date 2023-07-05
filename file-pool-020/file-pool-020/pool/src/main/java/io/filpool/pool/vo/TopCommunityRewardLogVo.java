package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 社区奖励（APP社区统计）
 *
 * @author filpay
 * @since 2020-08-19
 */
@Data
@ApiModel(value = "TopCommunityRewardLogVo对象")
public class TopCommunityRewardLogVo implements Serializable {

    @ApiModelProperty("昨日收益（USDT）")
    private BigDecimal yesterdayRewardUsdt;

    @ApiModelProperty("FIL昨日收益（T）")
    private BigDecimal yesterdayRewardFil;

    @ApiModelProperty("XCH昨日收益（T）")
    private BigDecimal yesterdayRewardXch;

    @ApiModelProperty("BZZ昨日收益(节点)")
    private BigDecimal yesterdayRewardBzz;

    @ApiModelProperty("累计总收益（USDT）")
    private BigDecimal  rewardUsdt;

    @ApiModelProperty("FIL累计总收益（T）")
    private BigDecimal  rewardFil;

    @ApiModelProperty("XCH累计总收益（T）")
    private BigDecimal  rewardXch;

    @ApiModelProperty("BZZ累计总收益(节点)")
    private BigDecimal rewardBzz;
}
