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

/**
 * @Description: app_version
 * @Author: jeecg-boot
 * @Date:   2020-05-08
 * @Version: V1.0
 */
@Data
@TableName("app_version")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AppVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
    private String versionCode;
	/**状态(0:老版本 1:最新版本)*/
	@Excel(name = "状态(0:老版本 1:最新版本)", width = 15)
    private Integer status;
	/**更新简要信息*/
	@Excel(name = "更新简要信息", width = 15)
    private String updateTitle;
	/**更新详情信息*/
	@Excel(name = "更新详情信息", width = 15)
    private String updateDetails;
	/**是否删除(0:未删除 1:已删除)*/
	@Excel(name = "是否删除(0:未删除 1:已删除)", width = 15)
    private Integer isDelete;
	/**0:选择更新 1:强制更新*/
	@Excel(name = "0:选择更新 1:强制更新", width = 15)
    private Integer updateType;
	/**发布人id*/
	@Excel(name = "发布人id", width = 15)
    private String userName;
	/**地址*/
	@Excel(name = "地址ios", width = 15)
    private String appUrlIos;
	@Excel(name = "地址安卓", width = 15)
    private String appUrlAndroid;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**英文标题*/
	@Excel(name = "英文标题", width = 15)
    private String updateTitleEn;
	/**英文详情*/
	@Excel(name = "英文详情", width = 15)
    private String updateDetailsEn;
}
