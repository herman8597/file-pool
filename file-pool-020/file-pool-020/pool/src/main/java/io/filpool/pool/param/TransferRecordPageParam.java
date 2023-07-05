package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.math.BigDecimal;

/**
 * <pre>
 * 用户划转记录 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户质押分页参数")
public class TransferRecordPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("金额数量")
    private BigDecimal amount;

    @ApiModelProperty("用户id")
    private Long userId;

}
