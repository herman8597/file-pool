package io.filpool.pool.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

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
 * 商品表
 *
 * @author filpool
 * @since 2021-03-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_goods")
@ApiModel(value = "Goods对象")
public class Goods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("状态: 0未开始 1进行中 2已售罄 3已下架")
    private Integer status;

    @NotNull(message = "原价不能为空")
    @ApiModelProperty("原价")
    private BigDecimal price;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("挖矿币种")
    private Long currencyId;

    @ApiModelProperty("挖矿币种单位")
    private String symbol;

    @ApiModelProperty("币种名称")
    @TableField(exist = false)
    private String asset;

    @NotNull(message = "商品类型: 1矿机 2云算力 3矿机集群不能为空")
    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群")
    private Integer type;

    @ApiModelProperty("预计日产出")
    private BigDecimal dayOut;

    @ApiModelProperty("功耗kw/h")
    private BigDecimal powerConsumption;

    @ApiModelProperty("服务费")
    private BigDecimal servingFee;

    @ApiModelProperty("合约期限(天)")
    private Integer contractPeriod;

    @ApiModelProperty("可售数量")
    private Integer quantity;

    @ApiModelProperty("单次最小购买数量")
    private Integer minLimit;

    @ApiModelProperty("单次最大购买数量")
    private Integer maxLimit;

    @ApiModelProperty("矿机集群下属id:多个英文逗号分隔")
    private String groupIds;

    @ApiModelProperty("已售数量")
    private Integer soldQuantity;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("商品详情描述")
    private String detail;

    @ApiModelProperty("商品标签")
    private String tag;

    @ApiModelProperty("算力(TB)")
    private BigDecimal power;

    @ApiModelProperty("商品封面图片:多张英文逗号分隔")
    private String images;

    @ApiModelProperty("权重(数字越大商品越靠前)")
    private Integer weight;

    @ApiModelProperty("是否显示在首页（0：不显示，1显示）")
    private Integer isShowPage;

    @ApiModelProperty("结算周期")
    private String billingCycle;

    @ApiModelProperty("每T需要GAS费")
    private BigDecimal tbGas;

    @ApiModelProperty("每T质押(FIL)")
    private BigDecimal tbPledge;

    @ApiModelProperty("022新需求，商品是否专题显示（0：不显示，1显示）")
    private Integer specialShow;
}
