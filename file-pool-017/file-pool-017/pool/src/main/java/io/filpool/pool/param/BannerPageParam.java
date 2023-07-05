package io.filpool.pool.param;

import io.filpool.pool.entity.Supplement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

/**
 * <pre>
 * banner 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "banner分页参数")
public class BannerPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("banner名称")
    private String title;

    @ApiModelProperty("语言")
    private String language;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

}
