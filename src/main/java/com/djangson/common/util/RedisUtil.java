package com.djangson.common.util;

import com.djangson.common.base.domain.vo.CacheBaseVO;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.context.BeanTool;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 * @Author: wangqinjun@vichain.com
 * @date 2018/09/23 20:00
 */
public class RedisUtil {

    private static RedisTemplate redisTemplate = BeanTool.getBean(RedisTemplate.class);

    /**
     * 获取RedisTemplate
     * @return
     */
    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 普通数据存入
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通数据存入，并设置过期时间（秒）
     * @param key
     * @param value
     * @param expireTime
     */
    public static void set(String key, Object value, long expireTime) {
        if (expireTime <= 0) {
            set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 普通数据查询
     * @param key
     * @return
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * @param key
     */
    public static Long delete(String... key) {
        if (key == null || key.length <= 0) {
            ExceptionUtil.rollback("参数无效！", ErrorConstants.OPERATION_FAIL);
        }
        return redisTemplate.delete(CollectionUtils.arrayToList(key));
    }

    /**
     * 指定缓存失效时间（秒）
     * @param key
     * @param expireTime
     */
    public static void setExpire(String key, long expireTime) {
        if (expireTime <= 0) {
            ExceptionUtil.rollback("参数无效！", ErrorConstants.OPERATION_FAIL);
        }
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 查询缓存失效时间（秒）
     * @param key
     * @return
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Hash数据存入
     * @param key
     * @param map
     */
    public static void setHash(String key, Map<?, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Hash数据存入，并设置过期时间（秒）
     * @param key
     * @param map
     * @param expireTime
     */
    public static void setHash(String key, Map<?, ?> map, long expireTime) {
        redisTemplate.opsForHash().putAll(key, map);
        if (expireTime > 0) {
            setExpire(key, expireTime);
        }
    }

    /**
     * Hash字段数据存入
     * @param key
     * @param fieldKey
     * @param value
     */
    public static void setHashField(String key, Object fieldKey, Object value) {
        redisTemplate.opsForHash().put(key, fieldKey, value);
    }

    /**
     * 查询HashKey对应的完整对象，以Map返回
     * @param key
     * @return
     */
    public static Map<Object, Object> getHash(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    /**
     * 查询HashKey对应的完整对象，以Map返回
     * @param keyList
     * @return
     */
    public static List<Map<Object, Object>> getHash(List<String> keyList) {
        return (List<Map<Object, Object>>) redisTemplate.execute(new RedisCallback<List<Map<Object, Object>>>() {
            @Override
            public List<Map<Object, Object>> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return keyList.stream().map(key -> {

                    // 查询数据
                    Map<byte[], byte[]> entries = redisConnection.hGetAll(redisTemplate.getKeySerializer().serialize(key));
                    if (CollectionUtils.isEmpty(entries)) {
                        return new LinkedHashMap<>(0);
                    }

                    // 解析转换数据
                    Map<Object, Object> resultMap = new LinkedHashMap(entries.size());
                    for (Map.Entry<byte[], byte[]> entry : entries.entrySet()) {
                        resultMap.put(redisTemplate.getHashKeySerializer().deserialize(entry.getKey()), redisTemplate.getHashValueSerializer().deserialize(entry.getValue()));
                    }
                    return resultMap;
                }).collect(Collectors.toList());
            }
        });
    }

    /**
     * 查询HashKey对应对象下字段的值
     * @param key
     * @param fieldKey
     * @return
     */
    public static Object getHashField(String key, Object fieldKey) {
        return redisTemplate.opsForHash().get(key, fieldKey);
    }

    /**
     * 删除HashKey对应对象下字段
     * @param key
     * @param fieldKeys
     * @return
     */
    public static Object deleteHashField(String key, Collection<?> fieldKeys) {
        return redisTemplate.opsForHash().delete(key, fieldKeys.toArray());
    }

    /**
     * 判断HashKey对应对象下是否有该字段
     * @param key
     * @param fieldKey
     * @return
     */
    public static Object hasHashField(String key, Object fieldKey) {
        return redisTemplate.opsForHash().hasKey(key, fieldKey);
    }

    /**
     * List数据存入
     * @param key
     * @param value
     */
    public static void setList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * List数据存入，并设置过期时间（秒）
     * @param key
     * @param value
     * @param expireTime
     */
    public static void setList(String key, Object value, long expireTime) {
        redisTemplate.opsForList().rightPush(key, value);
        if (expireTime > 0) {
            setExpire(key, expireTime);
        }
    }

    /**
     * List数据存入
     * @param key
     * @param values
     */
    public static void setList(String key, Collection<?> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * List数据存入，并设置过期时间（秒）
     * @param key
     * @param values
     * @param expireTime
     */
    public static void setList(String key, Collection<?> values, long expireTime) {
        redisTemplate.opsForList().rightPushAll(key, values);
        if (expireTime > 0) {
            setExpire(key, expireTime);
        }
    }

    /**
     * List数据存入，指定索引更新
     * @param key
     * @param index
     * @param value
     */
    public static void setList(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * List数据查询，0 ~ -1 表示查询所有数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> getList(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * List数据查询，根据索引值查询
     * @param key
     * @param index
     * @return
     */
    public static Object getListByIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * List元素数量查询
     * @param key
     * @return
     */
    public static long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 删除List中值为value的元素，
     * count > 0：从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0：从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0：移除表中所有与 VALUE 相等的值。
     * @param key
     * @param value
     * @param count
     * @return
     */
    public static long deleteList(String key, Object value, long count) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * Set数据存入
     * @param key
     * @param values
     * @return
     */
    public static long setHashSet(String key, Collection<?> values) {
        return redisTemplate.opsForSet().add(key, values.toArray());
    }

    /**
     * Set数据存入，并设置过期时间（秒）
     * @param key
     * @param values
     * @param expireTime
     * @return
     */
    public static long setHashSet(String key, Collection<?> values, long expireTime) {
        Long count = redisTemplate.opsForSet().add(key, values.toArray());
        if (expireTime > 0) {
            setExpire(key, expireTime);
        }
        return count;
    }

    /**
     * Set数据查询
     * @param key
     * @return
     */
    public static Set<Object> getHashSet(String key) {
        Set<Object> valueSet = new HashSet<>();
        try (Cursor<Object> cursor = redisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().match("*").count(1000).build())) {
            while (cursor.hasNext()) {
                valueSet.add(cursor.next());
            }
        } catch (Exception e) {
            ExceptionUtil.rollback("operation fail...", e);
        }
        return valueSet;
    }

    /**
     * 删除Set中值为value的元素
     * @param key
     * @param values
     * @return
     */
    public static long deleteHashSet(String key, Collection<?> values) {
        return redisTemplate.opsForSet().remove(key, values.toArray());
    }

    /**
     * 判断Set中是否存在值为value的元素
     * @param key
     * @param value
     * @return
     */
    public static boolean hasHashSetValue(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }


    /**
     * 查询Set的长度
     * @param key 键
     * @return
     */
    public static long getHashSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * Sorted Set数据存入
     * @param key
     * @param values
     * @return
     */
    public static long setSortedSet(String key, Set<CacheBaseVO> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * Sorted Set数据存入，并设置过期时间（秒）
     * @param key
     * @param values
     * @param expireTime
     * @return
     */
    public static long setSortedSet(String key, Set<CacheBaseVO> values, long expireTime) {
        Long count = setSortedSet(key, values);
        if (expireTime > 0) {
            setExpire(key, expireTime);
        }
        return count;
    }

    /**
     * 根据下标范围查询Sorted Set数据查询
     * @param key
     * @return
     */
    public static Set<Object> getSortedSet(String key, long startIndex, long endIndex) {
        return redisTemplate.opsForZSet().range(key, startIndex, endIndex);
    }

    /**
     * 根据分数范围查询Sorted Set数据查询
     * @param key
     * @return
     */
    public static Set<Object> getSortedSet(String key, double startScore, double endScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, startScore, endScore);
    }

    /**
     * 根据分数范围查询Sorted Set数据查询
     * @param key
     * @return
     */
    public static Set<Object> getSortedSet(String key, double startScore, double endScore, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, startScore, endScore, offset, count);
    }

    /**
     * 删除Sorted Set中值为value的元素
     * @param key
     * @param values
     * @return
     */
    public static long deleteSortedSet(String key, Collection<?> values) {
        return redisTemplate.opsForZSet().remove(key, values.toArray());
    }

    /**
     * 删除Sorted Set中分数范围内的元素
     * @param key
     * @param startScore
     * @param endScore
     * @return
     */
    public static long deleteSortedSet(String key, double startScore, double endScore) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, startScore, endScore);
    }

    /**
     * 判断Sorted Set中是否存在值为value的元素
     * @param key
     * @param value
     * @return
     */
    public static boolean hasSortedSetValue(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value) != null;
    }


    /**
     * 查询Sorted Set的长度
     * @param key 键
     * @return
     */
    public static long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取匹配的所以值
     * @param pattern
     * @return
     */
    public static List<String> keys(String pattern) {
        return (List<String>) redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Set<String> keySet = new HashSet<>();
                try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build())) {
                    cursor.forEachRemaining(item -> {
                        keySet.add(new String(item, StandardCharsets.UTF_8));
                    });
                } catch (Exception e) {
                    ExceptionUtil.rollback("operation fail...", e);
                }
                return new ArrayList<>(keySet);
            }
        });
    }
}
