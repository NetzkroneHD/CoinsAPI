package de.netzkronehd.coins.message.redis;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.message.listener.CoinsUpdateListener;
import de.netzkronehd.coins.message.publisher.CoinsUpdateRedisPublisher;
import lombok.Getter;

import java.io.Closeable;

@Getter
public class CoinsRedisClient implements Closeable {

    private final CoinsPlugin plugin;
    private final CoinsUpdateRedisPublisher publisher;
    private final RedisSubscriber subscriber;
    private final RedisCache redisCache;
    private final RedisConnection redisConnection;

    public CoinsRedisClient(CoinsPlugin plugin, RedisCredentials credentials, String channel, String keyPrefix, CoinsUpdateListener updateListener) {
        this.plugin = plugin;
        this.redisConnection = new RedisConnection(credentials);
        this.publisher = new CoinsUpdateRedisPublisher(redisConnection, channel);
        this.subscriber = new RedisSubscriber(redisConnection, channel, updateListener);
        this.redisCache = new RedisCache(redisConnection, keyPrefix);
    }

    public void connect() {
        redisConnection.connect();
        runAfterReady(subscriber::subscribe);
    }

    @Override
    public void close() {
        redisConnection.close();
    }

    public boolean isReady() {
        return this.redisConnection.isReady();
    }

    public void awaitReady() {
        while (!isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    public void runAfterReady(Runnable run) {
        if (isReady()) {
            run.run();
        } else {
            plugin.runAsync(() -> {
                awaitReady();
                run.run();
            });
        }
    }
}
