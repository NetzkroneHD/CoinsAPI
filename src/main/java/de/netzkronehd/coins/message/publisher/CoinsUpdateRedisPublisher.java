package de.netzkronehd.coins.message.publisher;

import com.google.gson.Gson;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.redis.RedisConnection;
import de.netzkronehd.coins.message.redis.RedisCredentials;

public class CoinsUpdateRedisPublisher extends RedisConnection implements CoinsUpdateMessagePublisher{

    private final String channel;
    private final Gson gson;

    public CoinsUpdateRedisPublisher(RedisCredentials credentials, String channel) {
        super(credentials);
        this.channel = channel;
        this.gson = new Gson();
    }

    @Override
    public void publishMessage(CoinsUpdateMessage updateMessage) {
        this.jedis.publish(channel, gson.toJson(updateMessage));
    }
}
