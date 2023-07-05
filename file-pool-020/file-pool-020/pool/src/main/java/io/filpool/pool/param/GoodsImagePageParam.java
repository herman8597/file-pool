package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 商品封面图 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-04-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品封面图分页参数")
public class GoodsImagePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
