package com.example.springbootdemo.service.redis;

import com.example.springbootdemo.domain.Billboard;
import com.example.springbootdemo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Billboard> redisZsetPut() {
        RedisUtil.putForZSet(redisTemplate, "a", "activity", 10);
        RedisUtil.putForZSet(redisTemplate, "b", "activity", 50);
        RedisUtil.putForZSet(redisTemplate, "c", "activity", 60);
        RedisUtil.putForZSet(redisTemplate, "d", "activity", 40);
        RedisUtil.putForZSet(redisTemplate, "e", "activity", 20);

        Set<ZSetOperations.TypedTuple<String>> hashSet = RedisUtil.getForZSet(redisTemplate, "activity", 0, 100);

        List<Billboard> billboards = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> hashSetValue :hashSet) {
            Billboard billboard = new Billboard();
            billboard.setName(hashSetValue.getValue().replace("\"", ""));
            billboard.setScore(String.valueOf(hashSetValue.getScore()));
            billboards.add(billboard);
        }
        return billboards;
    }
}
