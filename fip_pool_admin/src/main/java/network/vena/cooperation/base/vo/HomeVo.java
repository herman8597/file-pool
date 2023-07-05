package network.vena.cooperation.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import network.vena.cooperation.adminApi.entity.Goods;
import network.vena.cooperation.adminApi.entity.Weight;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HomeVo {
    private String ordersToday; //今日订单数
    private BigDecimal salesVolumeToday; //今日销售算力
    //private BigDecimal totalSalesToday; //今日销量总额
    List<SalesVo> salesToday; //今日销量总额

    private String ordersYesterday; //昨日订单数
    private BigDecimal salesVolumeYesterday; //昨日销售算力
    private BigDecimal totalSalesYesterday; //昨日销量总额
    //private BigDecimal totalRMBYesterday; //昨日销量总额

    List<SalesVo> salesYesterday; //昨日销量总额


    private String newOrdersToday; //今日新订单



    private BigDecimal totalSalesVolume; //总销售算力
    //private BigDecimal totalSales; //销售总额
    List<SalesVo> totalSales; //销售总额
    private String newOrdersYesterday; //昨天新订单
    private String newOrdersCurrentMonth; //本月新订单
    private String totalOrders; //总订单

    private List<Goods> orderVos; //销售总览

    public String getOrdersToday() {
        return ordersToday.toString();
    }

    public String getSalesVolumeToday() {
        return salesVolumeToday.toPlainString();
    }

    public String getTotalSalesVolume() {
        return totalSalesVolume.toPlainString();
    }



/*    public String getTotalSalesToday() {
        return totalSalesToday.toPlainString();
    }*/

    /*public String getTotalSales() {
        return totalSales.toPlainString();
    }*/

    public String getNewOrdersToday() {
        return newOrdersToday.toString();
    }

    public String getNewOrdersYesterday() {
        return newOrdersYesterday.toString();
    }

    public String getNewOrdersCurrentMonth() {
        return newOrdersCurrentMonth.toString();
    }

    public String getTotalOrders() {
        return totalOrders.toString();
    }

}
