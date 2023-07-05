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
 * @Description: award_freeze
 * @Author: jeecg-boot
 * @Date:   2020-07-17
 * @Version: V1.0
 */
@Data
@TableName("award_freeze")
public class AwardFreeze implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**api_key*/
	@Excel(name = "api_key", width = 15)
    private String apiKey;
	/**货币*/
	@Excel(name = "货币", width = 15)
    private String asset;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**是否已发放:0未发放,1已发放*/
	@Excel(name = "是否已发放:0未发放,1已发放", width = 15)
    private Integer isGrant;
	/**金额*/
	@Excel(name = "金额", width = 15)
    private BigDecimal quantity;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remark;
	/**"类型(0=冲币，1=提现，2=抢购，3=糖果收益，4=分享奖励，5=个人抢购排名奖励，6=团队人数排名奖励，7=PA奖励，8=手续费奖励)")*/
	@Excel(name = "类型", width = 15)
    private Integer type;
}
