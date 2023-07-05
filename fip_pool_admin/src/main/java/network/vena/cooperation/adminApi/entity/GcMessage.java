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
 * @Description: gc_message
 * @Author: jeecg-boot
 * @Date:   2020-04-19
 * @Version: V1.0
 */
@Data
@TableName("gc_message")
public class GcMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**标题*/
	@Excel(name = "标题", width = 15)
    private String title;
	/**enTitle*/
	@Excel(name = "enTitle", width = 15)
    private String enTitle;
	/**内容*/
	@Excel(name = "内容", width = 15)
    private String content;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

	@Excel(name = "排序", width = 15)
	private Integer contentRank;
	@Excel(name = "类型", width = 15,dicCode = "language_type")
	private Integer contentType;
}
