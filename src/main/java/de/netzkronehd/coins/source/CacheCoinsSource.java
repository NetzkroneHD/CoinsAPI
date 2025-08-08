package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import de.netzkronehd.coins.CoinsPlugin;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CacheCoinsSource implements CoinsSource {

    private final CoinsPlugin plugin;
    private final UUID uniqueId;
    private final String name;
    private final AtomicDouble coinsHolder;

    public CacheCoinsSource(CoinsPlugin plugin, UUID uniqueId, String name) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.name = name;
        this.coinsHolder = new AtomicDouble(0);
    }

    public CacheCoinsSource(CoinsPlugin plugin, UUID uniqueId, String name, double coins) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.name = name;
        this.coinsHolder = new AtomicDouble(coins);
    }

    @Override
    public void save() {

    }

    @Override
    public void saveAsync() {

    }

    @Override
    public void setCoins(double amount) {
        coinsHolder.set(amount);
    }

    @Override
    public double addCoins(double amount) {
        return coinsHolder.addAndGet(amount);
    }

    @Override
    public double removeCoins(double amount) {
        return coinsHolder.addAndGet(-amount);
    }

    @Override
    public double getCoins() {
        return coinsHolder.get();
    }


}
