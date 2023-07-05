package io.filpool.framework.redislock;

import java.lang.annotation.*;

/**
 * 锁的参数
 * @author chendesheng
 * @create 2019/10/11 16:08
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@interface CacheParam {
    /**
     * 字段名称
     *
     * @return String
     */
    String name() default "";
}
