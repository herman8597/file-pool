package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
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
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: distribution_detail
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("distribution_detail")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DistributionDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * apiKey
     */
    @Excel(name = "apiKey", width = 15)
    private String apiKey;
    /**
     * 0-铜牌，1-银牌，2-金牌，3-铂金，4-钻石
     */
    @Excel(name = "0-铜牌，1-银牌，2-金牌，3-铂金，4-钻石", width = 15)
    private Integer level = -1;
    @Excel(name = "市场等级", width = 15)
    private String marketLevel ;
    @Excel(name = "运营等级", width = 15)
    private String operatingLevel;
    /**
     * 自己购买的TB数
     */
    @Excel(name = "自己购买的TB数", width = 15)
    private BigDecimal selfPurchase;
    /**
     * 下级的购买TB数
     */
    @Excel(name = "下级的购买TB数", width = 15)
    private BigDecimal childrenPurchase ;
    /**
     * 下下级购买的TB数
     */
    @Excel(name = "下下级购买的TB数", width = 15)
    private BigDecimal grandchildrenPurchase;
    /**
     * updateTime
     */
    @Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public BigDecimal getSelfPurchase() {
        if (ObjectUtils.isEmpty(selfPurchase)) {
            return BigDecimal.ZERO;
        }
        return selfPurchase;
    }

    public void setSelfPurchase(BigDecimal selfPurchase) {
        this.selfPurchase = selfPurchase;
        if (BigDecimalUtil.less(this.selfPurchase, BigDecimal.ZERO)) {
            throw new RuntimeException("自身算力不能为负数");
        }
    }

    public BigDecimal getChildrenPurchase() {
        if (ObjectUtils.isEmpty(childrenPurchase)) {
            return BigDecimal.ZERO;
        }
        return childrenPurchase;
    }

    public void setChildrenPurchase(BigDecimal childrenPurchase) {
        this.childrenPurchase = childrenPurchase;
        if (BigDecimalUtil.less(this.childrenPurchase, BigDecimal.ZERO)) {
            throw new RuntimeException("一级算力不能为负数");
        }
    }

    public BigDecimal getGrandchildrenPurchase() {
        if (ObjectUtils.isEmpty(grandchildrenPurchase)) {
            return BigDecimal.ZERO;
        }
        return grandchildrenPurchase;
    }

    public void setGrandchildrenPurchase(BigDecimal grandchildrenPurchase) {
        this.grandchildrenPurchase = grandchildrenPurchase;
        if (BigDecimalUtil.less(this.childrenPurchase, BigDecimal.ZERO)) {
            throw new RuntimeException("二级算力不能为负数");
        }
    }
}
