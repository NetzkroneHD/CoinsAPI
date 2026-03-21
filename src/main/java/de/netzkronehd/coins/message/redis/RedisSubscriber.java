package de.netzkronehd.coins.message.redis;

import de.netzkronehd.coins.message.listener.CoinsUpdateListener;
import de.netzkronehd.coins.message.listener.RedisUpdateListener;
import lombok.Getter;

public class RedisSubscriber {

    private final String channel;
    private final CoinsUpdateListener listener;
    private final RedisConnection redisConnection;

    @Getter
    private boolean subscribed = false;

    public RedisSubscriber(RedisConnection redisConnection, String channel, CoinsUpdateListener listener) {
        this.redisConnection = redisConnection;
        this.channel = channel;
        this.listener = listener;
    }

    public void subscribe() {
        this.redisConnection.getRedisClient().subscribe(new RedisUpdateListener(channel, listener), channel);
        subscribed = true;
    }

}
