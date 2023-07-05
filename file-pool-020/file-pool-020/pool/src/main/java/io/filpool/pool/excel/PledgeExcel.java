package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PledgeExcel {

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "质押币种", width = 15)
    private String symbol;

   /* @Excel(name = "质押要求", width = 15)
    private BigDecimal pledgeRequire;*/

    @Excel(name = "质押账户总金额", width = 15)
    private BigDecimal amount;

    @Excel(name = "消费GAS总金额", width = 15)
    private BigDecimal gasSum;

}
