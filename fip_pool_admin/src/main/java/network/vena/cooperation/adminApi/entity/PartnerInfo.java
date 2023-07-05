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
 * @Description: partner_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("partner_info")
public class PartnerInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**title*/
	@Excel(name = "标题", width = 15)
    private String title;
	/**link*/
	@Excel(name = "链接", width = 15)
    private String link;
	/**image*/
	@Excel(name = "图片", width = 15)
    private String image;
	/**rank*/
	@Excel(name = "排序", width = 15)
    private Integer rank;
	/**ifUse*/
	@Excel(name = "是否使用", width = 15)
    private Integer ifUse;
	/**updateTime*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
