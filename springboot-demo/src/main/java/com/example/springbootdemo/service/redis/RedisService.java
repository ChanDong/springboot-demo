package com.example.springbootdemo.service.redis;

import com.example.springbootdemo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shixi03 on 2018/5/23.
 */
@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    public void redisStringPut() {

        RedisUtil.putForValue(redisTemplate, "great", "test1", null);
        RedisUtil.putForHash(redisTemplate, "beautiful", "test2", "test2-1");
        RedisUtil.putForHash(redisTemplate, "beauty", "test2", "test2-2");

        Map<String, String> map = new HashMap<>();
        map.put("Monday", "周一");
        map.put("Tuesday", "周二");
        map.put("Wednesday", "周三");
        RedisUtil.putForAllHash(redisTemplate, map, "test3");

        String stringValue = RedisUtil.getForValue(redisTemplate, "test1", String.class);
        String hashValue = RedisUtil.getForHash(redisTemplate, "test2", "test2-1", String.class);

        List<String> childKeyList = new ArrayList<>();
        childKeyList.add("test2-1");
        childKeyList.add("test2-2");
        List result = RedisUtil.getForAllHashByChildKeys(redisTemplate, "test2", childKeyList, List.class);

        Map<String, String> redisMap = RedisUtil.getForAllHash(redisTemplate, "test3");

        logger.info("string的值是:{}", stringValue);
        logger.info("hash的值是:{}", hashValue);
        logger.info("hash的child的值是:{}" , result.toString());
        logger.info("hash的Map是:{}", redisMap.toString());
    }
}
