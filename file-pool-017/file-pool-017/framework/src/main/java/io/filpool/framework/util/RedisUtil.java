package io.filpool.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisUtil {
    @Resource
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 获取一个hash
     *
     * @param cacheKey
     * @return
     */
    public Map<String, Object> getCacheMap(String cacheKey) {
        BoundHashOperations<Serializable, String, Object> bound = redisTemplate.boundHashOps(cacheKey);
        return bound.entries();
    }

    /**
     * 从hash里获取一个值
     *
     * @param cacheKey
     * @param key
     * @return
     */
    public Object getDataFromCacheMap(String cacheKey, Object key) {
        BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        return bound.get(key);
    }

    /**
     * 向hash放进一个键值
     *
     * @param cacheKey
     * @param key
     * @param value
     */
    public void setDataFromCacheMap(String cacheKey, Object key, Object value) {
        BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        bound.put(key, value);
    }

    /**
     * 删除hash一个键值
     *
     * @param cacheKey
     * @param key
     */
    public void removeDataFromCacheMap(String cacheKey, Object key) {
        BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
        bound.delete(key);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 延长有效期
     *
     * @param key
     * @param minutes
     */
    public boolean makeMoreTime(final String key, long minutes) {
        return redisTemplate.boundValueOps(key).expire(minutes, TimeUnit.SECONDS);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }
        return result;
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long addSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long removeSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long increment(final String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String getSet(final String key, final String value, final Integer dbTag) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                //切换DB
//                if (!ObjectUtils.isEmpty(dbTag)) {
//                    connection.select(dbTag);
//                }
                //get
                return serializer.deserialize(connection.getSet(serializer.serialize(key), serializer.serialize(value)));
            }
        });
    }


    public Boolean setNx(final String key, final String value) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                //切换DB
//                if (!ObjectUtils.isEmpty(dbTag)) {
//                    connection.select(dbTag);
//                }
                //操作数据
                return connection.setNX(serializer.serialize(key), serializer.serialize(value));
            }
        });
    }

    /**
     * 阻塞锁
     * @param lockKey
     * @param lockExpireMils
     * @return
     */
    public synchronized boolean lock(String lockKey, long lockExpireMils) {
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            Boolean flag = false;
            do {
                long nowTime = System.currentTimeMillis();
                flag = connection.setNX(lockKey.getBytes(), String.valueOf(nowTime + lockExpireMils + 1).getBytes());
                if (!flag) {
                    byte[] value = connection.get(lockKey.getBytes());
                    if (Objects.nonNull(value) && value.length > 0) {
                        long oldTime = Long.parseLong(new String(value));
                        if (oldTime < nowTime) {
                            return Boolean.TRUE;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (!flag);
            return Boolean.TRUE;
        });
    }

    /**
     * 解锁
     * @param lockKey
     */
    public void unlock(String lockKey) {
        remove(lockKey);
    }
}
