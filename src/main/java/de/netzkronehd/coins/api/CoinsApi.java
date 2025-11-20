package de.netzkronehd.coins.api;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.cache.CacheService;
import de.netzkronehd.coins.cache.CoinsCache;
import de.netzkronehd.coins.config.CoinsConfig;
import de.netzkronehd.coins.database.DatabaseService;
import de.netzkronehd.coins.economy.CoinsEconomy;
import de.netzkronehd.coins.message.model.CoinsUpdateMessage;
import de.netzkronehd.coins.message.model.UpdateType;
import de.netzkronehd.coins.source.CoinsSource;
import de.netzkronehd.coins.source.PlayerCoinsSource;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoinsApi {

    void loadCache() throws SQLException;

    /**
     * Publishes an update via the plugin messaging channel to all connected servers.
     * @param source the source of the coins update
     * @param updateType the type of update (e.g., ADD, REMOVE, SET)
     * @param amount the amount of coins involved in the update
     * @return the CoinsUpdateMessage that was published
     */
    CoinsUpdateMessage publishUpdate(CoinsSource source, UpdateType updateType, double amount);

    /**
     * Creates a new cache source for the given UUID, name, and coins amount.
     * If a source with the same UUID already exists, it will not create a new one.
     *
     * @param uuid  The UUID of the player.
     * @param name  The name of the player.
     * @param coins The amount of coins to set.
     * @return An Optional containing the created CoinsSource if successful, or empty if it already exists.
     * @throws SQLException If there is an error during database operations.
     */
    Optional<CoinsSource> createCacheSource(UUID uuid, String name, double coins) throws SQLException;

    default Optional<CoinsSource> getSource(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return getCache().get(uuid);
    }

    default Optional<CoinsSource> getSource(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return getCache().get(name);
    }

    default List<CoinsSource> getToplist(int maxEntries) {
        return getCache().getToplist(maxEntries);
    }

    Optional<PlayerCoinsSource> getPlayer(UUID uuid);

    Optional<PlayerCoinsSource> getPlayer(String name);

    default Optional<PlayerCoinsSource> getPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Optional.empty();
        }
        return getPlayer(player.getUniqueId());
    }

    CoinsCache getCache();

    CoinsConfig getConfig();

    DatabaseService getDatabaseService();
    CacheService getCacheService();
    CoinsEconomy getCoinsEconomy();

    static CoinsApi getInstance() {
        return CoinsPlugin.getInstance().getCoinsApi();
    }

}
