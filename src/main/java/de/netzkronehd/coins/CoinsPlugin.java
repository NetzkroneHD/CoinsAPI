package de.netzkronehd.coins;

import de.netzkronehd.coins.cache.CacheService;
import de.netzkronehd.coins.database.DatabaseService;
import de.netzkronehd.coins.dependency.Dependency;
import de.netzkronehd.coins.dependency.DependencyManager;
import de.netzkronehd.coins.dependency.exception.DependencyDownloadException;
import de.netzkronehd.coins.dependency.exception.DependencyNotDownloadedException;
import de.netzkronehd.coins.dependency.impl.DependencyManagerImpl;
import de.netzkronehd.coins.source.PlayerCoinsSource;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class CoinsPlugin extends JavaPlugin {

    private final Map<UUID, PlayerCoinsSource> playerCache = new HashMap<>();

    private DependencyManager dependencyManager;
    private CacheService cacheService;
    private DatabaseService databaseService;

    @Override
    public void onLoad() {
        try {
            loadDependencies();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cacheService = new CacheService(this);
        databaseService = new DatabaseService(this);
    }

    @Override
    public void onEnable() {
        databaseService.createConfig();
        cacheService.createConfig();

        databaseService.readConfig();
        cacheService.startCacheReloadTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadDependencies() throws DependencyDownloadException, IOException, InterruptedException, ClassNotFoundException, DependencyNotDownloadedException {
        this.dependencyManager = new DependencyManagerImpl(getDataFolder().toPath().resolve("dependencies/"));
        for (Dependency dependency : Dependency.values()) {
            getLogger().info("Loading dependency: " + dependency.getMavenRepoPath());
            if (!dependencyManager.isDownloaded(dependency)) {
                getLogger().info("Downloading dependency: " + dependency.getMavenRepoPath());
                dependencyManager.downloadDependency(dependency);
            }
            dependencyManager.loadDependency(dependency);
        }
        getLogger().info("Dependencies loaded successfully.");
    }

    public Optional<PlayerCoinsSource> getPlayer(UUID uuid) {
        return Optional.ofNullable(playerCache.get(uuid));
    }

}
