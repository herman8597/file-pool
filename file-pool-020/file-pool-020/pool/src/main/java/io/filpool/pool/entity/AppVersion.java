package io.filpool.pool.entity;

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
 * 版本更新表
 *
 * @author filpool
 * @since 2021-03-30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AppVersion对象")
public class AppVersion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("app名称")
    private String appName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("版本类型（1:安卓，2:ios）")
    private Integer versionType;

    @NotBlank(message = "版本号不能为空")
    @ApiModelProperty("版本号")
    private String versionCode;

    @ApiModelProperty("更新简要信息")
    private String updateTitle;

    @ApiModelProperty("是否删除(0:未删除 1:已删除)")
    private Integer isDelete;

    @ApiModelProperty("1:强制更新 2:提示更新")
    private Integer updateType;

    @ApiModelProperty("发布人id")
    private String userName;

    @ApiModelProperty("版本地址")
    private String appUrl;

}
