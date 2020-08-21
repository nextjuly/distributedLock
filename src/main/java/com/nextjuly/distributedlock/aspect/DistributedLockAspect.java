package com.nextjuly.distributedlock.aspect;

import com.nextjuly.distributedlock.service.DistributedLock;
import com.nextjuly.distributedlock.service.impl.RedisDistributedLock;
import com.nextjuly.distributedlock.annotation.DataLock;
import com.nextjuly.distributedlock.annotation.RedisLock;
import com.nextjuly.distributedlock.exception.BaseException;
import com.nextjuly.distributedlock.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 分布式锁切面
 *
 * @author wangyiting
 * @version 1.0 created 2019/1/24
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnClass(DistributedLock.class)
public class DistributedLockAspect {
	
	@Resource
	private RedisDistributedLock distributedLock;

	@Pointcut("@annotation(com.nextjuly.distributedlock.annotation.RedisLock)")
	private void lockPoint(){}
	
	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		//获得注解对象
		RedisLock redisLock = method.getAnnotation(RedisLock.class);
		//初始化key值
		StringBuffer key = new StringBuffer(redisLock.value());
		//没有指定key值需要从参数中获取
		if(StringUtils.isEmpty(redisLock.value())){
			//获得需要处理的参数顺序号
			int[] parameterSequenceNumbers = redisLock.parameterSequenceNumbers();
			Object[] args = pjp.getArgs();
			List<Object> objects = new ArrayList<>();
			if (parameterSequenceNumbers.length == 0) {
				return null;
			}
			//获取参数
			for (int parameterSequenceNumber : parameterSequenceNumbers) {
				if (parameterSequenceNumber >= args.length) {
					return null;
				}
				objects.add(args[parameterSequenceNumber]);
			}
			for (Object object : objects) {
				//组装key值
				this.assemblyKey(key, object);
			}
			if (StringUtils.isEmpty(key)) {
				return null;
			}
		}
		//若设置的后续操作是继续，设置重试次数
		int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
		//获取分布式锁
		boolean lock = distributedLock.lock(redisLock.prefix() + key.toString(), redisLock.keepMills(), retryTimes, redisLock.sleepMills());
		if(!lock) {
			log.debug("get lock failed : " + redisLock.prefix() + key.toString());
			throw new BaseException(redisLock.respStatusEnum());
		}
		//得到锁,执行方法，释放锁
		log.debug("get lock success : " + redisLock.prefix() + key);
		try {
			return pjp.proceed();
		} catch (Exception e) {
			log.error("execute locked method occured an exception", e);
			throw e;
		} finally {
			//释放锁
			boolean releaseResult = distributedLock.release(redisLock.prefix() + key.toString());
			log.debug("release lock : " + redisLock.prefix() + key.toString() + (releaseResult ? " success" : " failed"));
		}
	}

	/**
	 * 组装key值
	 *
	 * @author  wangyiting created 2020/4/9
	 * @param key key值
	 * @param object 参数对象
	 */
	private void assemblyKey(StringBuffer key, Object object) {
		if (object.getClass().isAnnotationPresent(DataLock.class)) {
			Field[] fields = ReflectUtils.getAllField(object);
			String[] fieldValues = new String[fields.length];
			Arrays.stream(fields).forEach(field -> {
				//允许反射访问
				field.setAccessible(true);
				if (field.isAnnotationPresent(DataLock.class)) {
					//获取注解
					DataLock dataLock = field.getAnnotation(DataLock.class);
					//获取顺序号
					int index = dataLock.order();
					try {
						Object o = field.get(object);
						if (o == null) {
							return;
						}
						fieldValues[index] = o.toString();
					} catch (IllegalAccessException e) {
						log.error("分布式锁-获取参数值出错");
					}
				}
			});
			Arrays.stream(fieldValues).filter(Objects::nonNull).forEach(key::append);
		} else {
			key.append(object);
		}
	}
}