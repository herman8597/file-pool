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

/**
 * @Description: operating_income
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Data
@TableName("operating_income")
public class OperatingIncome implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**apiKey*/

    private String apiKey;
	@Excel(name = "账号", width = 15)
	@TableField(exist = false,select = false )
	private String account;

	/**上级*/

	private String subordinate;
	@Excel(name = "下级账号", width = 15)
	@TableField(exist = false,select = false)
	private String subordinateAccount;

	/**伞下业绩*/
	@Excel(name = "伞下业绩", width = 15,exportConvert = true)
    private BigDecimal total;
	public String convertgetTotal() {
		return total.toPlainString();
	}


	/**关系深度*/
	@Excel(name = "关系深度", width = 15)
    private Integer relation;
	/**购买人*/

    private String originApiKey;
	@Excel(name = "购买人", width = 15)
	@TableField(exist = false,select = false)
	private String originAccount;
	/**等级*/
	@Excel(name = "等级", width = 15)
    private Integer level;
	/**奖励*/
	@Excel(name = "奖励", width = 15,exportConvert = true)
    private BigDecimal reward;
	public String convertgetReward() {
		return reward.toPlainString();
	}
	/**比例*/
	@Excel(name = "比例", width = 15,exportConvert = true)
    private BigDecimal ratio;
	public String convertgetRatio() {
		return ratio.toPlainString();
	}
	/**发放货币*/
	@Excel(name = "发放货币", width = 15)
    private String asset;
	/**createTime*/
	@Excel(name = "下单时间", width = 15, format = "YYYY-MM-DD HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "YYYY-MM-DD HH:mm:ss")
    @DateTimeFormat(pattern="YYYY-MM-DD HH:mm:ss")
    private Date createTime;
	/**0冻结 1发放*/
	@Excel(name = "是否已发放", width = 15,dicCode = "yes_or_no")
    private Integer status;
	/**订单流水*/
	@Excel(name = "订单流水", width = 15)
    private String pid;
	/**发放时间*/
	@Excel(name = "发放时间", width = 15, format = "YYYY-MM-DD HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "YYYY-MM-DD HH:mm:ss")
    @DateTimeFormat(pattern="YYYY-MM-DD HH:mm:ss")
    private Date grantTime;



	@Excel(name = "级差算力等级", width = 15)
	private String levelRank;
	@Excel(name = "级差算力奖励", width = 15)
	private BigDecimal hashrateReward;
	@Excel(name = "级差算力比例", width = 15)
	private BigDecimal rankRatio;

	//算力奖励类型
	private Integer hashrateType;

}
