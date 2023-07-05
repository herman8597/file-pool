package network.vena.cooperation.adminApi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InviteTeamVo {
    private Integer inviteCount = 0;
    private BigDecimal totalHashrate = BigDecimal.ZERO;
}
