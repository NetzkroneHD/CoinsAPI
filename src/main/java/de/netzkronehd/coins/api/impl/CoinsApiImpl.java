package de.netzkronehd.coins.api.impl;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.api.CoinsApi;
import de.netzkronehd.coins.cache.CacheService;
import de.netzkronehd.coins.cache.CoinsCache;
import de.netzkronehd.coins.config.CoinsConfig;
import de.netzkronehd.coins.database.DatabaseService;
import de.netzkronehd.coins.economy.CoinsEconomy;
import de.netzkronehd.coins.source.CacheCoinsSource;
import de.netzkronehd.coins.source.CoinsSource;
import de.netzkronehd.coins.source.PlayerCoinsSource;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class CoinsApiImpl implements CoinsApi {

    private final CoinsPlugin plugin;

    public CoinsApiImpl(CoinsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CoinsCache getCache() {
        return plugin.getCacheService().getCoinsCache();
    }

    @Override
    public CoinsConfig getConfig() {
        return plugin.getCoinsConfig();
    }

    @Override
    public Optional<CoinsSource> createCacheSource(UUID uuid, String name, double coins) throws SQLException {
        final var player = getCache().get(uuid);
        if (player.isPresent()) {
            return Optional.empty();
        }
        plugin.getDatabaseService().getDatabase().insertPlayer(uuid, name, coins);
        final var source = new CacheCoinsSource(plugin, coins, uuid, name);
        getCache().put(source);
        return Optional.of(source);
    }

    @Override
    public Optional<PlayerCoinsSource> getPlayer(UUID uuid) {
        return plugin.getPlayer(uuid);
    }

    @Override
    public Optional<PlayerCoinsSource> getPlayer(String name) {
        return plugin.getPlayer(name);
    }

    @Override
    public void loadCache() throws SQLException {
        plugin.getCacheService().loadCache();
    }

    @Override
    public DatabaseService getDatabaseService() {
        return plugin.getDatabaseService();
    }

    @Override
    public CacheService getCacheService() {
        return plugin.getCacheService();
    }

    @Override
    public CoinsEconomy getCoinsEconomy() {
        return plugin.getCoinsEconomy();
    }
}
