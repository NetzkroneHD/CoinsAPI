package de.netzkronehd.coins.message.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import static de.netzkronehd.coins.api.CoinsApiInstance.INSTANCE_ID;

/**
 * Metadata for a coins message.
 *
 * @param senderInstanceId the unique identifier of the sender instance
 * @param createdTime      the time the message was created
 */
public record CoinsMessageMetaData(UUID senderInstanceId, OffsetDateTime createdTime) {

    public static CoinsMessageMetaData create() {
        return new CoinsMessageMetaData(INSTANCE_ID, OffsetDateTime.now());
    }

}
