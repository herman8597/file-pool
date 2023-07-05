package network.vena.cooperation.adminApi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceModifyPipelineRemarkVo {

    private String id;
    private String apiKey;
    private String round;
    private String createTime;
    private String nickname;
    private String rewardQuantity;
    private String orderQuantity;
}
