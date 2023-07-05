package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CommunityRewardLogDescExcel {

    @Excel(name = "用户账号", width = 15)
    private String rewardUserAccount;

    @Excel(name = "等级", width = 15)
    private Integer rewardUserLevel;

    @Excel(name = "下级账号", width = 15)
    private String downUserAccount;

    @Excel(name = "等级", width = 15)
    private Integer downUserLevel;

    @Excel(name = "购买账号", width = 15)
    private String buyerUserAccount;

    @Excel(name = "社区usdt极差奖励", width = 15)
    private String usdtRangeReward;

    @Excel(name = "社区usdt平级奖励", width = 15)
    private String usdtLevelLeward;

    @Excel(name = "社区算力极差奖励", width = 15)
    private String powerRangeReward;

    @Excel(name = "社区算力平级奖励", width = 15)
    private String powerLevelReward;

    @Excel(name = "订单流水", width = 15)
    private String orderNumber;

    @Excel(name = "发放时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
