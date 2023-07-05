package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.filpool.framework.common.entity.BaseEntity;
import io.filpool.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 提币地址配置
 *
 * @author filpool
 * @since 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_withdraw_config")
@ApiModel(value = "WithdrawConfig对象")
public class WithdrawConfigVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("类型，1提币地址  2gas地址")
    private Integer type;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("私钥")
    private String privateKey;

    @ApiModelProperty("提币地址系列id")
    private Long seriesId;

    @ApiModelProperty("币种系列名称")
    private String series;
}
