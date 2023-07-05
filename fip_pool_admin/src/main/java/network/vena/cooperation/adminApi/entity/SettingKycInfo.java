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
 * @Description: setting_kyc_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("setting_kyc_info")
public class SettingKycInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**apiKey*/
	@Excel(name = "apiKey", width = 15)
    private String apiKey;
	/**kyc认证等级：0=未完善1，
1=手机号邮箱认证及资金密码设置{"moble":"","email":"","pay_pwd":0}，
2=实名认证{"name":"","id_number":""}，
3=添加银行卡{"bank_card":""}，
4=上传身份证照片{"positive_img":"","reverse_img":""}*/
	@Excel(name = "kyc认证等级", width = 15)
    private java.lang.Integer kycLevel;
	/**info*/
	@Excel(name = "info", width = 15)
    private java.lang.String info;
	/**该等级是否通过：0=未通过，1=通过,-1=待审核*/
	@Excel(name = "该等级是否通过：0=未通过，1=通过,-1=待审核", width = 15)
    private java.lang.Integer status;
	/**0=未删除，1=删除*/
	@Excel(name = "0=未删除，1=删除", width = 15)
    private java.lang.Integer deleted;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**updateTime*/
	@Excel(name = "updateTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**kyc类型：phone=手机，email=邮箱，pay_pwd=资金密码，authname=实名认证，bank=银行卡，identity=身份证照*/
	@Excel(name = "kyc类型：phone=手机，email=邮箱，pay_pwd=资金密码，authname=实名认证，bank=银行卡，identity=身份证照", width = 15)
    private String type;
	/**approveTime*/
	@Excel(name = "approveTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
	/**remark*/
	@Excel(name = "remark", width = 15)
    private String remark;
	/**idNumber*/
	@Excel(name = "idNumber", width = 15)
    private String idNumber;
}
