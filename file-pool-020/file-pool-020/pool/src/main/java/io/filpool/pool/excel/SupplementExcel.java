package io.filpool.pool.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SupplementExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "用户账号", width = 15)
    private String account;

    @Excel(name = "补单编号", width = 15)
    private Long id;

    @Excel(name = "补单类型", width = 15,replace = {"云算力_1","矿机_2","矿机集群_3"})
    private Integer type;

    @ApiModelProperty("算力数量")
    @Excel(name = "算力数量", width = 15)
    private BigDecimal tbSum;

    @Excel(name = "支付方式", width = 15)
    private String assetName;

    @Excel(name = "支付金额", width = 15)
    private BigDecimal amountPrice;

    @Excel(name = "质押金额(FIL)", width = 15)
    private BigDecimal pledgePrice;

    @Excel(name = "gas费数量(FIL)", width = 15)
    private BigDecimal gasPrice;

    @Excel(name = "合约天数", width = 15)
    private Integer contractDays;

    @Excel(name = "完成时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "状态",replace = {"待审核_0","通过_1","拒绝_2"})
    private Integer status;

}
