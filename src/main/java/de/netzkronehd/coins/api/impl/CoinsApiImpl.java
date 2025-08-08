package de.netzkronehd.coins.api.impl;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.api.CoinsApi;
import de.netzkronehd.coins.cache.CoinsCache;
import de.netzkronehd.coins.source.PlayerCoinsSource;

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
    public Optional<PlayerCoinsSource> getPlayer(UUID uuid) {
        return plugin.getPlayer(uuid);
    }

    @Override
    public Optional<PlayerCoinsSource> getPlayer(String name) {
        return plugin.getPlayer(name);
    }
}
