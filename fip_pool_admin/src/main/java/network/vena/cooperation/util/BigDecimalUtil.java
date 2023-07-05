package network.vena.cooperation.util;


import java.math.BigDecimal;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigDecimalUtil {

    private static Boolean checkType(Object... values) {
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Boolean flag = false;
        for (Object value : values) {
            if (value instanceof Double) {
                flag = true;
            } else if (value instanceof String) {
                Matcher isNum = pattern.matcher(value.toString());
                if (isNum.matches()) {
                    flag = true;
                } else {
                    throw new RuntimeException("操作数值有误");
                }
            } else if (value instanceof Long) {
                flag = true;
            } else if (value instanceof Float) {
                flag = true;
            } else if (value instanceof Integer) {
                flag = true;
            } else if (value instanceof BigDecimal) {
                flag = true;
            } else if (value instanceof Short) {
                flag = true;
            }
        }
        return flag;
    }


    public static boolean greater(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) > 0;
    }

    public static boolean less(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) < 0;
    }

    public static boolean equal(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) == 0;
    }

    public static BigDecimal sub(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).subtract(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal add(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).add(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal divide(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2) || equal(BigDecimal.ZERO, decimal2)) {
            throw new RuntimeException("除法操作异常,除数不能为0");
        }
        return new BigDecimal(decimal.toString()).divide(new BigDecimal(decimal2.toString()), 8, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal multiply(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).multiply(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal format(Object bigDecimal, Integer num) {
        checkType(bigDecimal);
        if (ObjectUtils.isEmpty(bigDecimal)) {
            return BigDecimal.ZERO;
        }
        String amount = new Formatter().format("%." + num + "f", new BigDecimal(bigDecimal.toString())).toString();
        return new BigDecimal(amount);
    }

    public static void main(String[] args) {
        System.out.println(format("1.12541254123214",3));
    }
}
