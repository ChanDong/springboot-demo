package com.example.springbootdemo.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by shixi03 on 2018/5/23.
 */
@Configuration
public class RedisConfig {

    @Bean("redisConnection")
    @ConfigurationProperties("spring.redis")
    public JedisConnectionFactory redisConnection() {
        return new JedisConnectionFactory();
    }

    //有redisPool的配置
//    @Bean("redisConnection")
//    @ConfigurationProperties("spring.redis")
//    public JedisConnectionFactory redisConnection(JedisPoolConfig redisPool) {
//        return new JedisConnectionFactory(redisPool);
//    }

    @Bean("redisPool")
    @ConfigurationProperties("spring.redis.pool")
    public JedisPoolConfig redisPool() {
        return new JedisPoolConfig();
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, String> redisTemplate(
        @Qualifier("redisConnection")RedisConnectionFactory connectionFactory) {
        return createRedisTemplate(connectionFactory);
    }

    private RedisTemplate<String, String> createRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
