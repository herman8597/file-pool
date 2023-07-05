package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 币种配置 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-05-31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "币种配置分页参数")
public class CurrencyConfigPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
