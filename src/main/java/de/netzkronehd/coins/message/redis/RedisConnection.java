package de.netzkronehd.coins.message.redis;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.RedisClient;

import java.io.Closeable;

public class RedisConnection implements Closeable {

    private final RedisCredentials credentials;
    @Getter
    @Setter
    private RedisClient redisClient;

    public RedisConnection(RedisCredentials credentials) {
        this.credentials = credentials;
    }

    public void connect() {
        if (redisClient != null) {
            close();
        }
        final var config = DefaultJedisClientConfig.builder()
                .user(credentials.user())
                .password(credentials.password())
                .clientName(credentials.clientName())
                .database(credentials.database())
                .build();

        this.redisClient = RedisClient.builder().hostAndPort(credentials.host(), credentials.port()).clientConfig(config).build();
    }

    public boolean isReady() {
        return redisClient != null && redisClient.ping() != null;
    }

    @Override
    public void close() {
        this.redisClient.close();
    }

}
