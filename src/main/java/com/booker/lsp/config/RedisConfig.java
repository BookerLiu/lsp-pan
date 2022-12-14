package com.booker.lsp.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @Author BookerLiu
 * @Date 2022/12/6 19:24
 * @Description redis
 **/
@Component
@Configuration
public class RedisConfig {

    /**
     * 默认是JDK的序列化策略，这里配置redisTemplate采用的是Jackson2JsonRedisSerializer的序列化策略
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
//    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // 使用 GenericFastJsonRedisSerializer 替换默认序列化
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();        // 设置key和value的序列化规则
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(stringRedisSerializer);
//        // 设置hashKey和hashValue的序列化规则
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(stringRedisSerializer);
//
//        redisTemplate.setEnableDefaultSerializer(false);
//        // 设置支持事物
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    /***
     * stringRedisTemplate默认采用的是String的序列化策略
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
//        StringRedisTemplate redisTemplate = new StringRedisTemplate();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setEnableDefaultSerializer(true);
//        return redisTemplate;
//    }

}
