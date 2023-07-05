package network.vena.cooperation.adminApi.dto;

import io.swagger.annotations.ApiOperation;
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
public class BalanceDTO {

    private String apiKey;

    private Integer type;
//货币
    private String asset;
//金额
    private BigDecimal available;
    private BigDecimal frozen;

    private Date createTime;

    private String remark;

}
