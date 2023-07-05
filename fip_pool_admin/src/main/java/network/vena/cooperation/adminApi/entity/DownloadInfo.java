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
 * @Description: download_info
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("download_info")
public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**channel*/
	@Excel(name = "渠道", width = 15)
    private String channel;
	/**name*/
	@Excel(name = "名称", width = 15)
    private String name;
	/**image*/
	@Excel(name = "image", width = 15)
    private String image;
	/**url*/
	@Excel(name = "链接", width = 15)
    private String url;
	/**clickCount*/
	@Excel(name = "点击次数", width = 15)
    private Integer clickCount;
	/**downloadCount*/
	@Excel(name = "下载次数", width = 15)
    private Integer downloadCount;
	/**registeCount*/
	@Excel(name = "注册次数", width = 15)
    private Integer registeCount;
	/**updateTime*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
