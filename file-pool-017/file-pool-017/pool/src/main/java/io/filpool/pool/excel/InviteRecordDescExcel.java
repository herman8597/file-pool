package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InviteRecordDescExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "邀请用户", width = 15)
    private String account;

    @Excel(name = "邀请关系", width = 15)
    private Integer relation;

    @Excel(name = "返佣金额奖励", width = 15)
    private BigDecimal fyUsdt;

    @Excel(name = "返佣算力奖励", width = 15)
    private BigDecimal fyTb;

    @Excel(name = "邀请日期", width = 15)
    private Date createTime;

}
