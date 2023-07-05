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
 * @Description: capital_pool_modify_pipeline
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("capital_pool_modify_pipeline")
public class CapitalPoolModifyPipeline implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
	/**操作者apikey，自动为AUTO*/
	@Excel(name = "操作者apikey，自动为AUTO", width = 15)
    private String apiKey;
	/**币种类型*/
	@Excel(name = "币种类型", width = 15)
    private String asset;
	/**数量*/
	@Excel(name = "数量", width = 15)
    private BigDecimal amount;
	/**类型：0=分红池注入，1=分红*/
	@Excel(name = "类型：0=分红池注入，1=分红", width = 15)
    private Integer type;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remark;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
