package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 
 *
 * @author filpool
 * @since 2021-06-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BzzConfig对象")
public class BzzConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("今日发出BZZ数")
    @TableField("Issued_today")
    private String issuedToday;

    @ApiModelProperty("今日兑换支票次数")
    private String exchangeToday;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
