package de.netzkronehd.coins.message.model;

import java.util.UUID;

/**
 * Represents a message for updating a player's coins.
 * @param messageId the unique identifier of the message
 * @param metaData metadata associated with the message
 * @param playerUniqueId the unique identifier of the player whose coins are to be updated
 * @param updateType the type of update (SET, ADD, REMOVE)
 * @param amount the amount of coins involved in the update
 */
public record CoinsUpdateMessage(UUID messageId, CoinsMessageMetaData metaData, UUID playerUniqueId, UpdateType updateType, double amount) {

    public static CoinsUpdateMessage of(UUID playerUniqueId, UpdateType updateType, double amount) {
        return new CoinsUpdateMessage(UUID.randomUUID(), CoinsMessageMetaData.create(), playerUniqueId, updateType, amount);
    }
}
