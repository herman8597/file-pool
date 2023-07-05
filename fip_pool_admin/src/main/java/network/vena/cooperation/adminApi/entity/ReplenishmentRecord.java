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
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 补单记录
 * @Author: jeecg-boot
 * @Date: 2020-04-21
 * @Version: V1.0
 */
@Data
@TableName("replenishment_record")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReplenishmentRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 补单号
     */
    @Excel(name = "补单号", width = 15)
    @ApiModelProperty(value = "补单号")
    private String pid;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 充值时间
     */
    @Excel(name = "充值时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "充值时间")
    private Date operTime;
    /**
     * api_key
     */
    @Excel(name = "api_key", width = 15)
    @ApiModelProperty(value = "api_key")
    private String apiKey;
    /**
     * 补单类型
     */
    @Excel(name = "补单类型", width = 15,dicCode = "weight_type")
    @ApiModelProperty(value = "补单类型:0补单,1充值")
    private Integer operType;
    /**
     * 金额
     */
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    /**
     * 单位
     */
    @Excel(name = "金额单位", width = 15)
    @ApiModelProperty(value = "金额单位")
    private String asset;
    /**
     * 算力
     */
    @Excel(name = "算力", width = 15)
    @ApiModelProperty(value = "算力")
    private BigDecimal hashrate;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15 ,dicCode ="replenishment_record_status" )
    @ApiModelProperty(value = "状态:0未审核,1同意,2拒绝")
    private Integer status;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private String images;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;

    @Excel(name = "账号", width = 15)
    @ApiModelProperty(value = "账号")
    @TableField(exist = false)
    private String account;

    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    @TableField(exist = false)
    private BigDecimal price;
    @ApiModelProperty(value = "是否修改修改")
    @TableField(exist = false)
    private Integer isEdit;

    //质押数量和GAS费用是011项目中的字段
    @Excel(name = "质押数量", width = 15)
    @ApiModelProperty(value = "质押数量")
    @TableField(exist = false)
    private BigDecimal pledgeQuantity;

    @TableField(exist = false)
    @Excel(name = "GAS费用", width = 15)
    @ApiModelProperty(value = "GAS费用")
    private BigDecimal gasCost;


    public BigDecimal getPrice() {
        if (!ObjectUtils.isEmpty(this.amount) && !ObjectUtils.isEmpty(this.hashrate)&&!BigDecimalUtil.equal(this.amount,BigDecimal.ZERO)
                &&!BigDecimalUtil.equal(this.hashrate,BigDecimal.ZERO)) {
            return BigDecimalUtil.divide(this.amount, this.hashrate);
        } else {
            return null;
        }
    }
}
