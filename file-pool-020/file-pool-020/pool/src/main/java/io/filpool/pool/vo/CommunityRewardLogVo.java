package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 社区奖励（后台）
 *
 * @author filpay
 * @since 2020-08-19
 */
@Data
@ApiModel(value = "CommunityRewardLog对象")
public class CommunityRewardLogVo implements Serializable {

    @ApiModelProperty("用户账号")
    @Excel(name = "用户账号")
    private String account;

    @ApiModelProperty("等级")
    @Excel(name = "等级")
    private Integer level;

    @ApiModelProperty("上级邀请人")
    @Excel(name = "上级邀请人")
    private String superiorAccount;

    @ApiModelProperty("团队人数")
    @Excel(name = "团队人数")
    private Integer teamSize;

    @ApiModelProperty("累计总收益U")
    @Excel(name = "累计总收益U")
    private BigDecimal rewardUsdt;

    @ApiModelProperty("Fil累计总收益")
    @Excel(name = "Fil累计总收益")
    private BigDecimal rewardFil;

    @ApiModelProperty("Xch累计总收益")
    @Excel(name = "Xch累计总收益")
    private BigDecimal rewardXch;

    @ApiModelProperty("Bzz累计总收益")
    @Excel(name = "Bzz累计总收益")
    private BigDecimal rewardBzz;
}
