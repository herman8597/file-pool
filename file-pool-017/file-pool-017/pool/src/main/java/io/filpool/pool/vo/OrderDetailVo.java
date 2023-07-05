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

/**
 * 订单表
 *
 * @author filpool
 * @since 2021-03-08
 */
@Data
@ApiModel(value = "Order详情返回")
public class OrderDetailVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "订单编号不能为空")
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("商品名称")
    private String goodName;

    @ApiModelProperty("原价")
    private BigDecimal price;

    @ApiModelProperty("折扣价")
    private BigDecimal discountPrice;

    @ApiModelProperty("总价")
    private BigDecimal totalAmount;

    @NotNull(message = "数量不能为空")
    @ApiModelProperty("数量")
    private Integer quantity;

    @NotNull(message = "商品id不能为空")
    @ApiModelProperty("商品id")
    private Long goodId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @NotNull(message = "订单状态: 1待付款 2已完成 3已取消不能为空")
    @ApiModelProperty("订单状态: 1待付款 2已完成 3已取消")
    private Integer status;

    @ApiModelProperty("支付币种")
    private Long currencyId;

    @ApiModelProperty("购买用户")
    private Long userId;

    @ApiModelProperty("总算力")
    private BigDecimal totalPower;

    @ApiModelProperty("商品类型: 1矿机 2云算力 3矿机集群")
    private Integer goodType;

    @ApiModelProperty("系统当前时间")
    private Date currentTime;
}
