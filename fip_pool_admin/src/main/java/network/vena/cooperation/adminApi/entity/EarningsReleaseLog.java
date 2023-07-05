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

/**
 * @Description: earnings_release_log
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
@Data
@TableName("earnings_release_log")
public class EarningsReleaseLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**金额*/
	@Excel(name = "金额", width = 15)
    private BigDecimal amount;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**币种*/
	@Excel(name = "币种", width = 15)
    private String asset;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**矿机收益id*/
	@Excel(name = "矿机收益id", width = 15)
    private Integer gainRecordId;
	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**类型：1首次发放 2逐次发放 3人工发放*/
	@Excel(name = "类型：1首次发放 2逐次发放 3人工发放", width = 15)
    private Integer releaseType;
	/**剩余数量*/
	@Excel(name = "剩余数量", width = 15)
    private BigDecimal surplusAmount;
	/**发放周期*/
	@Excel(name = "发放周期", width = 15)
    private Integer term;
	/**类型：1挖矿,2太空币奖励*/
	@Excel(name = "类型：1挖矿,2太空币奖励", width = 15)
    private Integer type;
}
