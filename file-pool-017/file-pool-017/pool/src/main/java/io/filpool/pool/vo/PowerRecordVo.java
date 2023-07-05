package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("算力记录返回")
public class PowerRecordVo {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("类型:1补单(云算力) 2补单(矿机) 3购买云算力 4平台充值 5平台扣除 6邀请奖励")
    private Integer type;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("算力数量")
    private BigDecimal powerAmount;
    @ApiModelProperty("GAS费,补单矿机类型")
    private BigDecimal gas;
}
