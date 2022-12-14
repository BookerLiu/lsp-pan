package com.booker.lsp.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author BookerLiu
 * @Date 2022/12/5 11:53
 * @Description 简单操作redis
 **/
@Log4j2
@Component
public class RedisUtil {

    private static StringRedisTemplate stringRedisTemplate;


    public static String get(String key) throws Exception {
        try {
            ValueOperations<String, String> vOpera= stringRedisTemplate.opsForValue();
            return vOpera.get(key);
        } catch (Exception e) {
            log.error("redis get value error, key: {}", key,  e);
            throw e;
        }
    }

    public static void set(String key, String value, Integer seconds)throws Exception {
        try {
            ValueOperations<String, String> vOpera= stringRedisTemplate.opsForValue();
            if (seconds != null) {
                vOpera.set(key, value, seconds, TimeUnit.SECONDS);
            } else {
                vOpera.set(key, value);
            }

        } catch (Exception e) {
            log.error("redis set value error, key: {}, value:{}", key, value,  e);
            throw e;
        }
    }

    public static void set(String key, String value)throws Exception {
        set(key, value, null);
    }


    public static boolean expire(String key, int seconds) {
        try {
            Boolean expire = stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return expire != null && expire;
        } catch (Exception e) {
            log.error("redis expire error, key:{}, seconds: {}", key, seconds, e);
            return false;
        }
    }


    public static boolean delete(String key)throws Exception {
        try {
            Boolean delete = stringRedisTemplate.delete(key);
            return delete != null && delete;
        } catch (Exception e) {
            log.error("delete key error:{}", key, e);
            throw e;
        }
    }


    @Resource
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }
}
