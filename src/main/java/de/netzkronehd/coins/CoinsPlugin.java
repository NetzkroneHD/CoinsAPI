package de.netzkronehd.coins;

import de.netzkronehd.coins.api.CoinsApi;
import de.netzkronehd.coins.api.impl.CoinsApiImpl;
import de.netzkronehd.coins.cache.CacheService;
import de.netzkronehd.coins.config.CoinsConfig;
import de.netzkronehd.coins.database.DatabaseService;
import de.netzkronehd.coins.database.model.PlayerEntry;
import de.netzkronehd.coins.dependency.Dependency;
import de.netzkronehd.coins.dependency.DependencyManager;
import de.netzkronehd.coins.dependency.exception.DependencyDownloadException;
import de.netzkronehd.coins.dependency.exception.DependencyNotDownloadedException;
import de.netzkronehd.coins.dependency.impl.DependencyManagerImpl;
import de.netzkronehd.coins.source.PlayerCoinsSource;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Getter
public final class CoinsPlugin extends JavaPlugin {

    @Getter
    public static CoinsPlugin instance;

    private final Map<UUID, PlayerCoinsSource> playerCache = new HashMap<>();

    private CoinsApi coinsApi;
    private CoinsConfig coinsConfig;
    private DependencyManager dependencyManager;
    private CacheService cacheService;
    private DatabaseService databaseService;

    @Override
    public void onLoad() {
        instance = this;
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
        saveDefaultConfig();
        loadConfig();

        coinsApi = new CoinsApiImpl(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling CoinsPlugin...");

        final var players = playerCache.values().stream().map(player -> new PlayerEntry(player.getUniqueId(), player.getName(), player.getCoins())).toList();
        try {
            databaseService.getDatabase().updatePlayers(players);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        playerCache.clear();
        getLogger().info("CoinsPlugin disabled successfully.");
    }

    private void loadConfig() {
        final var decimalFormat = new DecimalFormat(getConfig().getString("decimal-format", "#,##0.00"));
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.of(getConfig().getString("locale", "de"))));

        final var currencySymbol = getConfig().getString("currency.symbol", "C");
        final var currencyNameSingular = getConfig().getString("currency.name.singular", "Coin");
        final var currencyNamePlural = getConfig().getString("currency.name.plural", "Coins");

        this.coinsConfig = new CoinsConfig(decimalFormat, currencySymbol, currencyNameSingular, currencyNamePlural);
        getLogger().info("Coins configuration loaded successfully.");
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

    public Optional<PlayerCoinsSource> getPlayer(Player player) {
        if (player == null) {
            return Optional.empty();
        }
        return getPlayer(player.getUniqueId());
    }

    public Optional<PlayerCoinsSource> getPlayer(String name) {
        return getPlayer(getServer().getPlayer(name));
    }

    public void runAsync(Runnable run) {
        getServer().getScheduler().runTaskAsynchronously(this, run);
    }
}
