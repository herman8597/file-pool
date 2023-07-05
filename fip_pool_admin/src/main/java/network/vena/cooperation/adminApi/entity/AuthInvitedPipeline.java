package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: auth_invited_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("auth_invited_pipeline")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthInvitedPipeline  implements Serializable{
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**邀请者的apikey*/
	@Excel(name = "邀请者的apikey", width = 15)
    private String inviteApiKey;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**0无效1有效*/
    private Integer effective;
	/**createTime*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

	@Excel(name = "用户账号", width = 15)
	@TableField(exist = false)
	private String account;
	@Excel(name = "邀请码", width = 15)
	@TableField(exist = false)
	private String invitationCode;
	@Excel(name = "邀请者", width = 15)
	@TableField(exist = false)
	private String inviter;
	@Excel(name = "等级", width = 15,dicCode = "level")
	@TableField(exist = false)
	private Integer level;
	@Excel(name = "累计人数", width = 15)
	@TableField(exist = false)
	private Integer inviteCount; //累计人数 -
	@Excel(name = "奖励", width = 15)
	@TableField(exist = false)
	private BigDecimal award; //奖励 -
	@Excel(name = "累计算力", width = 15)
	@TableField(exist = false)
	private BigDecimal hashrateTotal; //fil累计算力
	@Excel(name = "一级分销奖励", width = 15)
	@TableField(exist = false)
	private BigDecimal childrenPurchase; //fil一级分销奖励
	@Excel(name = "二级分销奖励", width = 15)
	@TableField(exist = false)
	private BigDecimal grandchildrenPurchase; //fil二级分销奖励

	@TableField(exist = false)
	private BigDecimal childrenPurchaseXch;//xch一级分销

	@TableField(exist = false)
	private BigDecimal grandchildrenPurchaseXch; //xch二级分销奖励

	@TableField(exist = false)
	private BigDecimal hashrateTotalXch; //xch累计算力

	@TableField(exist = false)
	private Integer relation;

}
