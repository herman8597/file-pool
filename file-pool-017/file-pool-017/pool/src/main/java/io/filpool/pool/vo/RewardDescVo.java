package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RewardDescVo implements Serializable {
    @Excel(name = "订单编号", width = 20)
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @Excel(name = "商品类型", width = 20)
    @ApiModelProperty("商品类型")
    private String goodType;

    @Excel(name = "支付金额", width = 20)
    @ApiModelProperty("支付金额")
    private BigDecimal discountPrice;

    @Excel(name = "下单算力数量", width = 20)
    @ApiModelProperty("下单算力数量")
    private Integer quantity;

    @Excel(name = "奖励usdt数量", width = 20)
    @ApiModelProperty("奖励usdt数量")
    private BigDecimal usdtAmount;

    @Excel(name = "奖励算力数量", width = 20)
    @ApiModelProperty("奖励算力数量")
    private BigDecimal powerAmount;

    @Excel(name = "奖励时间", width = 20)
    @ApiModelProperty("奖励时间")
    private Date createTime;
}
