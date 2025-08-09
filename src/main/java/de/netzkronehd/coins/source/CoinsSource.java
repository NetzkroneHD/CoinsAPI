package de.netzkronehd.coins.source;

import com.google.common.util.concurrent.AtomicDouble;
import org.jetbrains.annotations.ApiStatus;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public interface CoinsSource {

    /**
     * Saves the current state of the CoinsSource to the database.
     * This method is synchronous and may block the calling thread.
     *
     * @throws SQLException if an error occurs while saving to the database.
     */
    void save() throws SQLException;

    /**
     * Saves the current state of the CoinsSource asynchronously.
     * This method is a convenience method that calls {@link #saveAsync(Consumer, Consumer)}
     * with null callbacks for afterSave and onError.
     * It is intended for use when you do not need to handle the completion or error of the save operation.
     */
    default void saveAsync() {
        saveAsync(null, null);
    }

    /**
     * Saves the current state of the CoinsSource asynchronously.
     *
     * @param afterSave Callback to execute after saving, can be null.
     * @param onError   Callback to execute if an error occurs, can be null.
     */
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
