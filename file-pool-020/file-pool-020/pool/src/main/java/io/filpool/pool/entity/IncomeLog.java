package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
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
 * 挖矿收益日志
 *
 * @author filpool
 * @since 2021-03-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_income_log")
@ApiModel(value = "IncomeLog对象")
public class IncomeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("类型:1当日挖矿收益 2当日挖矿首次释放收益 3当日挖矿冻结 4当日线性释放总和")
    private Integer type;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("收益金额")
    private BigDecimal amount;

    @ApiModelProperty("挖矿币种单位")
    private String powerSymbol;
}
