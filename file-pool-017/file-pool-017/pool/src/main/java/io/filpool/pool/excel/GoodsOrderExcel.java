package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsOrderExcel {

    @Excel(name = "质押币种", width = 15)
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群")
    private Integer goodType;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("合约期限(天)")
    private String contractPeriod;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("算力数量")
    private BigDecimal totalPower;

    @ApiModelProperty("算力是否有效")
    private BigDecimal isEffect;

    @ApiModelProperty("支付方式")
    private String symbol;

    @ApiModelProperty("实际支付金额")
    private BigDecimal discountPrice;

    @Excel(name = "下单时间", width = 15)
    private Date createTime;

    @NotNull(message = "订单状态: 1待付款 2已完成 3已取消 4已过期不能为空")
    @Excel(name = "状态", width = 15,replace = {"待付款_1","已完成_2","已取消_3","已过期_4"})
    private Integer status;

}
