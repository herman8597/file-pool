package io.filpool.scheduled.model;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.pool.util.BigDecimalUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilVo {
    private BigDecimal change_percentX;
    private BigDecimal change_percent;
    private BigDecimal priceX;
    private BigDecimal price;
    private BigDecimal usdt_price_cny;
    private BigDecimal cny;
    private BigDecimal volumeX;
    private BigDecimal volume;
    private String order;
    private FilAddressVo filAddress;
    private OverviewVo overview;
    private BzzDesc bzzDesc;
    private XchDesc xchDesc;

    public BigDecimal getCny() {
        return BigDecimalUtil.format(BigDecimalUtil.multiply(getUsdt_price_cny(), getPrice()), 2);
    }


    public BigDecimal getChange_percent() {
        if (ObjectUtils.isEmpty(change_percentX)) {
            return change_percent;
        }
        return change_percentX;
    }

    public BigDecimal getPrice() {
        if (ObjectUtils.isEmpty(priceX)) {
            return price;
        }
        return priceX;
    }

    public BigDecimal getVolume() {
        if (ObjectUtils.isEmpty(volumeX)) {
            return volume;
        }
        return volumeX;
    }
}
