package io.filpool.pool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.filpool.framework.core.pagination.BasePageOrderParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 提币记录 分页参数对象
 * </pre>
 *
 * @author filpool
 * @date 2021-03-10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "提币记录分页参数")
public class WithdrawRecordPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("币种")
    private String symbol;

    @ApiModelProperty("提币地址")
    private String toAddress;

    @ApiModelProperty("审核状态：1待审核 2审核通过 3审核拒绝 4提币中  5提币成功  6提币失败")
    private BigDecimal status;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

}
