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
 * @Description: gc_deposit
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("gc_deposit")
public class GcDeposit implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**币种*/
	@Excel(name = "币种", width = 15)
    private String asset;
	/**充值地址*/
	@Excel(name = "充值地址", width = 15)
    private String address;
	/**充值金额*/
	@Excel(name = "充值金额", width = 15)
    private BigDecimal amount;
	/**CANCELLED=取消，finish=完成，pending=正在处理*/
	@Excel(name = "充币状态", width = 15,dicCode = "gc_deposit_status")
    private String status;
	/**createTime*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**充值hash*/
	@Excel(name = "充值hash", width = 15)
    private String hash;
	/**通过高度*/
	@Excel(name = "通过高度", width = 15)
    private Integer accessHeight;
	/**当前高度*/
	@Excel(name = "当前高度", width = 15)
    private Integer currentHeight;
	/**创建高度*/
	@Excel(name = "创建高度", width = 15)
    private Integer createHeight;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    private String pid;
	/**fromAddress*/
	@Excel(name = "转出地址", width = 15)
    private String fromAddress;
	/**updateTime*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**用户*/
	/*@Excel(name = "用户", width = 15)*/
    private String apiKey;
	/**blockChain*/
	@Excel(name = "主链", width = 15)
    private String blockChain;
	/**type*/
	/*@Excel(name = "type", width = 15)*/
    private Integer type;

	@Excel(name = "账号", width = 15)
	@TableField(exist = false)
	private String account;
}
