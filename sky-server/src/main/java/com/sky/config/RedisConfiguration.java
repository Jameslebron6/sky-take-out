package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建模版对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置redis链接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置redis key 的序列化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}