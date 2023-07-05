package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawMoneyExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "充值单号", width = 15)
    private Long id;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "币种代码", width = 15)
    private String symbol;

    @Excel(name = "提币数量", width = 15)
    private BigDecimal amount;

    @Excel(name = "手续费", width = 15)
    private BigDecimal fee;

    @Excel(name = "提币地址", width = 15)
    private String toAddress;

    @Excel(name = "交易ID", width = 15)
    private String txHash;

    @Excel(name = "提币时间", width = 15)
    private Date createTime;

    @Excel(name = "审核时间", width = 15)
    private Date authTime;

//    @ApiModelProperty("状态：1成功，2 失败，3 等待确认")
    @Excel(name = "充币状态", width = 15,replace = {"待审核_1", "审核通过_2", "审核拒绝_3", "提币中_4", "提币成功_5", "提币失败_6"})
    private Integer status;
}
