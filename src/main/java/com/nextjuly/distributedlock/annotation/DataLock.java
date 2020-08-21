package com.nextjuly.distributedlock.annotation;

import java.lang.annotation.*;

/**
 * redis分布式数据锁注解
 *
 * @author wangyiting
 * @date 2019年12月31日 09:25:07
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataLock {

    /**
     * redis key拼接顺序号
     * @return int
     */
    int order() default 0;
}