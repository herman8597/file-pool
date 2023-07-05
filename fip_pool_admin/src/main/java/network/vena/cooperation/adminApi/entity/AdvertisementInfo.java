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
 * @Description: advertisement_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("advertisement_info")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdvertisementInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**title*/
	@Excel(name = "标题", width = 15)
    private String title;
	/**0-图片，1-视频*/
	@Excel(name = "类型", width = 15,dicCode = "advertisement_type")
    private Integer type;
	/**H5内容,如果是视频，内容和pc一样*/
	@Excel(name = "H5内容,如果是视频，内容和pc一样", width = 15)
    private String content;
	/**pc内容*/
	@Excel(name = "pc内容", width = 15)
    private String pcContent;
	/**rank*/
	@Excel(name = "排序", width = 15)
    private Integer rank;
	/**ifUse*/
	@Excel(name = "是否启用", width = 15,dicCode = "yes_or_no")
    private Integer ifUse;
	/**createTime*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**updateTime*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	@Excel(name = "图片")
	private String image;

}
