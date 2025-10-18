package de.netzkronehd.coins.message.redis;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.io.Closeable;

public class RedisConnection implements Closeable {

    private final RedisCredentials credentials;

    protected Jedis jedis;

    public RedisConnection(RedisCredentials credentials) {
        this.credentials = credentials;
    }

    public void connect() {
        final DefaultJedisClientConfig config = DefaultJedisClientConfig.builder()
                .user(credentials.user())
                .password(credentials.password())
                .clientName(credentials.clientName())
                .database(credentials.database())
                .build();

        this.jedis = new Jedis(new HostAndPort(credentials.host(), credentials.port()), config);
    }

    public boolean isReady() {
        return jedis != null && jedis.isConnected();
    }

    @Override
    public void close() {
        this.jedis.close();
    }

}
