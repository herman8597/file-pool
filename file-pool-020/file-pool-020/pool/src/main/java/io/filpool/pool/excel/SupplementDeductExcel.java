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
public class SupplementDeductExcel {

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "补单编号", width = 15)
    private Long orderId;

    @Excel(name = "订单原有算力", width = 15)
    private BigDecimal tbSum;

    @Excel(name = "扣除算力", width = 15)
    private BigDecimal deductSum;

    @Excel(name = "实际算力", width = 15)
    private BigDecimal actualTB;

    @Excel(name = "创建时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
