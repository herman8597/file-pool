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
 * @Description: gc_withdrawal_address
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("gc_withdrawal_address")
public class GcWithdrawalAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**用户*/
	@Excel(name = "用户", width = 15)
    private String apiKey;
	/**地址*/
	@Excel(name = "地址", width = 15)
    private String address;
	/**币种*/
	@Excel(name = "币种", width = 15)
    private String asset;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    private Integer deleted;
	/**addressPartTwo*/
	@Excel(name = "addressPartTwo", width = 15)
    private String addressPartTwo;
	/**remark*/
	@Excel(name = "remark", width = 15)
    private String remark;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
