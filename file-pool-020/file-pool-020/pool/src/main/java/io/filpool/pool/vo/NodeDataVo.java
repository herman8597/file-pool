package io.filpool.pool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "NodeDataVo对象")
public class NodeDataVo {

    //fil矿池总算力
    @ApiModelProperty("fil矿池总算力")
    private String totalQualityAdjPower;

    //fil 有效算力单T产量
    @ApiModelProperty("有效算力单T产量")
    private String dailyCoinsMined;

    //Xch矿池总算力
    @ApiModelProperty("Xch矿池总算力")
    private String supply;

    //企亚有效算力单T产量（FIL/TiB）
    @ApiModelProperty("企亚有效算力单T产量")
    private String dayIncome;

    //bzz矿池节点总数
    @ApiModelProperty("bzz矿池节点总数")
    private BigDecimal beenodesAll;

    //今日发出bzz数
    @ApiModelProperty("今日发出bzz数")
    private String issuedToday;

    //今日兑换支票次数
    @ApiModelProperty("今日兑换支票次数")
    private String exchangeToday;
}
