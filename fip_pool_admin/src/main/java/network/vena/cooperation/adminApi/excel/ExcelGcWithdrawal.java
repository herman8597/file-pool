package network.vena.cooperation.adminApi.excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExcelGcWithdrawal {

    /**id*/
    @TableId(type = IdType.AUTO)
    @Excel(name = "编号", width = 15)
    private Integer id;

    @Excel(name = "提现单号", width = 15)
    private String pid;

    @Excel(name = "申请时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @Excel(name = "账号", width = 15)
    private String account;

    @Excel(name = "货币", width = 15)
    private String asset;


    @Excel(name = "数量", width = 15)
    private BigDecimal realAmount;
    @Excel(name = "手续", width = 15)
    private BigDecimal fee;
    @Excel(name = "转出地址", width = 15)
    private String address;
    @Excel(name = "哈希", width = 15)
    private String hash;
    @Excel(name = "状态", width = 15)
    private String status;
}
