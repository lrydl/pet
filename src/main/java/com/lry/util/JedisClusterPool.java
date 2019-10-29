package com.lry.util;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/8/26
 */
public class JedisClusterPool {
    private static JedisCluster jedisCluster;

    public static JedisCluster  getJedisCluster(){
        if(jedisCluster==null){
            synchronized (JedisClusterPool.class){
                if(jedisCluster==null){
                    Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
                    // 添加节点
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6380));
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6381));
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6382));
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6390));
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6391));
                    hostAndPortsSet.add(new HostAndPort("192.168.235.100", 6392));
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(500);
                    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
                    config.setMaxIdle(5);
                    // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
                    config.setMaxWaitMillis(100*1000);
                    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
                    config.setTestOnBorrow(true);
                    jedisCluster = new JedisCluster(hostAndPortsSet, config);
                }
            }
        }
        return jedisCluster;
    }

}
