package io.filpool.scheduled;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.DateUtil;
import io.filpool.pool.entity.Order;
import io.filpool.pool.entity.PowerOrder;
import io.filpool.pool.service.OrderService;
import io.filpool.pool.service.PowerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 算力订单失效查询任务
 */
@Component
@Slf4j
public class PowerOrderInvalidTask {
    @Autowired
    private PowerOrderService powerOrderService;
    @Autowired
    private OrderService orderService;

    @XxlJob("powerOrderInvalidTask")
    public void dealOrderInvalid() throws Exception {
        //查出今日到期的有效算力订单
        Date todayStart = DateUtil.getTodayStart();
        List<PowerOrder> powerOrders = powerOrderService.lambdaQuery().eq(PowerOrder::getIsEffect, true)
                .le(PowerOrder::getInvalidTime, todayStart).list();
        if (!ObjectUtils.isEmpty(powerOrders)) {
            for (PowerOrder order : powerOrders) {
                updateOrder(order);
            }
        }
        XxlJobHelper.handleSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(PowerOrder order) throws Exception {
        order.setIsEffect(false);
        powerOrderService.updatePowerOrder(order);
        if (order.getType() == 1) {
            //云算力订单更改失效状态
            boolean isUpdate = orderService.lambdaUpdate()
                    .eq(Order::getId, order.getRecordId())
                    .set(Order::getUpdateTime,new Date())
                    .set(Order::getIsEffect, false).update();
            if (!isUpdate){
                //更新失败，抛异常回滚
                throw new RuntimeException("更新订单算力有效状态失败");
            }
        }
    }
}
