package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.filpool.framework.common.entity.BaseEntity;
import io.filpool.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 *
 * @author filpool
 * @since 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_user")
@ApiModel(value = "User对象")
public class UserExcel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "用户等级", width = 15,replace = {"青铜_0","白银_1","黄金_2","铂金_3","钻石_4"})
    private Integer levelId;

    @Excel(name = "用户标识", width = 15,replace = {"普通用户_0","代理商_1"})
    private Integer type;

    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty("邮箱")
    private String email;

    @Excel(name = "邀请码", width = 15)
    private String inviteCode;

    @Excel(name = "上级邀请人", width = 15)
    private String inviterAccount;

    @Excel(name = "算力总额", width = 15)
    private BigDecimal totalPower;

    @Excel(name = "账户是否启用", width = 15)
    private String isEnable;

    @Excel(name = "注册时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
