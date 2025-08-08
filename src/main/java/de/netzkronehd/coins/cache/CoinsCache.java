package de.netzkronehd.coins.cache;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.database.model.PlayerEntry;
import de.netzkronehd.coins.source.CacheCoinsSource;
import de.netzkronehd.coins.source.CoinsSource;

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

    public CoinsCache(List<PlayerEntry> entries, CoinsPlugin plugin) {
        this.plugin = plugin;
        this.nameCache = new HashMap<>();
        this.uuidCache = new HashMap<>();
        entries.forEach(entry -> {
            final CacheCoinsSource source = new CacheCoinsSource(
                plugin,
                entry.uuid(),
                entry.name(),
                entry.coins()
            );
            add(source);
        });
    }

    public void add(List<CoinsSource> sources) {
        sources.forEach(this::add);
    }

    public void add(CoinsSource source) {
        nameCache.put(source.getName().toLowerCase(), source);
        uuidCache.put(source.getUniqueId(), source);
    }

    public Optional<CoinsSource> get(String name) {
        return ofNullable(nameCache.get(name.toLowerCase()));
    }

    public Optional<CoinsSource> get(UUID uuid) {
        return ofNullable(uuidCache.get(uuid));
    }

}
