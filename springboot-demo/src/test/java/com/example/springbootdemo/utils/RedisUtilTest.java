package com.example.springbootdemo.utils;

import com.example.springbootdemo.service.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by shixi03 on 2018/5/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    @Test
    public void allRedisTest() {
        RedisUtil.putForHash(redisTemplate, "tdw", "is", "great");
    }
}
