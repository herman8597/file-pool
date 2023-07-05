package network.vena.cooperation.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import network.vena.cooperation.util.BigDecimalUtil;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InviteDetailPageVO {

    private BigDecimal selfPurchase = BigDecimal.ZERO;
    private BigDecimal paymentAmount = BigDecimal.ZERO;
    List<InviteDetailVO> inviteDetailVOS = new ArrayList<>();
    private Long total;
    private Set<Integer> relations = new HashSet<>();

    List<AuthInvitedPipeline> AuthInvitedPipelines = new ArrayList<>();


}
