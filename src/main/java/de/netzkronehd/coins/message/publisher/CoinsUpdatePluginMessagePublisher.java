package de.netzkronehd.coins.message.publisher;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.listener.CoinsUpdatePluginMessageListener;

public class CoinsUpdatePluginMessagePublisher implements CoinsUpdateMessagePublisher {

    public static final String CHANNEL = "bungeecord:main";

    private final CoinsPlugin plugin;
    private final Gson gson;

    public CoinsUpdatePluginMessagePublisher(CoinsPlugin plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
    }

    @Override
    public void publishMessage(CoinsUpdateMessage updateMessage) {
        final var output = ByteStreams.newDataOutput();

        output.writeUTF("Forward");
        output.writeUTF("ALL");
        output.writeUTF(CoinsUpdatePluginMessageListener.CHANNEL);
        output.writeUTF(gson.toJson(updateMessage));

        plugin.getServer().sendPluginMessage(plugin, CHANNEL, output.toByteArray());
    }
}
