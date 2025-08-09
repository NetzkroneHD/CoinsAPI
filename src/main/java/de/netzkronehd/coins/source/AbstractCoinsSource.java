package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.api.event.CoinsChangeEvent;

import java.sql.SQLException;
import java.util.function.Consumer;

public abstract class AbstractCoinsSource implements CoinsSource {

    protected final CoinsPlugin plugin;
    protected final AtomicDouble coinsHolder;

    public AbstractCoinsSource(CoinsPlugin plugin, AtomicDouble coinsHolder) {
        this.plugin = plugin;
        this.coinsHolder = coinsHolder;
    }

    public AbstractCoinsSource(CoinsPlugin plugin, double coins) {
        this(plugin, new AtomicDouble(coins));
    }

    @Override
    public double addCoins(double amount) {
        return setCoins(getCoins() + amount);
    }

    @Override
    public double removeCoins(double amount) {
        return setCoins(getCoins() - amount);
    }

    @Override
    public double getCoins() {
        return coinsHolder.get();
    }

    @Override
    public double setCoins(double amount) {
        final double from = getCoins();
        final var event = new CoinsChangeEvent(this, from, getCoins());
        plugin.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return from;
        }
        coinsHolder.set(event.getTo());
        return getCoins();
    }

    @Override
    public AtomicDouble getCoinsHolder() {
        return coinsHolder;
    }

    @Override
    public void save() throws SQLException {
        plugin.getDatabaseService().getDatabase().updatePlayer(getUniqueId(), getName(), getCoins());
    }

    @Override
    public void saveAsync(Consumer<CoinsSource> afterSave, Consumer<SQLException> onError) {
        plugin.runAsync(() -> {
            try {
                save();
                if (afterSave != null) afterSave.accept(this);
            } catch (SQLException e) {
                if (onError != null) {
                    onError.accept(e);
                } else {
                    plugin.getLogger().severe("Failed to save coins for player " + getName() + ": " + e);
                }
            }
        });
    }

}
