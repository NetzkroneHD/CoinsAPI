package de.netzkronehd.coins.cache;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.database.model.PlayerEntry;
import de.netzkronehd.coins.source.CacheCoinsSource;
import de.netzkronehd.coins.source.CoinsSource;
import de.netzkronehd.coins.source.PlayerCoinsSource;

import java.util.*;

import static java.util.Optional.ofNullable;

public class CoinsCache {

    private final CoinsPlugin plugin;
    private final Map<String, CoinsSource> nameCache;
    private final Map<UUID, CoinsSource> uuidCache;

    public CoinsCache(CoinsPlugin plugin) {
        this.plugin = plugin;
        this.nameCache = new HashMap<>();
        this.uuidCache = new HashMap<>();
    }

    public CoinsCache(CoinsPlugin plugin, List<PlayerEntry> entries) {
        this.plugin = plugin;
        this.nameCache = new HashMap<>();
        this.uuidCache = new HashMap<>();
        putAllEntries(entries);
    }

    public void putAllEntries(List<PlayerEntry> entries) {
        entries.forEach(entry -> {
            final CacheCoinsSource source = new CacheCoinsSource(
                    plugin,
                    entry.coins(),
                    entry.uuid(),
                    entry.name()
            );
            put(source);
        });
    }

    public void putAll(List<CoinsSource> sources) {
        sources.forEach(this::put);
    }

    public void put(CoinsSource source) {
        nameCache.put(source.getName().toLowerCase(), source);
        uuidCache.put(source.getUniqueId(), source);
    }

    public void cachePlayer(PlayerCoinsSource player) {
        final CacheCoinsSource source = new CacheCoinsSource(
                plugin,
                player.getCoinsHolder(),
                player.getUniqueId(),
                player.getName()
        );
        put(source);
    }

    public Optional<CoinsSource> get(String name) {
        return ofNullable(nameCache.get(name.toLowerCase()));
    }

    public Optional<CoinsSource> get(UUID uuid) {
        return ofNullable(uuidCache.get(uuid));
    }

    public void clear() {
        nameCache.clear();
        uuidCache.clear();
    }

    public Collection<CoinsSource> getAll() {
        return uuidCache.values();
    }

    public List<CoinsSource> getToplist(int maxEntries) {
        return uuidCache.values().stream()
                .sorted(Comparator.comparingDouble(CoinsSource::getCoins).reversed())
                .limit(maxEntries)
                .toList();
    }
}
