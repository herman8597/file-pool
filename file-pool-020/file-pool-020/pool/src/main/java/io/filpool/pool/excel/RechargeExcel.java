package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RechargeExcel{
    private static final long serialVersionUID = 1L;

    @Excel(name = "充值单号", width = 15)
    private Long id;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "币种代码", width = 15)
    private String symbol;

    @Excel(name = "充币数量", width = 15)
    private BigDecimal amount;

    @Excel(name = "发送地址", width = 15)
    private String fromAddress;

    @Excel(name = "接收地址", width = 15)
    private String toAddress;

    @Excel(name = "交易ID", width = 15)
    private String transHash;

    @Excel(name = "充币状态", width = 15,replace = {"成功_1","失败_2","等待确认_3"})
    private Integer type;

    @Excel(name = "充值时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
