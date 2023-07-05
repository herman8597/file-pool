package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 币种表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "币种表分页参数")
public class CurrencyPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("是否可以挖矿")
    private Boolean miningStatus;
}
