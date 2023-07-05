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
import network.vena.cooperation.util.BigDecimalUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: earnings_pledge_detail
 * @Author: jeecg-boot
 * @Date:   2020-11-30
 * @Version: V1.0
 */
@Data
@TableName("earnings_pledge_detail")
public class EarningsPledgeDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**api_key*/
	@Excel(name = "api_key", width = 15)
    private String apiKey;
	/**质押货币*/
	@Excel(name = "质押货币", width = 15)
    private String asset;
	/**订单赠送质押*/
	@Excel(name = "订单赠送质押", width = 15)
    private BigDecimal awardPledge;
	/**竞赛质押数量*/
	@Excel(name = "竞赛质押数量", width = 15)
    private BigDecimal competitionPledge;
	/**挖矿质押上限*/
	@Excel(name = "挖矿质押上限", width = 15)
    private BigDecimal miningPledgeLimit;
	/**挖矿质押*/
	@Excel(name = "挖矿质押", width = 15)
    private BigDecimal miningPledge;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;


	@Excel(name = "账号", width = 15)
	@TableField(exist = false)
	private String account;
	@TableField(exist = false)
	private Integer operType; // 1 订单赠送质押  2 竞赛质押数量 3 挖矿质押
	@TableField(exist = false)
	private Boolean flag;  // true 增加  false 减少
	@TableField(exist = false)
	private BigDecimal amount;  //操作数量


	public EarningsPledgeDetail setAwardPledge(BigDecimal awardPledge) {
		this.awardPledge = awardPledge;

		if (BigDecimalUtil.less(this.awardPledge,0)) {
			throw new RuntimeException("质押数量不能小于0");
		}
		return this;
	}

	public EarningsPledgeDetail setCompetitionPledge(BigDecimal competitionPledge) {
		this.competitionPledge = competitionPledge;
		if (BigDecimalUtil.less(this.competitionPledge,0)) {
			throw new RuntimeException("质押数量不能小于0");
		}
		return this;
	}


	public EarningsPledgeDetail setMiningPledge(BigDecimal miningPledge) {
		this.miningPledge = miningPledge;
		if (BigDecimalUtil.less(this.miningPledge,0)) {
			throw new RuntimeException("质押数量不能小于0");
		}

		return this;
	}
}
