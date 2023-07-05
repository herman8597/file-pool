package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("用户资产变动表")
public class AssetAccountExchangeVo {

    //用户
    private String userId;

    //1:加钱，2:减钱
    private Integer type;
    //货币类型
    private String symbol;
    //操作金额
    private BigDecimal operationAmount;

    private BigDecimal frozen;

    private Date createTime;

    private String remark;

}
