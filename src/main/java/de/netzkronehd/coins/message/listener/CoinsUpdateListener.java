package de.netzkronehd.coins.message.listener;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.source.CoinsSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CoinsUpdateListener {

    private final CoinsPlugin plugin;
    private final Set<UUID> processedMessages;

    public CoinsUpdateListener(CoinsPlugin plugin) {
        this.plugin = plugin;
        this.processedMessages = Collections.synchronizedSet(new HashSet<>());
    }

    public void onCoinsUpdateMessage(CoinsUpdateMessage update) {
        if(CoinsPlugin.INSTANCE_ID.equals(update.metaData().senderInstanceId())) {
            plugin.getLogger().fine("Ignoring coins update message from same instance with ID: " + update.metaData().senderInstanceId());
            return;
        }

        final var added = processedMessages.add(update.messageId());
        if(!added) {
            plugin.getLogger().fine("Ignoring duplicate coins update message with ID: " + update.messageId());
            return;
        }

        plugin.getCoinsApi().getSource(update.playerUniqueId()).ifPresentOrElse(
                coinsSource -> handleUpdateMessage(coinsSource, update),
                () -> plugin.getLogger().warning("Received coins update for unknown player with UUID: " + update.playerUniqueId())
        );
    }

    public void handleUpdateMessage(CoinsSource source, CoinsUpdateMessage message) {
        message.updateType().apply(source, message.amount());
    }

}
