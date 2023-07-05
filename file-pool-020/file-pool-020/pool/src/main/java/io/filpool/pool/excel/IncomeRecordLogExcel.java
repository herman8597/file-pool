package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class IncomeRecordLogExcel {
    private static final long serialVersionUID = 1L;

    public void setAmount(BigDecimal amount) {
        this.amount = amount.stripTrailingZeros();
    }

    @Excel(name = "释放类型", width = 20)
    private String type;

    @Excel(name = "释放金额", width = 20)
    private BigDecimal amount;

/*    @Excel(name = "挖矿币种", width = 20)
    private String symbol;*/

    @Excel(name = "释放日期", width = 20,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
