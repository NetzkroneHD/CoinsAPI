package de.netzkronehd.coins.api.event;

import de.netzkronehd.coins.source.CoinsSource;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CoinsChangeEvent extends Event implements Cancellable {

    public static final HandlerList handlerList = new HandlerList();

    private final CoinsSource source;
    private final double from;
    private double to;
    private boolean cancelled;

    public CoinsChangeEvent(CoinsSource source, double from, double to) {
        this.source = source;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
