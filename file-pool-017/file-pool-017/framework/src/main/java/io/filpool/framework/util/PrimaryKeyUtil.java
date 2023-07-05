package io.filpool.framework.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description TODO 利用redis生成数据库全局唯一性id
 */
@Component
@Slf4j
public class PrimaryKeyUtil {


    private static RedisTemplate redisTemplate;

    public PrimaryKeyUtil(RedisTemplate redisTemplate) {
        PrimaryKeyUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取年的后两位加上一年多少天+当前小时数作为前缀
     *
     * @param date
     * @return
     */
    private String getOrderIdPrefix(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //String.format("%1$02d", var)
        // %后的1指第一个参数，当前只有var一个可变参数，所以就是指var。
        // $后的0表示，位数不够用0补齐，如果没有这个0（如%1$nd）就以空格补齐，0后面的n表示总长度，总长度可以可以是大于9例如（%1$010d），d表示将var按十进制转字符串，长度不够的话用0或空格补齐。
        String monthFormat = String.format("%1$02d", month + 1);
        String dayFormat = String.format("%1$02d", day);

        return year + monthFormat + dayFormat;
    }

    /**
     * 生成订单号
     *
     * @param date 日期
     * @return
     */
    public Long getOrderId(Date date) {
        String prefix = getOrderIdPrefix(date);
        String key = "ORDER_ID_" + prefix;
        String orderId = null;
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);
            //往前补4位
            orderId = prefix + String.format("%1$04d", increment);
        } catch (Exception e) {
            System.out.println("生成单号失败");
            e.printStackTrace();
        }
        return Long.valueOf(orderId);
    }

}
