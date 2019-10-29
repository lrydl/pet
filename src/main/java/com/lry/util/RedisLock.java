package com.lry.util;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/8/23
 */
//        分布式锁一般有
//        数据库乐观锁、
//        基于Redis的分布式锁
//        基于ZooKeeper的分布式锁三种实现方式，
//        而本文将为大家带来的就是第二种基于Redis的分布式锁正确的实现方法，希望对大家会有所帮助。
//
//        首先，想要保证分布式锁可以使用，下面这四个条件是必须要满足的：
//        1、互斥性。在任意时刻，只有一个客户端能持有锁。
//        2、不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
//        3、具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
//        4、解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
public class RedisLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";//NX 不存在才会set
    private static final String SET_WITH_EXPIRE_TIME = "PX";//EX:s PX:ms

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        //
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
