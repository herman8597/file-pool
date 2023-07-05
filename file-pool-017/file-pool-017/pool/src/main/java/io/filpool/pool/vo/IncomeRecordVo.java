package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("挖矿收益记录列表返回")
public class IncomeRecordVo {
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("记录类型：1当日挖矿收益 2当日挖矿首次释放收益 3当日挖矿冻结 4当日线性释放总和")
    private Integer type;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
