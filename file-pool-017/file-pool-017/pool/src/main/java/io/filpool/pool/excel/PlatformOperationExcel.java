package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PlatformOperationExcel {

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "类型", width = 15)
    private Integer type;

    @Excel(name = "币种", width = 15)
    private String symbol;

    @Excel(name = "数量", width = 15)
    private BigDecimal operationAmount;

    @Excel(name = "操作时间", width = 15)
    private Date createTime;

}
