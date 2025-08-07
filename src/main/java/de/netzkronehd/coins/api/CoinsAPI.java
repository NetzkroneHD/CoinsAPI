package de.netzkronehd.coins.api;

import com.google.common.util.concurrent.AtomicDouble;
import de.netzkronehd.coins.CoinsPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface CoinsAPI {

  double getCoins(UUID uuid);
  double setCoins(UUID uuid, double amount);
  double addCoins(UUID uuid, double amount);
  double removeCoins(UUID uuid, double amount);

  AtomicDouble getCoinsHolder(Player player);

  Optional<CoinsPlayer> getPlayer(Player player);

  default double getCoins(Player player) {
    return getPlayer(player).map(CoinsPlayer::getCoins).orElse(0D);
  }

  default double addCoins(Player player, double amount) {
    return addCoins(player.getUniqueId(), amount);
  }


}
