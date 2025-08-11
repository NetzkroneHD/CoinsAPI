package de.netzkronehd.coins.api.event;

import de.netzkronehd.coins.source.CoinsSource;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class CoinsSourceSavedEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();
    private final CoinsSource coinsSource;

    public CoinsSourceSavedEvent(CoinsSource coinsSource) {
        super(true);
        this.coinsSource = coinsSource;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
