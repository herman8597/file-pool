package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InviteExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "邀请码", width = 15)
    private String inviteCode;

    @Excel(name = "上级邀请人", width = 15)
    private String inviterAccount;

    @Excel(name = "直推人数", width = 15)
    private Integer directPush;

    @Excel(name = "间推人数", width = 15)
    private Integer interposition;

    @Excel(name = "返佣金额奖励", width = 15)
    private BigDecimal fyUsdt;

    @Excel(name = "返佣算力奖励", width = 15)
    private BigDecimal fyTb;

}
