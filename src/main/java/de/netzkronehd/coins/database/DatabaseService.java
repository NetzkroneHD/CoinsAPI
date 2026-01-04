package de.netzkronehd.coins.database;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.config.DatabaseConfig;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DatabaseService {

    private final CoinsPlugin plugin;
    private final File file;
    private YamlConfiguration cfg;

    @Getter
    private Database database;

    public DatabaseService(CoinsPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "database.yml");
    }

    public void createConfig() {
        if (!file.exists()) {
            plugin.saveResource("database.yml", false);
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void readConfig() {
        final var config = DatabaseConfig.builder()
                .driver(cfg.getString("driver"))
                .host(cfg.getString("host"))
                .port(cfg.getInt("port"))
                .database(cfg.getString("database"))
                .username(cfg.getString("username"))
                .password(cfg.getString("password"))
                .maximumPoolSize(cfg.getInt("maximumPoolSize", 10))
                .minimumIdle(cfg.getInt("minimumIdle", 1))
                .build();
        loadDatabase(config);
    }

    public void loadDatabase(DatabaseConfig config) {
        final var databaseFile = plugin.getDataFolder().toPath().resolve(config.getDatabase() + ".db");
        this.database = config.createDatabase(databaseFile);
        plugin.getLogger().info("Loading database driver class: " + database.getClassName());
        database.loadDriverClass(plugin.getDependencyManager());

        plugin.getLogger().info(String.format("Connecting to database %s@%s:%s/%s...", config.getUsername(), config.getHost(), config.getPort(), config.getDatabase()));
        try {
            database.connect(config);
            plugin.getLogger().info("Database connection established successfully. Creating tables...");
            database.createTables();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            config.destroy();
        }
    }
}
