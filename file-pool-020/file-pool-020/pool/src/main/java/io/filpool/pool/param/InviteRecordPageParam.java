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
 * 邀请关系表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "邀请关系表分页参数")
public class InviteRecordPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("返佣人id")
    private Long returnedPeopleId;

    @ApiModelProperty("推荐关系")
    private Integer relation;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("商品类型:1云算力，2矿机，3集群，4云算力补单，5矿机补单，6集群补单")
    private Integer goodType;

    @ApiModelProperty("奖励类型:1购买返佣，2补单返佣")
    private Integer type;

}
