package network.vena.cooperation.adminApi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: fil_goods
 * @Author: jeecg-boot
 * @Date:   2020-12-31
 * @Version: V1.0
 */
@Data
@TableName("fil_goods")
public class FilGoods implements Serializable {
    private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId(type = IdType.AUTO)
    private Integer id;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
	private String goodsName;
	/**产品图*/
	@Excel(name = "产品图", width = 15)
    private String goodsImg;
	/**CPU参数*/
	@Excel(name = "CPU参数", width = 15)
    private String cpuParameter;
	/**CPU品牌*/
	@Excel(name = "CPU品牌", width = 15)
    private String cpuBrand;
	/**内存参数*/
	@Excel(name = "内存参数", width = 15)
    private String memoryParameter;
	/**内存品牌*/
	@Excel(name = "内存品牌", width = 15)
    private String memoryBrand;
	/**系统盘参数*/
	@Excel(name = "系统盘参数", width = 15)
    private String systemdiskParameter;
	/**系统盘品牌*/
	@Excel(name = "系统盘品牌", width = 15)
    private String systemdiskBrand;
	/**数据盘参数*/
	@Excel(name = "数据盘参数", width = 15)
    private String datadiskParameter;
	/**数据盘品牌*/
	@Excel(name = "数据盘品牌", width = 15)
    private String datadiskBrand;
	/**gpu参数*/
	@Excel(name = "gpu参数", width = 15)
    private String gpuParameter;
	/**gpu品牌*/
	@Excel(name = "gpu品牌", width = 15)
    private String gpuBrand;
	/**网卡参数*/
	@Excel(name = "网卡参数", width = 15)
    private String networkcardParameter;
	/**网卡品牌*/
	@Excel(name = "网卡品牌", width = 15)
    private String networkcardBrand;
	/**电源参数*/
	@Excel(name = "电源参数", width = 15)
    private String powersupplyParameter;
	/**电源品牌*/
	@Excel(name = "电源品牌", width = 15)
    private String powersupplyBrand;
	/**多语言*/
	@Excel(name = "多语言", width = 15)
	private Integer languages;
	/**pc显示*/
	@Excel(name = "pc显示", width = 15)
	private Integer pcShow;
	/**app显示*/
	@Excel(name = "app显示", width = 15)
	private Integer appShow;
	/**排序*/
	@Excel(name = "排序", width = 15)
	private Integer sort;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
}
