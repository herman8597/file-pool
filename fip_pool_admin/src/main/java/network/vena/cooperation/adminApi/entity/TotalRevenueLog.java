package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: total_revenue_log
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
@Data
@TableName("total_revenue_log")
public class TotalRevenueLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**iD*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**发放日期*/
	@Excel(name = "发放日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date grantDate;
	/**矿池总量*/
	@Excel(name = "矿池总量", width = 15)
    private BigDecimal amountTotal;
	/**矿池总权重*/
	@Excel(name = "矿池总权重", width = 15)
    private BigDecimal weightTotal;
	/**参与人数数量*/
	@Excel(name = "参与人数数量", width = 15)
    private Integer count;
	/**是否已挖矿:0未1已*/
	@Excel(name = "是否已挖矿:0未1已", width = 15)
    private Integer isGrant;
	/**挖矿币种*/
	@Excel(name = "挖矿币种", width = 15)
    private String asset;


	@Excel(name = "备注", width = 15)
    private String remark;


	@Excel(name = "挖矿订单开始时间", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "mining_time_begin")
	private Date miningTimeBegin;

	@Excel(name = "挖矿订单结束时间", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "mining_time_end")
	private Date miningTimeEnd;



}
