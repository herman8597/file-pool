package network.vena.cooperation.adminApi.excel;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class ExcelGainRecord {
    @Excel(name = "账号", width = 15)
    private String account;
    @Excel(name = "真实姓名", width = 15)
    private String realName;
    @Excel(name = "算力值", width = 15)
    private BigDecimal hashrate;
    @Excel(name = "金额", width = 15)
    private BigDecimal amount;
    @Excel(name = "已发放数量", width = 15)
    private BigDecimal issuedAmount;


}
