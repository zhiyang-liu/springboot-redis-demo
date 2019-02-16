package com.lianjia.springbootredis.config;

import com.lianjia.springbootredis.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {  
  
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    //RedisTemplate<Object, Object>这种返回方式，只能注入RedisTemplate<Object, Object>和private RedisTemplate<String, String> redisTemplate;
    //但是RedisTemplate<Object, Object>，这种注入方式，可以再写入或者读取时，使用任意对象。
    //如果确保项目中只是用一种redis结构，最好在这里准确定义，例如：RedisTemplate<String, User>，但是这种定义方式的template将无法支持其他类型
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
  
        //使用fastjson序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);  
        // value值的序列化采用fastJsonRedisSerializer ,如果不指定，存储的对象必须实现Serializable且要有无参构造函数。序列化到redis中的对象将是字节序列
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用StringRedisSerializer  
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
  
        template.setConnectionFactory(redisConnectionFactory);  
        return template;  
    }  
  
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}  