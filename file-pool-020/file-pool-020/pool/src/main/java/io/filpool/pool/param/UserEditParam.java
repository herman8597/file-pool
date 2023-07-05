package io.filpool.pool.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.filpool.framework.core.pagination.BasePageOrderParam;
import io.filpool.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <pre>
 * 用户表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-02
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户表分页参数")
public class UserEditParam {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("谷歌验证码是否开启：1：开启，0：关闭")
    @TableField("googleAuthIsOpen")
    private Boolean googleAuthIsOpen;

    @ApiModelProperty("谷歌验证码密钥")
    @TableField("googleSecretKey")
    private String googleSecretKey;

    /**
     * 0=禁用提币，1=启用提币
     */
    @ApiModelProperty("0=禁用提币，1=启用提币")
    private Integer isWithdrawalFil;

    /**
     * 0=禁用提币，1=启用提币
     */
    @ApiModelProperty("0=禁用提币，1=启用提币")
    private Integer isWithdrawalUsdt;

}


