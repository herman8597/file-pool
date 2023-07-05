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
 * @Description: collect_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("collect_pipeline")
public class CollectPipeline implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**from_address*/
	@Excel(name = "from_address", width = 15)
    private String from;
	/**to_address*/
	@Excel(name = "to_address", width = 15)
    private String to;
	/**归集金额*/
	@Excel(name = "归集金额", width = 15)
    private BigDecimal amount;
	/**createTime*/
	@Excel(name = "createTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**币种*/
	@Excel(name = "币种", width = 15)
    private String asset;
	/**交易hash*/
	@Excel(name = "交易hash", width = 15)
    private String hash;
	/**1=提交上链，2=交易确认*/
	@Excel(name = "1=提交上链，2=交易确认", width = 15)
    private Integer status;
}
