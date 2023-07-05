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
 * 平台信息表
 *
 * @author filpool
 * @since 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fil_about")
@ApiModel(value = "About对象")
public class About extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;

    @NotNull(message = "创建时间不能为空")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @NotNull(message = "更新时间不能为空")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @NotNull(message = "类型:1 关于我们  2用户协议不能为空")
    @ApiModelProperty("类型:1 关于我们  2用户协议")
    private Boolean type;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @NotBlank(message = "语言类型： zh-CN 中文，zh-TW 繁体，en-US 英文不能为空")
    @ApiModelProperty("语言类型： zh-CN 中文，zh-TW 繁体，en-US 英文")
    private String language;

}
