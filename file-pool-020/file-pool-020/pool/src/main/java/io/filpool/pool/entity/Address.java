package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 地址表
 *
 * @author filpool
 * @since 2021-03-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_address")
@ApiModel(value = "Address对象")
public class Address extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("币种系列")
    private String series;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("币种系列id")
    private Long seriesId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("操作密钥")
    private String secret;
}
