package de.netzkronehd.coins.message.redis;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.message.listener.CoinsUpdateListener;
import de.netzkronehd.coins.message.publisher.CoinsUpdateRedisPublisher;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

public class RedisClient implements Closeable {

    private final CoinsPlugin plugin;
    private final CoinsUpdateRedisPublisher publisher;
    private final RedisSubscriber subscriber;

    public RedisClient(CoinsPlugin plugin, RedisCredentials credentials, String channel, CoinsUpdateListener updateListener) {
        this.plugin = plugin;
        this.publisher = new CoinsUpdateRedisPublisher(credentials, channel);
        this.subscriber = new RedisSubscriber(credentials, channel, updateListener);
    }

    public void connect() {
        plugin.runAsync(publisher::connect);
        plugin.runAsync(subscriber::connect);
    }

    @Override
    public void close() {
        publisher.close();
        subscriber.close();
    }

    public boolean isReady() {
        return this.publisher.isReady() && this.subscriber.isReady();
    }

    public void awaitReady() {
        final AtomicBoolean ready = new AtomicBoolean(isReady());
        do {
            ready.set(isReady());
        } while (!ready.get());
    }

    public void runAfterReady(Runnable run) {
        plugin.runAsync(() -> {
            awaitReady();
            run.run();
        });
    }
}
