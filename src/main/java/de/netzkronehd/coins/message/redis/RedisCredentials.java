package de.netzkronehd.coins.message.redis;

public record RedisCredentials(String host, int port, String user, String password, String clientName, int database) {
}
