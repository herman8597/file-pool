package io.filpool.pool.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("用户资产")
public class AssetAccountVo {
    @ApiModelProperty("具体资产列表")
    private List<AccountVo> assets;

    @ApiModel("资产")
    @Data
    public static class AccountVo implements Serializable {
        @ApiModelProperty("总资产")
        private BigDecimal total;
        @ApiModelProperty("可用资产")
        private BigDecimal available;
        @ApiModelProperty("冻结资产")
        private BigDecimal frozen;
        @ApiModelProperty("质押资产")
        private BigDecimal pledge;
        @ApiModelProperty("待释放数量,挖矿冻结")
        private BigDecimal minerFrozen;
        @ApiModelProperty("币种简称")
        private String symbol;
        @ApiModelProperty("币种名称")
        private String name;
        @ApiModelProperty("id")
        private Long id;
        @ApiModelProperty("币种图标")
        private String img;
        @ApiModelProperty("币种id")
        private Long currencyId;
        @ApiModelProperty("更新日期")
        private Date updateTime;

    }
}
