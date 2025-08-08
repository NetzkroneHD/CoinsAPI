package de.netzkronehd.coins.api;

import de.netzkronehd.coins.cache.CoinsCache;
import de.netzkronehd.coins.source.CoinsSource;
import de.netzkronehd.coins.source.PlayerCoinsSource;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface CoinsApi {

    CoinsCache getCache();

    default Optional<CoinsSource> getCoinsSource(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return getCache().get(uuid);
    }

    default Optional<CoinsSource> getCoinsSource(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return getCache().get(name);
    }

    Optional<PlayerCoinsSource> getPlayer(UUID uuid);

    Optional<PlayerCoinsSource> getPlayer(String name);

    default Optional<PlayerCoinsSource> getPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return Optional.empty();
        }
        return getPlayer(player.getUniqueId());
    }


}
