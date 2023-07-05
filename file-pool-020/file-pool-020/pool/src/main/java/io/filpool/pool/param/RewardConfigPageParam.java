package io.filpool.pool.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 奖励等级配置 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "奖励等级配置分页参数")
public class RewardConfigPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @Excel(name = "设置时间", width = 20)
    @ApiModelProperty("设置时间")
    private Date createTime;

    @ApiModelProperty("商品类型")
    private Integer goodsType;

    @ApiModelProperty("等级名称")
    private String name;

    @ApiModelProperty("等级2")
    private Integer level;

    @ApiModelProperty("算力直推usdt返佣比例")
    private BigDecimal powerOneUsdtRate;

    @ApiModelProperty("算力间推usdt返佣比例")
    private BigDecimal powerTwoUsdtRate;

    @ApiModelProperty("算力直推算力返佣比例")
    private BigDecimal powerOnePowerRate;

    @ApiModelProperty("算力间推算力返佣比例")
    private BigDecimal powerTwoPowerRate;

}
