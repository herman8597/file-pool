package network.vena.cooperation.adminApi.excel;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fengxiyu
 * @Date: 2021/09/10/14:27
 * @Description:
 */

@Data
public class UserExcel {
    @Excel(name = "用户账号", width = 15)
    private String userAccount;

    @Excel(name = "上级邀请人账号",width = 15)
    private String inviteApiAccount;

    @Excel(name = "经验值数量FIL", width = 15)
    private BigDecimal filExperience;

    @Excel(name = "累计分销算力FIL", width = 15)
    private BigDecimal hashrateTotal;

    @Excel(name = "经验值数量XCH", width = 15)
    private BigDecimal xchExperience;

    @Excel(name = "累计分销算力XCH", width = 15)
    private BigDecimal xchHashrateTotal;

    @Excel(name = "伞下业绩(社区)", width = 15)
    private BigDecimal total;

}
