package com.nextjuly.distributedlock.service;

/**
 * <p>
 * 分布式锁接口
 * </p>
 *
 * @author wangyiting
 * @since 2020-04-09
 */
@SuppressWarnings("ALL")
public interface DistributedLock {

    long TIMEOUT_MILLIS = 30000;

    int RETRY_TIMES = Integer.MAX_VALUE;

    long SLEEP_MILLIS = 500;

    /**
     * 根据key值获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @return flag
     */
    boolean lock(String key);

    /**
     * 根据key值和重试次数获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @param retryTimes 重试次数
     * @return flag
     */
    boolean lock(String key, int retryTimes);

    /**
     * 根据key值、重试次数和睡眠时间获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @param retryTimes 重试次数
     * @param sleepMillis 睡眠时间
     * @return flag
     */
    boolean lock(String key, int retryTimes, long sleepMillis);

    /**
     * 根据key值和预期时间获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @param expire 预期时间
     * @return flag
     */
    boolean lock(String key, long expire);

    /**
     * 根据key值、预期时间以及重试次数获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @param expire 到期时间
     * @param retryTimes 重试次数
     * @return flag
     */
    boolean lock(String key, long expire, int retryTimes);

    /**
     * 根据key值、预期时间以及重试次数获得锁
     *
     * @author  wangyiting created 2020/4/9
     * @param key key值
     * @param expire 到期时间
     * @param retryTimes 重试次数
     * @param sleepMillis 睡眠时间
     * @return flag
     */
    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 释放锁
     * @param lockKey key值
     * @return flag
     */
    boolean release(String lockKey);
}