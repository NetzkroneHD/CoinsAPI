package de.netzkronehd.coins.source;

import de.netzkronehd.coins.CoinsPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerCoinsSource extends AbstractCoinsSource {

    private final Player player;

    public PlayerCoinsSource(CoinsPlugin plugin, double coins, Player player) {
        super(plugin, coins);
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }
}
