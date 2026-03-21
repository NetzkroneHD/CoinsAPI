package de.netzkronehd.coins.message.publisher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.redis.RedisConnection;

import java.time.OffsetDateTime;

public class CoinsUpdateRedisPublisher implements CoinsUpdateMessagePublisher {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .create();

    private final RedisConnection redisConnection;
    private final String channel;

    public CoinsUpdateRedisPublisher(RedisConnection redisConnection, String channel) {
        this.redisConnection = redisConnection;
        this.channel = channel;
    }

    @Override
    public void publishMessage(CoinsUpdateMessage updateMessage) {
        redisConnection.getRedisClient().publish(channel, GSON.toJson(updateMessage));
    }
}
