package de.netzkronehd.coins.message.listener;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import io.papermc.paper.connection.PlayerConnection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class CoinsUpdatePluginMessageListener implements PluginMessageListener {

    public static final String CHANNEL = "netzcoinsapi:coins";

    private final Gson gson;
    private final CoinsUpdateListener coinsUpdateListener;

    public CoinsUpdatePluginMessageListener(CoinsPlugin plugin, CoinsUpdateListener coinsUpdateListener) {
        this.coinsUpdateListener = coinsUpdateListener;
        this.gson = new Gson();
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL, this);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if(!channel.equals(CHANNEL)) return;
        handleRawMessage(message);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull PlayerConnection connection, byte @NotNull [] message) {
        if(!channel.equals(CHANNEL)) return;
        handleRawMessage(message);
    }

    private void handleRawMessage(byte[] message) {
        final var plainMessageString = ByteStreams.newDataInput(message).readUTF();
        final var update = gson.fromJson(plainMessageString, CoinsUpdateMessage.class);
        coinsUpdateListener.onCoinsUpdateMessage(update);
    }

}
