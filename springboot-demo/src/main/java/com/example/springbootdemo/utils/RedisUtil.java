package com.example.springbootdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by shixi03 on 2018/5/23.
 */
public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static final String FAILURE_LOG = "Method:{} Key:{} ChildKey:{} ErrorInfo:{}";

    public RedisUtil() {
    }

    /**
     * 提交单个 Hash
     * @param redisTemplate 数据源
     * @param data 数据
     * @param key 主key
     * @param childKey 子key
     */
    public static <T> void putForHash(RedisTemplate<String, String> redisTemplate, T data, String key, String childKey) {
        BoundHashOperations<String, String, String> boundHashOps;
        String jsonData;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            jsonData = JSON.toJSONString(data, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty);
            boundHashOps.put(childKey, jsonData);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "putForHash", key, childKey, e);
        }
    }

    /**
     * 批量保存Hash
     * @param redisTemplate 数据源
     * @param data 数据
     * @param key 主key
     */
    public static void putForAllHash(RedisTemplate<String, String> redisTemplate, Map<String, String> data, String key) {
        BoundHashOperations<String, String, String> boundHashOps;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            boundHashOps.putAll(data);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "putForAllHash", key, "", e);
        }
    }

    /**
     * 获取单个 Hash
     * @param redisTemplate 数据源
     * @param key 主key
     * @param childKey 子key
     * @param type class
     */
    public static <T> T getForHash(RedisTemplate<String, String> redisTemplate, String key, String childKey, Type type) {
        BoundHashOperations<String, String, String> boundHashOps;
        String jsonData;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            jsonData = boundHashOps.get(childKey);
            return JSONObject.parseObject(jsonData, type);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "getForHash", key, childKey, e);
        }
        return null;
    }

    public static <T> T getForHash(RedisTemplate<String, String> redisTemplate, String key, String childKey, Class<T> clazz) {
        return getForHash(redisTemplate, key, childKey, (Type)clazz);
    }

    /**
     * 根据主key获取所有的hash
     * @param redisTemplate
     * @param key
     * @return
     */
    public static Map<String, String> getForAllHash(RedisTemplate<String, String> redisTemplate, String key) {
        BoundHashOperations<String, String, String> boundHashOps;
        Map<String, String> hashMap = new HashMap<>();
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            hashMap = boundHashOps.entries();
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "getForAllHash", key, "", e);
        }
        return hashMap;
    }

    /**
     * 根据 childKeys 批量获取Hash
     * @param redisTemplate 数据源
     * @param key 主key
     * @param childkeys 子key
     * @param type class
     * @return
     */
    public static <T> List<T> getForAllHashByChildKeys(RedisTemplate<String, String> redisTemplate, String key, List<String> childkeys, Type type) {
        BoundHashOperations<String, String, String> boundHashOps;
        List<String> jsonData;
        List<T> list = new ArrayList<T>();
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            jsonData = boundHashOps.multiGet(childkeys);
            jsonData.forEach(json -> {
//                JSONObject obj = JSONObject.parseObject(json);
//                String value = obj == null ? null : Optional.ofNullable(obj.getString("Value")).orElse(json);
//                T t = JSONObject.parseObject(value, type);
//                if (t != null) {
//                    list.add(t);
//                }
                if (json != null) {
                    list.add((T) json);
                }
            });
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "getForAllHashByChildKeys", key, childkeys, e);
        }
        return list;
    }

    public static <T> List<T> getForAllHashByChildKeys(RedisTemplate<String, String> redisTemplate, String key, List<String> childKeys, Class<T> clazz) {
        return getForAllHashByChildKeys(redisTemplate, key, childKeys, (Type) clazz);
    }

    /**
     * 根据childKey来删除某个Hash
     * @param redisTemplate 数据源
     * @param key 主key
     * @param childKey 子key
     */
    public static void deleteForHash(RedisTemplate<String, String> redisTemplate, String key, String childKey) {
        BoundHashOperations boundHashOps;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            boundHashOps.delete(childKey);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "deleteForHash", key, childKey, e);
        }
    }

    /**
     * 提交 Value
     * @param redisTemplate 数据源
     * @param data 数据
     * @param key 主key
     * @param childKey 子key
     */
    public static <T> void putForValue(RedisTemplate<String, String> redisTemplate, T data, String key, String childKey) {
        BoundValueOperations<String, String> boundValueOps;
        String keyName = key;
        String jsonData;
        try {
            if (childKey != null) {
                keyName = key.concat(childKey);
            }
            jsonData = JSON.toJSONString(data, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty);
            boundValueOps = redisTemplate.boundValueOps(keyName);
            boundValueOps.set(jsonData);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "putForValue", key, childKey, e);
        }
    }

    /**
     * 获取Value
     * @param redisTemplate 数据源
     * @param key 主key
     * @param childKey 子key
     * @param type class
     */
    public static <T> T getForValue(RedisTemplate<String, String> redisTemplate, String key, String childKey, Type type) {
        String jsonData;
        try {
            jsonData = forValue(redisTemplate, key , childKey);
            return JSON.parseObject(jsonData, type);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "getForValue", key, childKey, e);
        }
        return null;
    }

    public static <T> T getForValue(RedisTemplate<String, String> redisTemplate, String key, Class<T> clazz) {
        return getForValue(redisTemplate, key, null, (Type)clazz);
    }

    /**
     * 获取的value是一个list的情况
     * @param redisTemplate
     * @param key
     * @param childKey
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getForValueList(RedisTemplate<String, String> redisTemplate, String key, String childKey, Class<T> clazz) {
        String jsonData;
        try {
            jsonData = forValue(redisTemplate, key, childKey);
            if (!StringUtils.isEmpty(jsonData)) {
                return JSON.parseArray(jsonData, clazz);
            }
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "getForValueList", key, childKey, e);
        }
        return null;
    }

    private static String forValue(RedisTemplate<String, String> redisTemplate, String key, String childKey) {
        BoundValueOperations<String, String> boundValueOps;
        String keyName = key;
        String jsonData;
        if (childKey != null) {
            keyName = key.concat(childKey);
        }
        boundValueOps = redisTemplate.boundValueOps(keyName);
        jsonData = boundValueOps.get();
        return jsonData;
    }

    public static <T> void putForZSet(RedisTemplate<String, String> redisTemplate, T data, String key, int score) {
        BoundZSetOperations<String, String> boundZSetOps;
        String jsonData;
        try {
            boundZSetOps = redisTemplate.boundZSetOps(key);
            jsonData = JSON.toJSONString(data, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty);
            boundZSetOps.add(jsonData, score);
        } catch (Exception e) {
            logger.error(FAILURE_LOG, "putForZSet", key, null, e);
        }
    }

    private static void setTimeout(BoundKeyOperations boundKeyOps, Expire expire) {
        if (expire != null) {
            if (expire.expireDate != null) {
                boundKeyOps.expireAt(expire.expireDate);
            } else {
                boundKeyOps.expire(expire.timeout, expire.unit);
            }
        }
    }

    /**
     * 超时参数
     */
    private static class Expire {
        private Date expireDate;
        private Long timeout;
        private TimeUnit unit;

        Expire(Date expireDate) {
            this.expireDate = expireDate;
        }

        Expire(Long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
        }
    }

    public static Expire expire(LocalDateTime expireDate) {
        if (expireDate == null) {
            return null;
        }
        return new Expire(Date.from(expireDate.toInstant(ZoneOffset.of("+8"))));
    }

    public static Expire expire(long timeout, TimeUnit timeUnit) {
        return new Expire(timeout, timeUnit);
    }
}
