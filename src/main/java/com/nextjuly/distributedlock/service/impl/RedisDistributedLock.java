package com.nextjuly.distributedlock.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 分布式锁接口实现(redis实现)
 * </p>
 *
 * @author wangyiting
 * @since 2020-04-09
 */
@Slf4j
@Component
public class RedisDistributedLock extends AbstractDistributedLock {
	
	private ThreadLocal<String> lockFlag = new ThreadLocal<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		boolean result = setRedis(key, expire);
		// 如果获取锁失败，按照传入的重试次数进行重试
		while((!result) && retryTimes-- > 0){
			try {
				log.debug("lock failed, retrying..." + retryTimes);
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				return false;
			}
			result = setRedis(key, expire);
		}
		return result;
	}

    /**
     * 加锁
     * @param lockKey 加锁的Key
     * @param expire 锁持续时间
     * @return flag
     */
    private boolean setRedis(String lockKey, long expire){
        long timeStamp = System.currentTimeMillis() + expire;
        lockFlag.set(String.valueOf(timeStamp));
        if(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockFlag.get())){
            // 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
            return true;
        }

        //设置失败，获得锁失败
        // 判断锁超时 - 防止原来的操作异常，没有运行解锁操作 ，防止死锁
        String currentLock = stringRedisTemplate.opsForValue().get(lockKey);
        // 如果锁过期 currentLock不为空且小于当前时间
        if(!StringUtils.isEmpty(currentLock) && Long.parseLong(currentLock) < System.currentTimeMillis()){
            //如果lockKey对应的锁已经存在，获取上一次设置的时间戳之后并重置lockKey对应的锁的时间戳
            String preLock = stringRedisTemplate.opsForValue().getAndSet(lockKey, lockFlag.get());

            //假设两个线程同时进来这里，因为key被占用了，而且锁过期了。
            //获取的值currentLock=A(get取的旧的值肯定是一样的),两个线程的timeStamp都是B,key都是K.锁时间已经过期了。
            //而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的timeStamp已经变成了B。
            //只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
            return !StringUtils.isEmpty(preLock) && preLock.equals(currentLock);
        }

        return false;
    }

    @Override
    public boolean release(String lockKey){
        try {
            String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
            if(!StringUtils.isEmpty(currentValue) && currentValue.equals(lockFlag.get()) ){
                // 删除锁状态
                stringRedisTemplate.opsForValue().getOperations().delete(lockKey);
            }
            lockFlag.remove();
            return true;
        } catch (Exception e) {
            log.error("警报！警报！警报！解锁异常");
            System.out.println("警报！警报！警报！解锁异常");
        }
        return false;
    }
}