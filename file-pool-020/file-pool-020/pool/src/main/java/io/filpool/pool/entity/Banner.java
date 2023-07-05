package io.filpool.pool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * banner
 *
 * @author filpool
 * @since 2021-03-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_banner")
@ApiModel(value = "Banner对象")
public class Banner extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("外部链接")
    private String linkUrl;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @NotBlank(message = "语言类型： zh-CN 中文，zh-TW 繁体，en-US 英文不能为空")
    @ApiModelProperty("语言类型： zh-CN 中文，zh-TW 繁体，en-US 英文")
    private String language;

    @ApiModelProperty("排序越大越靠前")
    private Integer rank;

    @ApiModelProperty("内容")
    private String content;

}
