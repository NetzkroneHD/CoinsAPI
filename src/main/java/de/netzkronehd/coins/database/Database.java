package de.netzkronehd.coins.database;

import de.netzkronehd.coins.config.DatabaseConfig;
import de.netzkronehd.coins.database.model.UuidAndName;
import de.netzkronehd.coins.dependency.Dependency;
import de.netzkronehd.coins.dependency.DependencyManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class Database {

    protected Connection connection;
    protected Class<?> driverClass;

    public void loadDriverClass(DependencyManager dependencyManager) {
        dependencyManager.getClassLoader(getDependency()).ifPresentOrElse(
                driverClass -> this.driverClass = driverClass,
                () -> {
                    throw new IllegalStateException("Dependency not loaded: " + getDependency().name());
                }
        );
    }

    public void connect(DatabaseConfig config) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, IOException {
        connect(config.getHost(), config.getPort(), config.getDatabase(), config.getUsername(), config.getPassword());
    }

    public void connect(String host, int port, String database, String user, String password) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, IOException {
        connection = createConnection(host, port, database, user, password);
    }

    public abstract Connection createConnection(String host, int port, String database, String user, String password) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException;

    public void createTables() throws SQLException {
        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS coinsapi_players
                (
                    player_uniqueId VARCHAR(36) PRIMARY KEY,
                    player_name     VARCHAR(16) NOT NULL,
                    coins           DOUBLE DEFAULT 0
                )
                """).executeUpdate();

    }

    public int insertPlayer(UUID uuid, String playerName) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("""
                INSERT INTO coinsapi_players (player_uniqueId, player_name)
                VALUES (?, ?, ?)
                """);
        ps.setString(1, uuid.toString());
        ps.setString(2, playerName);
        return ps.executeUpdate();
    }

    public int insertOrUpdatePlayer(UUID uuid, String playerName) throws SQLException {
        if (playerExists(uuid)) {
            return setName(uuid, playerName);
        } else {
            return insertPlayer(uuid, playerName);
        }
    }

    public boolean playerExists(UUID uuid) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("""
                SELECT player_uniqueId
                FROM coinsapi_players
                WHERE player_uniqueId = ?
                """);
        ps.setString(1, uuid.toString());
        return ps.executeQuery().next();
    }

    public void setCoins(UUID uuid, double coins) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("UPDATE coinsapi_players SET coins = ? WHERE player_uniqueId = ?");
        ps.setDouble(1, coins);
        ps.setString(2, uuid.toString());
        ps.executeUpdate();
    }

    public int setName(UUID uuid, String playerName) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("UPDATE coinsapi_players SET player_name = ? WHERE player_uniqueId = ?");
        ps.setString(1, playerName);
        ps.setString(2, uuid.toString());
        return ps.executeUpdate();
    }

    public Optional<UuidAndName> getUuid(String playerName) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT player_uniqueId, player_name FROM coinsapi_players WHERE LOWER(player_name) = ?");
        ps.setString(1, playerName.toLowerCase());
        final ResultSet rs = ps.executeQuery();
        if (!rs.next()) return Optional.empty();
        return Optional.of(UuidAndName.of(UUID.fromString(rs.getString("player_uniqueId")), rs.getString("player_name")));
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public void close() throws SQLException {
        if (connection == null) return;
        connection.close();
    }

    public abstract String getName();

    public abstract String getClassName();

    public abstract Dependency getDependency();
}
