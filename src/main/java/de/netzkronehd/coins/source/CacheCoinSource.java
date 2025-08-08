package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import de.netzkronehd.coins.CoinsPlugin;

import java.util.UUID;

public class CacheCoinSource implements CoinSource {

    private final CoinsPlugin plugin;
    private final UUID uniqueId;
    private final String name;
    private final AtomicDouble coins;

    public CacheCoinSource(CoinsPlugin plugin, UUID uniqueId, String name) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.name = name;
        this.coins = new AtomicDouble(0);
    }

    public CacheCoinSource(CoinsPlugin plugin, UUID uniqueId, String name, double coins) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.name = name;
        this.coins = new AtomicDouble(coins);
    }

    @Override
    public double getCoins() {
        return coins.get();
    }

    @Override
    public void setCoins(double amount) {
        coins.set(amount);
    }

    @Override
    public double addCoins(double amount) {
        return coins.addAndGet(amount);
    }

    @Override
    public double removeCoins(double amount) {
        return coins.addAndGet(-amount);
    }

    @Override
    public AtomicDouble getCoinsHolder() {
        return coins;
    }

    @Override
    public void save() {

    }

    @Override
    public void saveAsync() {

    }
}
