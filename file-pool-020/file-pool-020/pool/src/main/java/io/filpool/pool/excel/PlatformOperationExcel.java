package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PlatformOperationExcel {

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "类型", width = 15,replace = {"平台充值_3","平台扣除_4"})
    private Integer type;

    @Excel(name = "币种", width = 15)
    private String symbol;

    @Excel(name = "数量", width = 15)
    private BigDecimal operationAmount;

    @Excel(name = "操作时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
