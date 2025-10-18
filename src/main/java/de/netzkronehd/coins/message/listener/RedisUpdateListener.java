package de.netzkronehd.coins.message.listener;

import com.google.gson.Gson;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

@Getter
public class RedisUpdateListener extends JedisPubSub {

    private final String channel;
    private final CoinsUpdateListener listener;
    private final Gson gson;

    public RedisUpdateListener(String channel, CoinsUpdateListener listener) {
        this.channel = channel;
        this.listener = listener;
        this.gson = new Gson();
    }

    @Override
    public void onMessage(String channel, String message) {
        if(!Objects.equals(channel, this.channel)) {
            return;
        }
        var updateMessage = gson.fromJson(message, CoinsUpdateMessage.class);
        listener.onCoinsUpdateMessage(updateMessage);
    }
}
