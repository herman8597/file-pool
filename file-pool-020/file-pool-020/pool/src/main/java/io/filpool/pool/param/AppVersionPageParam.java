package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

/**
 * <pre>
 * 版本更新表 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "版本更新表分页参数")
public class AppVersionPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("版本号")
    private String versionCode;

    @ApiModelProperty("客户端（1:安卓，2:ios）")
    private Integer versionType;

    @ApiModelProperty("状态")
    private Integer updateType;

}
