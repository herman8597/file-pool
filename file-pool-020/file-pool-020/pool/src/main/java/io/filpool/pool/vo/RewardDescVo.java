package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RewardDescVo implements Serializable {
    @ApiModelProperty("奖励类型: 1邀请用户订单返佣 2补单返佣")
    private Integer type;

    @Excel(name = "订单编号", width = 20)
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @Excel(name = "商品类型", width = 20,replace = {"矿机_1","云算力_2","矿机集群_3","云算力补单_4","矿机补单_5","矿机集群补单_6"})
    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群")
    private Integer goodType;

    @Excel(name = "支付金额", width = 20)
    @ApiModelProperty("支付金额")
    private BigDecimal discountPrice;

    @Excel(name = "下单算力数量", width = 20)
    @ApiModelProperty("下单算力数量")
    private BigDecimal quantity;

    @Excel(name = "奖励usdt数量", width = 20)
    @ApiModelProperty("奖励usdt数量")
    private BigDecimal usdtAmount;

    @Excel(name = "奖励算力数量", width = 20)
    @ApiModelProperty("奖励算力数量")
    private BigDecimal powerAmount;

    @Excel(name = "算力单位", width = 20)
    @ApiModelProperty("算力单位")
    private String powerSymbol;

    @Excel(name = "奖励时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
