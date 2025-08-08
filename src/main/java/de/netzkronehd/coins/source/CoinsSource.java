package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.UUID;

public interface CoinsSource {

    void save();

    void saveAsync();

    void setCoins(double amount);

    double addCoins(double amount);

    double removeCoins(double amount);

    double getCoins();

    AtomicDouble getCoinsHolder();

    String getName();

    UUID getUniqueId();

}
