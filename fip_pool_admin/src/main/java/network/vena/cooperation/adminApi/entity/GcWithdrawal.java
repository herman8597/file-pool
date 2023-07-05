package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: gc_withdrawal
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("gc_withdrawal")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GcWithdrawal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
    /**
     * address
     */
    @Excel(name = "转出地址", width = 15)
    private String address;
    /**
     * amount
     */
    @Excel(name = "数量", width = 15)
    private BigDecimal amount;
    /**
     * fee
     */
    @Excel(name = "手续费", width = 15, exportConvert = true)
    private BigDecimal fee;
    /**
     * pending=提币中，finish=已完成，refuse=拒绝
     */
    @Excel(name = "提币状态", width = 15, dicCode = "gc_withdrawal_status")
    private String status;
    /**
     * createTime
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * apiKey
     */
    @Excel(name = "apiKey", width = 15)
    private String apiKey;
    /**
     * hash
     */
    @Excel(name = "哈希", width = 15)
    private String hash;
    /**
     * 系统审核状态:提交申请(STRATEGY_SOLVING)等待管理员检查(WAITING_MANAGER_CHECK)等待钱包处理(WAITING_WALLET_HANDLING)钱包正在处理(WALLET_HANDLING)可提现(CAN_WITHDRAWAL)管理员拒绝(MANAGER_REFUSED)钱包拒绝(WALLET_REFUSED)完成(FINISHED)
     */
    private String systemStatus;
    /**
     * 检查结果状态：提现数量太大(WITHDRAWAL_NUMBER_BIGGER)注册时间太短(REGISTER_TIME_SHORT)没有充值(NO_DEPOSIT)
     */
    private String checkStatus;
    /**
     * 说明
     */
    private String remark;
    /**
     * finishedTime
     */
    @Excel(name = "完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;
    /**
     * 当前高度
     */
    @Excel(name = "当前高度", width = 15)
    private Integer currentHeight;
    /**
     * 真实提现数量
     */
    @Excel(name = "真实提现数量", width = 15)
    private BigDecimal realAmount;
    /**
     * 最小提现费率
     */
    @Excel(name = "最小提现费率", width = 15, exportConvert = true)
    private BigDecimal minWithdrawalRate;
    /**
     * 通过高度
     */
    @Excel(name = "通过高度", width = 15)
    private Integer accessHeight;
    /**
     * pid
     */
    @Excel(name = "流水号", width = 15)
    private String pid;
    /**
     * asset
     */
    @Excel(name = "币种", width = 15)
    private String asset;
    /**
     * updateTime
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * approveTime
     */
    @Excel(name = "处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    /**
     * blockChain
     */
    @Excel(name = "主链", width = 15)
    private String blockChain;
    /**
     * operator
     */
    @Excel(name = "操作人", width = 15)
    private String operator;
    /**
     * operator
     */
    @Excel(name = "账号", width = 15)
    @TableField(exist = false)
    private String account;
    @Excel(name = "手机号", width = 15)
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private Integer pass;

    public String convertgetMinWithdrawalRate() {

        return minWithdrawalRate.toPlainString();
    }
    public String convertgetFee() {
        return fee.toPlainString();
    }

}
