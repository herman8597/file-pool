package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

/**
 * <pre>
 * 算力补单 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "算力补单分页参数")
public class SupplementPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("补单编号")
    private Long id;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("补单类型 1补单矿机 2补单云算力")
    private Integer type;

    @ApiModelProperty("状态:待审核0，通过1，拒绝2")
    private Integer status;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

}
