package network.vena.cooperation.adminApi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("weight_pledge_log")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("购买算力质押记录")
public class WeightPledgeLog {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("apikey")
    private String apiKey;

    @ApiModelProperty("资产名称")
    private String asset;

    @ApiModelProperty("购买的算力")
    private BigDecimal quantity;

    @ApiModelProperty("订单ID")
    private String orderId;

    @ApiModelProperty("质押数量")
    private BigDecimal pledgeSize;

    @ApiModelProperty("gas费")
    private BigDecimal gas;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty("用户账户")
    @TableField(exist = false)
    private String account;

}
