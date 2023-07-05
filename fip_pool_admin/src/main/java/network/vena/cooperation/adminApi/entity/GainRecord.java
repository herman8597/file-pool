package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * @Description: gain_record
 * @Author: jeecg-boot
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Data
@TableName("gain_record")
public class GainRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
    /**
     * apiKey
     */
    private String apiKey;
    @Excel(name = "账号", width = 15)
    @TableField(exist = false)
    private String account;
    /**
     * 金额
     */
    @Excel(name = "金额", width = 15,exportConvert = true)
    private BigDecimal amount;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 货币
     */
    @Excel(name = "货币", width = 15)
    private String asset;
    /**
     * 是否已发放
     */
    @Excel(name = "是否已发放", width = 15, dicCode = "yes_or_no")
    private Integer isGrant;
    /**
     * 算力
     */
    @Excel(name = "算力", width = 15)
    private BigDecimal hashrate;
    /**
     * 1挖矿,2太空币奖励
     */
    @Excel(name = "类型", width = 15, dicCode = "gain_record_type")
    private Integer recordType;


    @Excel(name = "奖励总量", width = 15)
    private BigDecimal rewardTotal;

    @Excel(name = "矿池总算力", width = 15)
    private BigDecimal totalHashrate;

    @Excel(name = "已发放数量", width = 15,exportConvert =true )
    private BigDecimal issuedAmount;

    @Excel(name = "真实姓名", width = 15)
    @TableField(exist = false)
    private String realName;


    public String convertgetIssuedAmount() {
        return issuedAmount.toPlainString();
    }

    public String convertgetAmount() {
        return amount.toPlainString();
    }

}
