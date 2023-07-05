package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import io.filpool.framework.common.entity.BaseEntity;
import io.filpool.pool.vo.GoodsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.filpool.framework.core.validator.groups.Update;

/**
 * 商品活动专题
 *
 * @author filpool
 * @since 2021-06-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_activity_topics")
@ApiModel(value = "ActivityTopics对象")
public class ActivityTopics extends BaseEntity {
    private static final long serialVersionUID = 1L;

//    @NotNull(message = "不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("专题名称")
    private String topicName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("跳转链接")
    private String jumpUrl;

    @ApiModelProperty("状态（0未启用 1启用）")
    private Integer status;

    @ApiModelProperty("活动商品集合")
    private String goodsList;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("创建时间")
    private Date crateTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("商品详情")
    @TableField(exist = false)
    private List<GoodsVo> goodsListTwo;

}
