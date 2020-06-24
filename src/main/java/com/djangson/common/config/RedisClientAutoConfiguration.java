package com.djangson.common.config;

import com.djangson.common.context.BeanTool;
import com.djangson.common.util.ExceptionUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/3/27 16:43
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisClientAutoConfiguration {

    /**
     * 设置缓存管理
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(cacheConfiguration).build();
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {

        // 初始化设置序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializer stringSerializer = new StringRedisSerializer();

        // 配置 redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer); // Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 生成连接工厂对象
     * @return
     */
    private synchronized RedisConnectionFactory redisConnectionFactory() {

        // 1. 若客户端配置Lettuce，则默认使用自动化配置
        if (BeanTool.getProperty("spring.redis.lettuce.pool.max-active") != null) {
            return BeanTool.getBean(LettuceConnectionFactory.class);
        }

        // 2. 若客户端使用Jedis，则需要手动配置
        if (BeanTool.getProperty("spring.redis.jedis.pool.max-active") != null) {

            // 若Bean已存在，则直接返回该Bean
            RedisConnectionFactory redisConnectionFactory = getJedisConnectionFactory();
            if (redisConnectionFactory != null) {
                return redisConnectionFactory;
            }

            // 初始化客户连接配置（单节点）
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
            standaloneConfig.setHostName(BeanTool.getProperty("spring.redis.host"));
            standaloneConfig.setPort(Integer.parseInt(BeanTool.getProperty("spring.redis.port")));
            standaloneConfig.setPassword(RedisPassword.of(BeanTool.getProperty("spring.redis.password")));

            // 初始化客户端连接池配置
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxIdle(Integer.parseInt(BeanTool.getProperty("spring.redis.jedis.pool.max-idle")));
            poolConfig.setMaxTotal(Integer.parseInt(BeanTool.getProperty("spring.redis.jedis.pool.max-active")));
            poolConfig.setMaxWaitMillis(getMillis(BeanTool.getProperty("spring.redis.jedis.pool.max-wait")));

            // 构建客户端配置对象
            JedisClientConfiguration.DefaultJedisClientConfigurationBuilder configurationBuilder = (JedisClientConfiguration.DefaultJedisClientConfigurationBuilder) JedisClientConfiguration.builder();
            configurationBuilder.usePooling().poolConfig(poolConfig);
            configurationBuilder.connectTimeout(Duration.ofMillis(getMillis(BeanTool.getProperty("spring.redis.timeout"))));
            configurationBuilder.readTimeout(Duration.ofMillis(getMillis(BeanTool.getProperty("spring.redis.timeout"))));
            JedisClientConfiguration clientConfig = configurationBuilder.build();

            // 注册连接池工厂对象
            BeanTool.registerBean("jedisConnectionFactory", JedisConnectionFactory.class, standaloneConfig, clientConfig);

            // 返回连接池
            return BeanTool.getBean("jedisConnectionFactory", JedisConnectionFactory.class);
        }

        return BeanTool.getBean(RedisConnectionFactory.class);
    }

    /**
     * 将时间字符串转成时间毫秒
     * @return
     */
    private long getMillis(String time) {
        if (time == null) {
            return 0;
        }
        if (!time.endsWith("ms")) {
            ExceptionUtil.rollback("Redis配置参数有误！请检查先关参数是否配置正确！");
        }
        return Long.parseLong(time.replace("ms", ""));
    }

    /**
     * 获取JedisConnectionFactory Bean对象
     * @return
     */
    private JedisConnectionFactory getJedisConnectionFactory() {
        try {
            return BeanTool.getBean("jedisConnectionFactory", JedisConnectionFactory.class);
        } catch (Exception e) {
            return null;
        }
    }
}
