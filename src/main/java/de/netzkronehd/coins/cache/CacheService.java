package de.netzkronehd.coins.cache;

import de.netzkronehd.coins.CoinsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class CacheService {

    private final CoinsPlugin plugin;
    private final File file;

    private YamlConfiguration cfg;
    private long reloadCacheTimeInSeconds;


    public CacheService(CoinsPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "cache.yml");
    }

    public void createConfig() {
        if (!file.exists()) {
            plugin.saveResource("cache.yml", false);
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void readConfig() {
        this.reloadCacheTimeInSeconds = cfg.getLong("reload-cache-time-in-seconds", TimeUnit.MINUTES.toSeconds(15));
    }

    public void loadCache() {

    }

    public void startCacheReloadTask() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getLogger().info("Reloading cache...");
            final long startTime = System.currentTimeMillis();
            loadCache();
            plugin.getLogger().info("Cache reloaded in " + (System.currentTimeMillis() - startTime) + "ms");
        }, 0L, reloadCacheTimeInSeconds * 20L);
    }
}
