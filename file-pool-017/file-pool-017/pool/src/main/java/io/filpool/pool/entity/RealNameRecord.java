package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import io.filpool.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.filpool.framework.core.validator.groups.Update;

/**
 * 实名认证信息
 *
 * @author filpool
 * @since 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_real_name_record")
@ApiModel(value = "RealNameRecord对象")
public class RealNameRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("区号")
    private String areaCode;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("身份证号码")
    private String cardNumber;

    @ApiModelProperty("正面照片")
    private String cardFront;

    @ApiModelProperty("反面照片")
    private String cardSide;

    @ApiModelProperty("手持签名照片")
    private String cardHolding;

    @ApiModelProperty("提交时间")
    private Date createTime;

    @ApiModelProperty("审核时间")
    private Date authTime;

    @ApiModelProperty("审核人名字")
    private String authUserName;

    @ApiModelProperty("审核状态：1待审核 2审核失败 3审核通过")
    private Integer authStatus;

    @ApiModelProperty("审核意见")
    private String remark;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("国家地区名称")
    private String areaName;
}
