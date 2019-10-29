package com.lry.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/8/24
 */
public class RedisPool {

    private static JedisPool pool = null;

    static{
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(500);
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(100*1000);
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        pool = new JedisPool(config,"192.168.235.100",6379,10000);
    }


    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void release(Jedis jedis){
        if(jedis!=null)
        jedis.close();
    }

}
