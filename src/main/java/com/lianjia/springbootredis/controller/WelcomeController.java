package com.lianjia.springbootredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class WelcomeController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @RequestMapping(value = "/welcome")
    public String index() throws InterruptedException {
        /*redisTemplate.opsForValue().set("welcome","liuzhiyang");
        System.out.println(redisTemplate.opsForValue().get("welcome"));*/

        /**
         * redis实现分布式锁
         */
        String key = "lock";
        String value = UUID.randomUUID().toString();
        //key不存在时，创建并返回true，若存在则返回false
        Boolean nx = redisTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(), value.getBytes());
        //创建key成功，获取key
        if(nx){
            //设置超时时间，防止代码出错导致无法删除key
            redisTemplate.expire(key, 10, TimeUnit.SECONDS);
            System.out.println("分布式业务执行。。。。。。");
            Thread.sleep(6000);
            String lockValue = redisTemplate.opsForValue().get(key);
            //判断value相同再删除key，释放锁
            if(lockValue != null && lockValue.equals(value)){
                redisTemplate.delete(key);
                System.out.println("任务执行结束，删除分布式锁的key！！！");
            }
        }

        return "success";
    }
}
