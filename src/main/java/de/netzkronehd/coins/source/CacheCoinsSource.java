package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import de.netzkronehd.coins.CoinsPlugin;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CacheCoinsSource extends AbstractCoinsSource {

    private final UUID uniqueId;
    private final String name;

    public CacheCoinsSource(CoinsPlugin plugin, AtomicDouble coinsHolder, UUID uniqueId, String name) {
        super(plugin, coinsHolder);
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public CacheCoinsSource(CoinsPlugin plugin, double coins, UUID uniqueId, String name) {
        super(plugin, coins);
        this.uniqueId = uniqueId;
        this.name = name;
    }
}
