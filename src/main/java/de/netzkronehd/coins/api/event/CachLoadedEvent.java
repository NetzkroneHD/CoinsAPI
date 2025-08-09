package de.netzkronehd.coins.api.event;

import de.netzkronehd.coins.cache.CoinsCache;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class CachLoadedEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();
    private final CoinsCache coinsCache;

    public CachLoadedEvent(CoinsCache coinsCache) {
        super(true);
        this.coinsCache = coinsCache;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
