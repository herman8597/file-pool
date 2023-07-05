package network.vena.cooperation.base.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InviteDetailVO {
    @Excel(name = "账号", width = 15)
    private String account;
    @Excel(name = "apikey", width = 15)
    private String apiKey;
    @Excel(name = "等级", width = 15, dicCode = "level")
    private Integer level;
    @Excel(name = "关系", width = 15, dicCode = "user_relation")
    private Integer relation;
    @Excel(name = "购买算力", width = 15, exportConvert = true)
    private BigDecimal selfPurchase = BigDecimal.ZERO; //购买fil算力

    @TableField(exist = false)
    private BigDecimal selfPurchaseXch = BigDecimal.ZERO; //购买xch算力


    @Excel(name = "支付金额", width = 15, exportConvert = true)
    private BigDecimal paymentAmount = BigDecimal.ZERO; //支付金额

    @Excel(name = "团队算力", width = 15, exportConvert = true)
    private BigDecimal teamHashrate = BigDecimal.ZERO; //团队算力
    //团队xch
    private BigDecimal teamHashrateXch = BigDecimal.ZERO; //团队算力xch

    @Excel(name = "直接上级佣金", width = 15, exportConvert = true)
    private BigDecimal immediateBrokerage = BigDecimal.ZERO; //直接上级佣金
    @Excel(name = "间接上级佣金", width = 15, exportConvert = true)
    private BigDecimal indirectBrokerage = BigDecimal.ZERO; //间接上级佣金



    public String convertgetImmediateBrokerage() {

        return immediateBrokerage.toPlainString();
    }
    public String convertgetIndirectBrokerage() {

        return indirectBrokerage.toPlainString();
    }
    public String convertgetSelfPurchase() {

        return selfPurchase.toPlainString();
    }

    public String convertgetPaymentAmount() {

        return paymentAmount.toPlainString();
    }

    public String convertgetTeamHashrate() {

        return teamHashrate.toPlainString();
    }

}
