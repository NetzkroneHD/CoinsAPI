package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public class PlayerCoinsSource {

    private final Player player;
    private final AtomicDouble atomicCoins;

    public double addCoins(double amount) {
        return atomicCoins.addAndGet(amount);
    }

    public double removeCoins(double amount) {
        return atomicCoins.addAndGet(-amount);
    }

    public double getCoins() {
        return atomicCoins.get();
    }

    public void setCoins(double amount) {
        atomicCoins.set(amount);
    }


}
