package de.netzkronehd.coins.api.event;

import de.netzkronehd.coins.source.CoinsSource;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class CoinsInitializedEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    private final CoinsSource source;

    public CoinsInitializedEvent(CoinsSource source) {
        super(true);
        this.source = source;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
