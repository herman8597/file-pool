package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: goods
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("goods")
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称", width = 15)
    private String name;
    /**
     * 商品标签(左上角)
     */
    @Excel(name = "商品标签(左上角)", width = 15)
    private String tag;
    /**
     * 强调部分
     */
    @Excel(name = "强调部分", width = 15)
    private String highlight;
    /**
     * 宣传标语
     */
    @Excel(name = "宣传标语", width = 15)
    private String slogan;
    /**
     * 购买价格，基础单位usdt
     */
    @Excel(name = "购买价格，基础单位usdt", width = 15)
    private BigDecimal price;
    /**
     * 原价
     */
    @Excel(name = "原价", width = 15)
    private BigDecimal originalPrice;
    /**
     * 初始数量
     */
    @Excel(name = "初始数量", width = 15)
    private BigDecimal quantity;
    /**
     * 剩余数量
     */
    @Excel(name = "剩余数量", width = 15)
    private BigDecimal remainingQuantity;
    /**
     * 单次购买最小限额
     */
    @Excel(name = "单次购买最小限额", width = 15)
    private BigDecimal minLimit;
    @Excel(name = "单次购买最大限额", width = 15)
    private BigDecimal maxLimit;
    /**
     * 合约日期
     */
    @Excel(name = "合约日期", width = 15)
    private Integer contractDuration;
    /**
     * 合约详情
     */
    @Excel(name = "合约详情", width = 15)
    private String contractDetails;
    /**
     * 算力类型
     */
    @Excel(name = "算力类型", width = 15)
    private String weightAsset;
    /**
     * 算力生效时间
     */
    @Excel(name = "算力生效时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date weightStartTime;
    /**
     * 技术服务费比例
     */
    @Excel(name = "技术服务费比例", width = 15)
    private BigDecimal serviceChargeRate;
    /**
     * 结算周期
     */
    @Excel(name = "结算周期", width = 15)
    private String settlementPeriod;
    /**
     * 产品特色
     */
    @Excel(name = "产品特色", width = 15)
    private String features;
    /**
     * 商品展示排序权重
     */
    @Excel(name = "商品展示排序权重", width = 15)
    private Integer weight;
    /**
     * createTime
     */
    @Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * updateTime
     */
    @Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 状态：1=进行中，2=未开始，3=已结束，99=未发布,1000=奖励商品/不在前端展示
     */
    @Excel(name = "状态：1=进行中，2=未开始，3=已结束，99=未发布,1000=奖励商品/不在前端展示", width = 15)
    private Integer status;
    /**
     * enName
     */
    @Excel(name = "enName", width = 15)
    private String enName;
    /**
     * enTag
     */
    @Excel(name = "enTag", width = 15)
    private String enTag;
    /**
     * enHighlight
     */
    @Excel(name = "enHighlight", width = 15)
    private String enHighlight;
    /**
     * enSlogan
     */
    @Excel(name = "enSlogan", width = 15)
    private String enSlogan;
    /**
     * unit
     */
    @Excel(name = "unit", width = 15)
    private String unit;
    @TableField(exist = false)
    @Excel(name = "销售数量", width = 15)
    private BigDecimal salesQuantity;

    @ApiModelProperty("商品类型（1：普通商品，2：奇亚算力）")
    private Integer goodsType;

    /**
     * 合约详情
     */
    @Excel(name = "合约详情英文", width = 15)
    private String enContractDetails;

    @Excel(name = "支付类型：1 混合支付，2 USDT支付")
    private Integer payType;

    public BigDecimal getSalesQuantity() {
        if (!ObjectUtils.isEmpty(quantity) || !ObjectUtils.isEmpty(remainingQuantity)) {
            return BigDecimalUtil.sub(quantity, remainingQuantity);
        } else {
            return salesQuantity;
        }

    }
}
