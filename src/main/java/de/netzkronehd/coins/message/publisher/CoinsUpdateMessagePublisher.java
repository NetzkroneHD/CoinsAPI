package de.netzkronehd.coins.message.publisher;

import de.netzkronehd.coins.message.model.CoinsUpdateMessage;

public interface CoinsUpdateMessagePublisher {

    void publishMessage(CoinsUpdateMessage updateMessage);

}
