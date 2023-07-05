package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 020社区奖励 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-05-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "020社区奖励分页参数")
public class ReturnedRewardConfigPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
