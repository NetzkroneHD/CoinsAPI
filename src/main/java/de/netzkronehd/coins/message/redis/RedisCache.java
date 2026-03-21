package de.netzkronehd.coins.message.redis;

import java.util.Optional;

public class RedisCache {

    private final String keyPrefix;
    private final RedisConnection redisConnection;

    public RedisCache(RedisConnection redisConnection, String keyPrefix) {
        this.keyPrefix = keyPrefix;
        this.redisConnection = redisConnection;
    }

    public void set(String key, String value) {
        redisConnection.getRedisClient().set(keyPrefix+key, value);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(redisConnection.getRedisClient().get(keyPrefix+key));
    }

    public void remove(String key) {
        redisConnection.getRedisClient().del(keyPrefix+key);
    }

}
