package io.filpool.pool.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import io.filpool.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.filpool.framework.core.validator.groups.Update;

/**
 * 020社区奖励
 *
 * @author filpool
 * @since 2021-05-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ReturnedRewardConfig对象")
public class ReturnedRewardConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品类型：1矿机 2云算力")
    private Integer goodsType;

    @ApiModelProperty("用户等级")
    private Integer level;

    @ApiModelProperty("USDT极差奖励")
    private BigDecimal usdtRangeReward;

    @ApiModelProperty("USDT平级奖励")
    private BigDecimal usdtLevelReward;

    @ApiModelProperty("算力极差奖励")
    private BigDecimal powerRangeReward;

    @ApiModelProperty("算力平级奖励")
    private BigDecimal powerLevelReward;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("各个等级升级经验要求（升级条件1）")
    private Integer experience;

    @ApiModelProperty("需要p1等级的儿子个数（升级条件2）")
    private String sonCount;

}
