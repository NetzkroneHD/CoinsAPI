package de.netzkronehd.coins.message.redis;

import de.netzkronehd.coins.message.listener.CoinsUpdateListener;
import de.netzkronehd.coins.message.listener.RedisUpdateListener;

import java.util.concurrent.atomic.AtomicBoolean;

public class RedisSubscriber extends RedisConnection {

    private final String channel;
    private final CoinsUpdateListener listener;

    public RedisSubscriber(RedisCredentials credentials, String channel, CoinsUpdateListener listener) {
        super(credentials);
        this.channel = channel;
        this.listener = listener;
    }

    @Override
    public void connect() {
        super.connect();
        awaitReady();
        subscribe();
    }

    public void awaitReady() {
        final AtomicBoolean ready = new AtomicBoolean(isReady());
        do {
            ready.set(isReady());
        } while (!ready.get());
    }

    public void subscribe() {
        this.jedis.subscribe(new RedisUpdateListener(channel, listener), channel);
    }

}
