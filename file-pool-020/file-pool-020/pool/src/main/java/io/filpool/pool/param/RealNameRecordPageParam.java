package io.filpool.pool.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

/**
 * <pre>
 * 实名认证信息 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "实名认证信息分页参数")
public class RealNameRecordPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String account;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("提交时间")
    private Date createTime;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

    @ApiModelProperty("审核状态：1待审核 2审核失败 3审核通过")
    private Integer authStatus;


}
