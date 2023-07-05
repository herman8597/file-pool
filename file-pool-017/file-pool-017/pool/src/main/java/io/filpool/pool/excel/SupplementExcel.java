package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SupplementExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "补单编号", width = 15)
    private Long id;

    @Excel(name = "补单类型", width = 15,replace = {"云算力_1","矿机_2"})
    private Integer type;

    @ApiModelProperty("算力数量")
    @Excel(name = "算力数量", width = 15)
    private BigDecimal tbSum;

    @Excel(name = "支付方式", width = 15)
    private String assetName;

    @Excel(name = "支付金额", width = 15)
    private BigDecimal amountPrice;

    @Excel(name = "质押金额(FIL)", width = 15)
    private BigDecimal pledgePrice;

    @Excel(name = "gas费数量", width = 15)
    private BigDecimal gasPrice;

    @Excel(name = "合约天数", width = 15)
    private Integer contractDays;

    @Excel(name = "完成时间", width = 15)
    private Date updateTime;

}
