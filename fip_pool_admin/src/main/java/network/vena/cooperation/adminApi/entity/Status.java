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
 * @Description: status
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("status")
public class Status implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**BTC,OMNI,ETH,TRON*/
	@Excel(name = "BTC,OMNI,ETH,TRON", width = 15)
    private String blockChain;
	/**节点当前高度*/
	@Excel(name = "节点当前高度", width = 15)
    private Integer height;
	/**depositHeight*/
	@Excel(name = "depositHeight", width = 15)
    private Integer depositHeight;
	/**withdrawHeight*/
	@Excel(name = "withdrawHeight", width = 15)
    private Integer withdrawHeight;
	/**0=充值未扫描，1=真该扫描*/
	@Excel(name = "0=充值未扫描，1=真该扫描", width = 15)
    private Integer depositScanFlag;
	/**0=不启用rpc,1=启用*/
	@Excel(name = "0=不启用rpc,1=启用", width = 15)
    private Integer rpc;
	/**collectMinAmount*/
	@Excel(name = "collectMinAmount", width = 15)
    private BigDecimal collectMinAmount;
	/**0=不遍历高度，1=遍历高度*/
	@Excel(name = "0=不遍历高度，1=遍历高度", width = 15)
    private Integer scanFlag;
	/**addressCount*/
	@Excel(name = "addressCount", width = 15)
    private Integer addressCount;
	/**chainNode*/
	@Excel(name = "chainNode", width = 15)
    private String chainNode;
	/**上链时长，默认20分钟，若超过20分钟该tx_hash还未被确认（status==3）则该笔交易失败，待手动处理*/
	@Excel(name = "上链时长，默认20分钟，若超过20分钟该tx_hash还未被确认（status==3）则该笔交易失败，待手动处理", width = 15)
    private Integer duration;
	/**feeAddress*/
	@Excel(name = "feeAddress", width = 15)
    private String feeAddress;
	/**chainNodeInfo*/
	@Excel(name = "chainNodeInfo", width = 15)
    private String chainNodeInfo;
	/**withdrawalAddress*/
	@Excel(name = "withdrawalAddress", width = 15)
    private String withdrawalAddress;
	/**collectAddress*/
	@Excel(name = "collectAddress", width = 15)
    private String collectAddress;
}
