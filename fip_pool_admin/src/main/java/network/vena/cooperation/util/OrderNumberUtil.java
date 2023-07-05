package network.vena.cooperation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtil {

    /**
     * 20位订单号
     */
    public static String getOrderNumber(){
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return (now + getRandom(4));
    }

    /**
     * 生成固定位数的随机数
     */
    public static String getRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val.append(random.nextInt(10));
        }
        return val.toString();
    }

    public static void main(String[] args) {
    }


}
