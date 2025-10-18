package de.netzkronehd.coins.message.model;

import de.netzkronehd.coins.CoinsPlugin;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Metadata for a coins message.
 * @param senderInstanceId the unique identifier of the sender instance
 * @param createdTime the time the message was created
 */
public record CoinsMessageMetaData(UUID senderInstanceId, OffsetDateTime createdTime) {

    public static CoinsMessageMetaData create() {
        return new CoinsMessageMetaData(CoinsPlugin.INSTANCE_ID, OffsetDateTime.now());
    }

}
