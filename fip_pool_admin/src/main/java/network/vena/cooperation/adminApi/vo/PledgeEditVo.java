package network.vena.cooperation.adminApi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PledgeEditVo {
    private BigDecimal miningPledgeLimit;
    private Date beginTime;
    private Date endTime;
    private BigDecimal pledgeAmount;

}
