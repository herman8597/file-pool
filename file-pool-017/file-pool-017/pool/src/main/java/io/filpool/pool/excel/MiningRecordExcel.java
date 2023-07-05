package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MiningRecordExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "发放日期", width = 20)
    private Date createTime;

    @Excel(name = "矿池总份额", width = 20)
    private BigDecimal amount;

    @Excel(name = "矿池总收益（FIL）", width = 20)
    private BigDecimal power;

    @Excel(name = "参与挖矿人数", width = 20)
    private Integer userNumber;

    @Excel(name = "挖矿币种", width = 20)
    private String symbol;

    @Excel(name = "备注", width = 20)
    private String remark;

}
