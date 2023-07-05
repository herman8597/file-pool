package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PledgeDescExcel {


    @Excel(name = "质押币种", width = 15)
    private String symbol;

    @Excel(name = "质押金额", width = 15)
    private BigDecimal amount;

    @Excel(name = "GAS费用(FIL)", width = 15)
    private BigDecimal gasSum;

    @Excel(name = "创建时间", width = 15)
    private Date createTime;

}
