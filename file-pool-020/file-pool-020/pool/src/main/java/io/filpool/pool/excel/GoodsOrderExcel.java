package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsOrderExcel {

    @Excel(name = "订单编号", width = 15)
    private String orderNumber;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "商品类型", width = 15,replace = {"矿机_1","云算力_2","矿机集群_3"})
    private Integer goodType;

    @Excel(name = "商品名称", width = 15)
    private String goodName;

    @Excel(name = "合约期限(天)", width = 15)
    private Integer contractDays;

    @ApiModelProperty("购买数量")
    @Excel(name = "购买数量", width = 15)
    private Integer quantity;

    @Excel(name = "算力数量", width = 15)
    private BigDecimal totalPower;

    @Excel(name = "算力是否有效", width = 15)
    private String isEffect;

    @Excel(name = "支付方式", width = 15)
    private String symbol;

    @Excel(name = "实际支付金额", width = 15)
    private BigDecimal discountPrice;

    @Excel(name = "下单时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "状态", width = 15,replace = {"待付款_1","已完成_2","已取消_3","已过期_4"})
    private Integer status;

}
