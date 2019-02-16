package com.lianjia.springbootredis.controller;

import com.lianjia.springbootredis.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @RequestMapping(value = "/index")
    public String index(){
        User user = new User("小明",14);
        redisTemplate.opsForHash().put("key2","hash",user);
        redisTemplate.opsForValue().set("key3", user);
        User user22 = (User)redisTemplate.opsForHash().get("key2","hash");
        User user11=(User) redisTemplate.opsForValue().get("key3");
        System.out.println(user11.getUsername()+user11.getAge());
        System.out.println(user22.getUsername()+user22.getAge());
        return "success";
    }
}
