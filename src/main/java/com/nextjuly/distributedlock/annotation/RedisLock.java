package com.nextjuly.distributedlock.annotation;


import com.nextjuly.distributedlock.enums.RespStatusEnum;

import java.lang.annotation.*;

/**
 * redis分布式锁注解
 *
 * @author wangyiting
 * @date 2019年12月31日 09:25:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {

	/** 前缀 */
	String prefix() default "";

	/** 锁的资源，redis的key*/
	String value() default "";

	/** 参数顺序号(在方法中的顺序) */
	int[] parameterSequenceNumbers() default {};

	/** 持锁时间,单位毫秒*/
	long keepMills() default 30000;

	/** 当获取失败时候动作*/
	LockFailAction action() default LockFailAction.CONTINUE;

	enum LockFailAction{
        /** 放弃 */
        GIVEUP,
        /** 继续 */
        CONTINUE;
    }

    /** 未获得锁之后提示错误枚举 **/
	RespStatusEnum respStatusEnum() default RespStatusEnum.DATA_IS_BEING_MODIFIED;

	/** 重试的间隔时间,设置GIVEUP忽略此项*/
    long sleepMills() default 200;

    /** 重试次数*/
    int retryTimes() default 5;
}