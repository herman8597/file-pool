package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class IncomeRecordLogExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "25%直接释放", width = 20)
    private BigDecimal firstAmount;

    @Excel(name = "线性释放金额", width = 20)
    private BigDecimal releaseAmount;

    @Excel(name = "挖矿币种", width = 20)
    private String symbol;

    @Excel(name = "释放日期", width = 20)
    private Date createTime;

}
