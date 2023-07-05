package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("奖励记录返回")
public class RewardRecordVo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("奖励usdt数量")
    private BigDecimal usdtAmount;

    @ApiModelProperty("奖励算力数量")
    private BigDecimal powerAmount;

    @ApiModelProperty("订单USDT金额")
    private BigDecimal orderUsdtAmount;

    @ApiModelProperty("订单算力数量")
    private BigDecimal orderPowerAmount;

    @ApiModelProperty("算力单位")
    private String powerSymbol;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("邀请关系: 1直推 2间推")
    private Integer inviteType;

    @ApiModelProperty("奖励类型: 1邀请用户订单返佣  2补单返佣")
    private Integer type;

    @ApiModelProperty("订单类型: 1矿机  2云算力")
    private Integer orderType;

    @ApiModelProperty("用户账号")
    private String account;
}
