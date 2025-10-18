package de.netzkronehd.coins.message.listener;

import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.publisher.CoinsUpdateRedisPublisher;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

@Getter
public class RedisUpdateListener extends JedisPubSub {

    private final String channel;
    private final CoinsUpdateListener listener;

    public RedisUpdateListener(String channel, CoinsUpdateListener listener) {
        this.channel = channel;
        this.listener = listener;
    }

    @Override
    public void onMessage(String channel, String message) {
        if(!Objects.equals(channel, this.channel)) {
            return;
        }
        var updateMessage = CoinsUpdateRedisPublisher.GSON.fromJson(message, CoinsUpdateMessage.class);
        listener.onCoinsUpdateMessage(updateMessage);
    }
}
