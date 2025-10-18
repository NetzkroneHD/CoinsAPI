package de.netzkronehd.coins.message.publisher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.redis.RedisConnection;
import de.netzkronehd.coins.message.redis.RedisCredentials;

import java.time.OffsetDateTime;

public class CoinsUpdateRedisPublisher extends RedisConnection implements CoinsUpdateMessagePublisher{

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .create();

    private final String channel;

    public CoinsUpdateRedisPublisher(RedisCredentials credentials, String channel) {
        super(credentials);
        this.channel = channel;
    }

    @Override
    public void publishMessage(CoinsUpdateMessage updateMessage) {
        this.jedis.publish(channel, GSON.toJson(updateMessage));
    }
}
