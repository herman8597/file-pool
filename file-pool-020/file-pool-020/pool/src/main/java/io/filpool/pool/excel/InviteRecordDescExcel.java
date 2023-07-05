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
public class InviteRecordDescExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "邀请用户", width = 15)
    private String account;

    @Excel(name = "邀请关系", width = 15)
    private String relation;

    @Excel(name = "返佣金额奖励", width = 15)
    private String fyUsdt;

    @Excel(name = "返佣算力奖励", width = 15)
    private String fyTbFil;

    @Excel(name = "返佣算力奖励XCH", width = 15)
    private String fyTbXch;

    @Excel(name = "邀请日期", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
