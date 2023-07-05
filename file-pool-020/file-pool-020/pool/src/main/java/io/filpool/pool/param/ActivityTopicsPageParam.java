package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 商品活动专题 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-06-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品活动专题分页参数")
public class ActivityTopicsPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("活動id")
    private String[] ids;

    @ApiModelProperty("活动名称")
    private String topicName;

    @ApiModelProperty("状态（0未启用 1启用）")
    private Integer status;
}
