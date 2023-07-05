package network.vena.cooperation.base.vo;


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
public class OrderVo {
    private String parentAccount; //上级邀请人
    private String id; //序号
    private String relatedName; //产品信息
    private BigDecimal paymentQuantity; //销售份数
    private BigDecimal quantity; //剩余份数
    private Integer status; //状态
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private String account; //用户账号
    private String nickname; //用户名称
    private String asset; //支付货币
    private BigDecimal price; //单价
    private String link; //联系方式




}
