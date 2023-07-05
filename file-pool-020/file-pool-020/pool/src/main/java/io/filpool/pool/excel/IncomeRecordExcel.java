package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class IncomeRecordExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "用户账号", width = 20)
    private String account;

    @Excel(name = "当日算力(TB)", width = 20)
    private BigDecimal userPower;

    @Excel(name = "挖矿收益", width = 20)
    private BigDecimal userMiningAmount;

    @Excel(name = "25%直接释放", width = 20)
    private BigDecimal firstAmount;

    @Excel(name = "挖矿币种", width = 20)
    private String symbol;

    @Excel(name = "当前矿池总份额", width = 20)
    private BigDecimal systemPower;
    public void setUserPower(BigDecimal userPower) {
        this.userPower = userPower.stripTrailingZeros();
    }

    public void setUserMiningAmount(BigDecimal userMiningAmount) {
        this.userMiningAmount = userMiningAmount.stripTrailingZeros();
    }

    public void setFirstAmount(BigDecimal firstAmount) {
        this.firstAmount = firstAmount.stripTrailingZeros();
    }

    public void setSystemPower(BigDecimal systemPower) {
        this.systemPower = systemPower.stripTrailingZeros();
    }

    public void setTotalMiningAmount(BigDecimal totalMiningAmount) {
        this.totalMiningAmount = totalMiningAmount.stripTrailingZeros();
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee.stripTrailingZeros();
    }

    @Excel(name = "矿池总收益", width = 20)
    private BigDecimal totalMiningAmount;

    @Excel(name = "技术服务费", width = 20)
    private BigDecimal serviceFee;

    @Excel(name = "发放日期", width = 20,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
