package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public interface CoinsSource {

    void save() throws SQLException;

    default void saveAsync() {
        saveAsync(null, null);
    }

    void saveAsync(Consumer<CoinsSource> afterSave, Consumer<SQLException> onError);

    void setCoins(double amount);

    double addCoins(double amount);

    double removeCoins(double amount);

    double getCoins();

    AtomicDouble getCoinsHolder();

    String getName();

    UUID getUniqueId();

}
