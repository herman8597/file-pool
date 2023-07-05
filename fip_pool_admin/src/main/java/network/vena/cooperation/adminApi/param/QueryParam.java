package network.vena.cooperation.adminApi.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class QueryParam {
    private Integer pageNo =1;
    private Integer pageSize =10;

    private String account;
    private String InviteAccount;
    private String apiKey;

    private Integer fixRelation;
    private Integer relation;
    private String relatedName;
    private String parentAccount;
    private String id;
    private String status;
    private String createTime;
    private BigDecimal paymentQuantity;

    private Integer type;

}
