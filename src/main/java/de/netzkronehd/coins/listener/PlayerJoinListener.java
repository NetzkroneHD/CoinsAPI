package de.netzkronehd.coins.listener;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.source.PlayerCoinsSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerJoinListener {

    private final CoinsPlugin plugin;

    public PlayerJoinListener(CoinsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final var player = new PlayerCoinsSource(
                plugin,
                0,
                event.getPlayer()
        );
        plugin.getCacheService().getCoinsCache().put(player);
        plugin.runAsync(() -> {
            try {
                plugin.getDatabaseService().getDatabase().insertOrUpdatePlayer(player.getUniqueId(), player.getName());
                plugin.getDatabaseService().getDatabase().getPlayerEntry(player.getUniqueId()).ifPresent(entry -> {
                    player.getCoinsHolder().set(entry.coins());
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayer(event.getPlayer()).ifPresentOrElse(player -> {
            plugin.getCacheService().getCoinsCache().cachePlayer(player);
            player.saveAsync();

        }, () -> {});
    }

}
