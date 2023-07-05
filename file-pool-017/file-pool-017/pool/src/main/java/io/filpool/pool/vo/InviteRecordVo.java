package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("邀请记录返回")
@Data
public class InviteRecordVo {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户id")
    @Excel(name = "用户id")
    private Long userId;

    @ApiModelProperty("用户账号")
    @Excel(name = "用户账号")
    private String userAccount;

    @ApiModelProperty("邀请人id")
    @Excel(name = "邀请人id")
    private Long inviteUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("邀请日期")
    @Excel(name = "邀请日期",databaseFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "类型： 1直退 2间推")
    @ApiModelProperty("类型： 1直退 2间推")
    private Integer type;
}
