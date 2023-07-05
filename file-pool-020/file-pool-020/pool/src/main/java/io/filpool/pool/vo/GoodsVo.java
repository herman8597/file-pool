package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.filpool.framework.common.entity.BaseEntity;
import io.filpool.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 *
 * @author filpool
 * @since 2021-03-08
 */
@Data
@ApiModel("Goods列表返回")
public class GoodsVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("状态: 0未开始 1进行中 2已售罄 3已下架")
    private Integer status;

    @ApiModelProperty("原价")
    private BigDecimal price;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("挖矿币种")
    private Long currencyId;

    @ApiModelProperty("挖矿币种单位")
    private String symbol;

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

    @ApiModelProperty("结算周期")
    private String billingCycle;

    @ApiModelProperty("可售数量")
    private Integer quantity;

    @ApiModelProperty("单次最小购买数量")
    private Integer minLimit;

    @ApiModelProperty("单次最大购买数量")
    private Integer maxLimit;

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

    @ApiModelProperty("商品封面图片:多张英文逗号分隔")
    private String images;

    @ApiModelProperty("矿机集群下矿机数量")
    private Integer goodSize;

    @ApiModelProperty("是否展示在首页")
    private Integer isShowPage;

    @ApiModelProperty("每T质押(FIL)")
    private BigDecimal tbPledge;

    @ApiModelProperty("每T需要质押")
    private BigDecimal tbGas;

    @ApiModelProperty("预计年化收益")
    private BigDecimal yearIncomeRate;

    @ApiModelProperty("回本周期")
    private Integer returnDay;
}
