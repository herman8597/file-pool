package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 挖矿收益日志 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "挖矿收益日志分页参数")
public class IncomeLogPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
