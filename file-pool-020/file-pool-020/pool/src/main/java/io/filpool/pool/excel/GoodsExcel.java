package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsExcel {

    @Excel(name = "商品类型", width = 15,replace = {"矿机_1","云算力_2","矿机集群_3"})
    private Integer type;

    @Excel(name = "挖矿币种")
    private String symbol;

    @Excel(name = "商品名称", width = 15)
    private String name;

    @Excel(name = "商品标签", width = 15)
    private String tag;

    @Excel(name = "总份量", width = 15)
    private Integer quantitySum;

    @Excel(name = "已售份量", width = 15)
    private Integer soldQuantity;

    @Excel(name = "剩余", width = 15)
    private Integer quantity;

    @Excel(name = "原价", width = 15)
    private BigDecimal price;

    @Excel(name = "折扣价", width = 15)
    private BigDecimal discountPrice;

    @Excel(name = "权重", width = 15)
    private Integer weight;

    @ApiModelProperty("是否显示在首页（0：不显示，1显示）")
    @Excel(name = "展示在首页", width = 15,replace = {"不显示_0","显示_1"})
    private Integer isShowPage;

    @Excel(name = "状态", width = 15,replace = {"未开始_0","进行中_1","已售罄_2"})
    private Integer status;

    @Excel(name = "更新时间", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
