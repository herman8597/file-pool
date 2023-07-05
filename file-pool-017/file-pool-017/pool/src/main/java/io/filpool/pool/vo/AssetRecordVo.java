package io.filpool.pool.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("资产记录")
@Data
public class AssetRecordVo implements Serializable {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("对应的记录id")
    private Long recordId;

    @Excel(name = "币种", width = 20)
    @ApiModelProperty("币种")
    private String symbol;

    //    @ApiModelProperty("币种图标")
//    private String img;
    @Excel(name = "交易哈希", width = 50)
    @ApiModelProperty("交易哈希，仅在充提记录里有")
    private String hash;

    @Excel(name = "数量", width = 30)
    @ApiModelProperty("数量")
    private BigDecimal amount;

    @Excel(name = "手续费", width = 30)
    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @Excel(name = "转账地址", width = 30)
    @ApiModelProperty("转账地址，内部转账记录为转账人id")
    private String fromAddress;

    @Excel(name = "收款地址", width = 30)
    @ApiModelProperty("收款地址，内部转账记录为收款人id")
    private String toAddress;

    @Excel(name = "手续费", width = 30, replace = {"充值_1", "提现_2", "平台充值_3", "平台扣除_4", "gas费_5", "购买矿机_6", "购买云算力_7", "购买矿机集群_8"
            , "奖励返佣_9", "当日挖矿首次释放_10", "当日线性释放_11"})
    @ApiModelProperty("记录的类型：1充值 2提现 3平台充值 4平台扣除 5gas费 6购买矿机 7购买云算力 8购买矿机集群 9奖励返佣 10当日挖矿首次释放 11当日线性释放")
    private Integer type;

    @Excel(name = "云钱包id", width = 30)
    @ApiModelProperty("云钱包id")
    private Long assetAccountId;

    @Excel(name = "状态", width = 30, replace = {"完成_1", "失败_2", "审核中_3", "确认中_4"})
    @ApiModelProperty("状态 1完成 2失败 3审核中 4确认中 ")
    private Integer status;

    @Excel(name = "创建时间", databaseFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("币种系列,充提手续费使用")
    private String series;
}
