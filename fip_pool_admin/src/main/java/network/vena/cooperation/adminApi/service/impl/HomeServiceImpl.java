package network.vena.cooperation.adminApi.service.impl;

import network.vena.cooperation.adminApi.entity.Weight;
import network.vena.cooperation.adminApi.mapper.WeightMapper;
import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.adminApi.service.IWeightService;
import network.vena.cooperation.base.vo.HomeVo;
import network.vena.cooperation.base.vo.SalesVo;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.DateUtils;
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: captain
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class HomeServiceImpl {

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private IWeightService weightService;

    @Autowired
    private WeightMapper weightMapper;

    public HomeVo homeVo(QueryParam queryParam) {

        Long totalOrders = weightMapper.countTotal();
        HomeVo homeVo = new HomeVo();

        Date date = DateUtils.lastDateOfPrevMonth(DateUtils.getMaxTime(new Date()));
        List<Weight> currentMonthOrders = weightService.lambdaQuery()
                .eq(Weight::getStatus, 1).in(Weight::getType, 1, 7).gt(Weight::getCreateTime, date).isNotNull(Weight::getAsset).list();

        List<Weight> ordersToday = currentMonthOrders.stream()
                .filter(e -> DateUtils.getDayOfDate(e.getCreateTime()) == DateUtils.getDayOfDate(new Date())).collect(Collectors.toList());

        //004后台首页的今日收益要把补单算进去，由于补单的Weight::getAsset是没有数据的，在此将其删除
        List<Weight> currentMonthOrdersTwo = weightService.lambdaQuery()
                .eq(Weight::getStatus, 1).in(Weight::getType, 1, 7).gt(Weight::getCreateTime, date).list();

        List<Weight> ordersTodayTwo = currentMonthOrdersTwo.stream()
                .filter(e -> DateUtils.getDayOfDate(e.getCreateTime()) == DateUtils.getDayOfDate(new Date())).collect(Collectors.toList());

        //今日销售量
        BigDecimal salesVolumeToday = ordersTodayTwo.stream().map(e -> {
            if (ObjectUtils.isEmpty(e.getQuantity())) {
                return BigDecimal.ZERO;
            } else {
                if ("GB".equals(e.getUnit())) {
                    return BigDecimalUtil.divide(e.getQuantity(), 1000);
                }
                return e.getQuantity();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        ArrayList<SalesVo> salesToday = new ArrayList<>();

        Map<String, List<Weight>> asset$MapToday = ordersToday.stream().collect(Collectors.groupingBy(Weight::getAsset));
        asset$MapToday.forEach((k, v) -> {
            if (!ObjectUtils.isEmpty(k) && !"UNKNOWN".equals(k) && !"UPDATE".equals(k)) {
                BigDecimal reduce = v.stream().map(e -> BigDecimalUtil.add(e.getPaymentQuantity(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
                salesToday.add(new SalesVo(k, BigDecimalUtil.format(reduce, 3)));
            }
        });

        /*BigDecimal totalSalesToday = ordersToday.stream().map(e -> {
            if (ObjectUtils.isEmpty(e.getPaymentQuantity())) {
                return BigDecimal.ZERO;
            } else {
                return e.getPaymentQuantity();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);*/


        Date beforeDate = DateUtils.stringToDate(DateUtils.getBeforeDateTime(1, DateUtils.DATE_TIME_FORMAT));
        List<Weight> orderYesterday = currentMonthOrders.stream().filter(e -> DateUtils.getDayOfDate(e.getCreateTime()) == DateUtils.getDayOfDate(beforeDate)).collect(Collectors.toList());
        //昨日销售
        BigDecimal salesVolumeYesterday = orderYesterday.stream().map(e -> {
            if (ObjectUtils.isEmpty(e.getQuantity())) {
                return BigDecimal.ZERO;
            } else {
                if ("GB".equals(e.getUnit())) {
                    return BigDecimalUtil.divide(e.getQuantity(), 1000);
                }
                return e.getQuantity();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

          //重新計算昨日銷售額
         salesVolumeYesterday = weightMapper.sumSalesVolumeYesterday();

        Map<String, List<Weight>> asset$MapYesterday = orderYesterday.stream().collect(Collectors.groupingBy(Weight::getAsset));

        ArrayList<SalesVo> salesYesterday = new ArrayList<>();

        asset$MapYesterday.forEach((k, v) -> {
            if (!ObjectUtils.isEmpty(k) && !"UNKNOWN".equals(k)&&!"UPDATE".equals(k)) {
                BigDecimal reduce = v.stream().map(e -> BigDecimalUtil.add(e.getPaymentQuantity(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
                salesYesterday.add(new SalesVo(k, BigDecimalUtil.format(reduce, 3)));
            }
        });

        /*BigDecimal totalSalesYesterday = orderYesterday.stream().map(e -> {
            if (ObjectUtils.isEmpty(e.getPaymentQuantity())) {
                return BigDecimal.ZERO;
            } else {
                return e.getPaymentQuantity();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);*/

        //总销售算力(TB)
        BigDecimal totalSalesVolume = weightMapper.sumQuantity();


        //BigDecimal totalSales = weightMapper.sumPaymentQuantity();

        ArrayList<SalesVo> salestotal = new ArrayList<>();

        List<Weight> list = weightService.lambdaQuery().select(Weight::getAsset, Weight::getPaymentQuantity).eq(Weight::getStatus, 1).in(Weight::getType, 1, 7).isNotNull(Weight::getAsset).list();
        Map<String, List<Weight>> asset$MapTotal = list.stream().collect(Collectors.groupingBy(Weight::getAsset));
        asset$MapTotal.forEach((k, v) -> {
            if (!ObjectUtils.isEmpty(k) && !"UNKNOWN".equals(k) && !"UPDATE".equals(k)) {
                BigDecimal reduce = v.stream().map(e -> BigDecimalUtil.add(e.getPaymentQuantity(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
                salestotal.add(new SalesVo(k, BigDecimalUtil.format(reduce, 3)));
            }
        });

        Integer newOrdersToday = ordersToday.size();
        Integer newOrdersYesterday = currentMonthOrders.stream()
                .filter(e -> DateUtils.getDayOfDate(e.getCreateTime()) == DateUtils.getDayOfDate(DateUtils.stringToDate(DateUtils.getBeforeDateTime(1))))
                .collect(Collectors.toList()).size();


        homeVo.setOrdersToday(ordersToday.size() + "");
        homeVo.setNewOrdersCurrentMonth(currentMonthOrders.size() + "");
        homeVo.setSalesVolumeToday(BigDecimalUtil.format(salesVolumeToday, 2));
        homeVo.setTotalSalesVolume(BigDecimalUtil.format(totalSalesVolume, 2));
        homeVo.setSalesToday(salesToday);
        // homeVo.setTotalSales(BigDecimalUtil.format(totalSales, 2));
        homeVo.setTotalSales(salestotal);
        homeVo.setOrdersYesterday(orderYesterday.size() + "");
        homeVo.setSalesVolumeYesterday(BigDecimalUtil.format(salesVolumeYesterday, 3));
        //homeVo.setTotalSalesYesterday(BigDecimalUtil.format(totalSalesYesterday, 3));
        homeVo.setSalesYesterday(salesYesterday);
        homeVo.setNewOrdersToday(newOrdersToday.toString());
        homeVo.setNewOrdersYesterday(newOrdersYesterday.toString());
        homeVo.setTotalOrders(totalOrders.toString());
        homeVo.setOrderVos(goodsService.list());

        return homeVo;
    }
}
