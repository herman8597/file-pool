package network.vena.cooperation.adminApi.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: user_id_info
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Data
@TableName("user_id_info")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserIdInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditTime;
	/**提交时间*/
	@Excel(name = "提交时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**用户ID*/
    private String apiKey;
	@TableField(exist = false)
	@Excel(name = "账号", width = 15)
	private String account;
	/**用户证件号码*/
	@Excel(name = "用户证件号码", width = 15)
    private String idCardNo;
	/**证件类型 1身份证*/
	@Excel(name = "证件类型 1身份证", width = 15)
    private String idCardType;
	/**状态:0待审核,1审核通过,2审核拒绝*/
	@Excel(name = "状态:0待审核,1审核通过,2审核拒绝", width = 15)
    private Integer auditStatus;
	/**身份证正面*/
	@Excel(name = "身份证正面", width = 15)
    private String idFront;
	/**身份证反面*/
	@Excel(name = "身份证反面", width = 15)
    private String idVerso;
	/**审核备注*/
	@Excel(name = "审核备注", width = 15)
    private String auditRemark;
	/**用户真实姓名*/
	@Excel(name = "用户真实姓名", width = 15)
    private String realName;
	/**审核员ID*/
    private String staffId;
	@Excel(name = "审核员ID", width = 15)
	@TableField(exist = false)
    private String staff;
	/**国家*/
	@Excel(name = "国家", width = 15)
    private String country;
}
