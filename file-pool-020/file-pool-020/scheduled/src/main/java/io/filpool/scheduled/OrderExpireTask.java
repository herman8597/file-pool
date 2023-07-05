package io.filpool.scheduled;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.DateUtil;
import io.filpool.pool.entity.Order;
import io.filpool.pool.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 查询订单失效定时任务，防止redis过期发射失败的情况
 */
@Component
@Slf4j
public class OrderExpireTask {
    @Autowired
    private OrderService orderService;

    @XxlJob("orderExpireTask")
    public void dealOrderExpire() throws Exception {
        //查找超过15分钟未过期订单
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        //设置15分钟之前
        calendar.set(Calendar.MINUTE, minute - 15);
        List<Order> orderList = orderService.lambdaQuery().eq(Order::getStatus, 1)
                .le(Order::getCreateTime, calendar.getTime()).list();
        if (!ObjectUtils.isEmpty(orderList)) {
            for (Order order : orderList) {
                //将订单设为过期状态
                updateOrder(order, now);
            }
        }
        XxlJobHelper.handleSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Order order, Date date)
            throws Exception {
        //将订单设为过期状态
        order.setStatus(4);
        order.setUpdateTime(date);
        orderService.updateOrder(order);
    }
}
