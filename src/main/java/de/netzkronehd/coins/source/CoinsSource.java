package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import org.jetbrains.annotations.ApiStatus;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public interface CoinsSource {

    void save() throws SQLException;

    default void saveAsync() {
        saveAsync(null, null);
    }

    void saveAsync(Consumer<CoinsSource> afterSave, Consumer<SQLException> onError);

    double setCoins(double amount);

    double addCoins(double amount);

    double removeCoins(double amount);

    double getCoins();

    /**
     * @deprecated Use {@link #getCoins()} instead.
     * Editing this method directly is not recommended and may lead to unexpected behavior.
     */
    @Deprecated
    @ApiStatus.Internal
    AtomicDouble getCoinsHolder();

    String getName();

    UUID getUniqueId();

}
