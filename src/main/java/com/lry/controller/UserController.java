package com.lry.controller;

import com.lry.bean.User;
import com.lry.service.UserService;
import com.lry.util.RedisPool;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/8/28
 */
@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("hello world");
        return "hello world";
    }

//    @RequestMapping("/queryUser")
//    @ResponseBody
//    public List<User> queryUser(){
//        List<User> userList = userService.queryUser(0,10);
//        return userList;
//    }

    //采用先更新数据库，再删除缓存策略 +设置失效时间 可解决大部分缓存一致性问题
    //redis缓存一致性
    //读操作：先读缓存，读不到再读数据库，回写缓存
//    写操作：
//     先更新缓存，在更新数据库
//    （1）线程A更新了缓存
//    （2）线程B更新了缓存
//    （3）线程B更新了数据库
//    （4）线程A更新了数据库
//    先更新数据库，再更新缓存
//    （1）线程A更新了数据库
//    （2）线程B更新了数据库
//    （3）线程B更新了缓存
//    （4）线程A更新了缓存
//    先删除缓存，在更新数据库
//    该方案会导致不一致的原因是。同时有一个请求A进行更新操作，另一个请求B进行查询操作。那么会出现如下情形:
//    （1）请求A进行写操作，删除缓存
//    （2）请求B查询发现缓存不存在
//    （3）请求B去数据库查询得到旧值
//    （4）请求B将旧值写入缓存
//    （5）请求A将新值写入数据库
//    上述情况就会导致不一致的情形出现。而且，如果不采用给缓存设置过期时间策略，该数据永远都是脏数据。
//
//    先更新数据库，再删除缓存
//    失效：应用程序先从cache取数据，没有得到，则从数据库中取数据，成功后，放到缓存中。
//    命中：应用程序从cache中取数据，取到后返回。
//    更新：先把数据存到数据库中，成功后，再让缓存失效。

    //redis缓存穿透：大量请求访问一个一定不在数据库中的值，所有请求全部落在db，导致db宕机。->布隆过滤器/给缓存设置一个空值
//    缓存穿透，是指查询一个数据库一定不存在的数据。正常的使用缓存流程大致是，数据查询先进行缓存查询，
//    如果key不存在或者key已经过期，再对数据库进行查询，并把查询到的对象，放进缓存。如果数据库查询对象为空，则不放进缓存。

//    redis缓存雪崩：缓存数据在某个集中时间失效，导致大量请求落在db，导致宕机。->分散设置缓存失效时间
//    缓存雪崩，是指在某一个时间段，缓存集中过期失效。
//    产生雪崩的原因之一，比如在写本文的时候，马上就要到双十二零点，很快就会迎来一波抢购，
//    这波商品时间比较集中的放入了缓存，假设缓存一个小时。那么到了凌晨一点钟的时候，这批商品的缓存就都过期了。
//    而对这批商品的访问查询，都落到了数据库上，对于数据库而言，就会产生周期性的压力波峰。
//    小编在做电商项目的时候，一般是采取不同分类商品，缓存不同周期。在同一分类中的商品，加上一个随机因子。
//    这样能尽可能分散缓存过期时间，而且，热门类目的商品缓存时间长一些，冷门类目的商品缓存时间短一些，也能节省缓存服务的资源。

    //缓存击穿 是指一个key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，当这个key在失效的瞬间，
    // 持续的大并发就穿破缓存，直接请求数据库，就像在一个屏障上凿开了一个洞。
    //设置缓存不过期，数据库更新后删除，再重新get设置缓存不过期
    @RequestMapping("/testRedis")
    @ResponseBody
    public Object testRedis(){
        System.out.println(RedisPool.getJedis().get("stock"));
        System.out.println(stringRedisTemplate.opsForValue().get("stock"));
        User user = (User)redisTemplate.opsForValue().get("user");
        System.out.println(user);
        return user;
    }

    //缓存穿透/缓存雪崩
    @RequestMapping("/queryUser")
    @ResponseBody
    public User queryUser(int userId){
        //先从缓存找
        User user = (User)redisTemplate.opsForValue().get("user:"+userId);
        if(user!=null)
            return user;
        //再查数据库
        System.out.println("查询数据库，id=" + userId);
        user = userService.getUser(userId);
        if(user!=null){
            Random random = new Random();//热门商品过期时间设置长一点
            long time = 3600+random.nextInt(3600);//一个小时到两个小时，解决缓存雪崩
            redisTemplate.opsForValue().set("user:"+userId,user,time, TimeUnit.SECONDS);
        }else{//解决缓存穿透
            redisTemplate.opsForValue().set("user:"+userId,null,60, TimeUnit.SECONDS);
        }
        return user;
    }
//    RESP 是redis客户端和服务端之前使用的一种通讯协议；
//
//    RESP 的特点：实现简单、快速解析、可读性好
//
//    For Simple Strings the first byte of the reply is "+" 回复
//
//    For Errors the first byte of the reply is "-" 错误
//
//    For Integers the first byte of the reply is ":" 整数
//
//    For Bulk Strings the first byte of the reply is "$" 字符串
//
//    For Arrays the first byte of the reply is "*" 数组

    //秒杀扣减库存
    @RequestMapping("/subStock")
    @ResponseBody
    public int subStock(){
        String requestId = UUID.randomUUID().toString();
        String key = "stockkey";
        int stock=0;
        try{
            //事务加锁设置expire
            stringRedisTemplate.setEnableTransactionSupport(true);
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForValue().setIfAbsent(key,requestId);//setnx
            stringRedisTemplate.expire(key,30, TimeUnit.SECONDS);
            List<Object> result = stringRedisTemplate.exec();
            //开子线程给这把锁续命，每隔十秒执行stringRedisTemplate.expire(key,30, TimeUnit.SECONDS);
            //如果没到十秒本请求执行结束，释放锁，子线程自己消亡
            if(result.get(0).equals(true)&&result.get(1).equals(true)){
                stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
                if(stock<=0){
                    System.out.println("库存不足");
                    return stock;
                }
                stock-=1;
                System.out.println("剩余库存："+stock);
                stringRedisTemplate.opsForValue().increment("stock",-1);
            }
        }finally {
            //只有自己才能释放自己的锁
            if(requestId.equals(stringRedisTemplate.opsForValue().get(key))){
                stringRedisTemplate.delete(key);
            }
        }
        return stock;
    }



}
