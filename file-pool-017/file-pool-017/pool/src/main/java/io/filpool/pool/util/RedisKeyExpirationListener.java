package io.filpool.pool.util;

import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Order;
import io.filpool.pool.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private OrderService orderService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * Redis数据失效处理
     * */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith(Constants.ORDER_NUMBER_KEY)){
            //获取订单编号
            try {
                Order order = orderService.lambdaQuery().eq(Order::getOrderNumber,expiredKey.substring(Constants.ORDER_NUMBER_KEY.length())).one();
                if (order != null && order.getStatus() == 1){
                    //订单还是待付款状态,设置为超时
                    order.setStatus(4);
                    orderService.updateOrder(order);
                }
            } catch (Exception e){
                log.error("RedisKeyExpirationListenerException:", e);
            }
        }
    }
}
