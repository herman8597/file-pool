package network.vena.cooperation.adminApi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BaseParam {
    private Integer id;

    @ApiModelProperty
    private String apiKey;
    @ApiModelProperty
    private Integer type;

    @ApiModelProperty
    private String asset;
    @ApiModelProperty
    private BigDecimal available;
    @ApiModelProperty
    private BigDecimal frozen;

    @ApiModelProperty
    private Date createTime;

    @ApiModelProperty
    private String remark;

    @ApiModelProperty(value = "业务id")
    private String pid;

    @ApiModelProperty(value = "处理状态")
    private Integer pass;


}
