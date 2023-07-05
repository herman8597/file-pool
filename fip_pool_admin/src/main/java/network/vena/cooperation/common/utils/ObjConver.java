package network.vena.cooperation.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 对象转换工具类
 */
public class ObjConver {

    /**
     * 对象转换
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T converOne(Object object, Class<T> clazz) {
        return JSONObject.parseObject(JSONObject.toJSONString(object), clazz);
    }

    /**
     * 对象列表装换
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> converList(Object object, Class<T> clazz) {
        return JSONObject.parseArray(JSONObject.toJSONString(object), clazz);
    }
}
