package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 释放日志表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "释放日志表分页参数")
public class IncomeReleaseLogPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
