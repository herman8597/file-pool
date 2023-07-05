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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import javax.persistence.Column;

/**
 * @Description: pledge_log
 * @Author: jeecg-boot
 * @Date:   2020-11-30
 * @Version: V1.0
 */
@Data
@TableName("pledge_log")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PledgeLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**1 订单赠送 2 太空竞赛2 3挖矿质押*/
	@Excel(name = "类型", width = 15,dicCode = "pledge_types")
    private Integer pledgeType;
	/**数量*/
	@Excel(name = "数量", width = 15)
    private BigDecimal amount;
	@Excel(name = "货币", width = 15)
	private String asset;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**apikey*/
    private String apiKey;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    private String pid;
	/**货币*/

	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remark;

	@Excel(name = "算力", width = 15)
	private BigDecimal hashrate;
}
