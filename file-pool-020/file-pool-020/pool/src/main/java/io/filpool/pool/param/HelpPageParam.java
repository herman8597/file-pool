package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 帮助中心表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "帮助中心表分页参数")
public class HelpPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
