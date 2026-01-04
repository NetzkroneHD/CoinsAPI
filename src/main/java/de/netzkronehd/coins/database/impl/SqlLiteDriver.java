package de.netzkronehd.coins.database.impl;


import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.dependency.Dependency;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Properties;

public class SqlLiteDriver extends Database {

    private final Path databasePath;
    private Connection connection;

    public SqlLiteDriver(Path databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public void loadDataSource(String host, int port, String database, String user, String password, int maximumPoolSize, int minimumIdle) {
        try {
            connection = createConnection();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create SQLite connection.", ex);
        }
    }

    public Connection createConnection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if (driverClass == null) {
            throw new IllegalStateException("ClassLoader is not set.");
        }
        if (!Files.exists(databasePath)) {
            Files.createFile(databasePath);
        }
        final Method createConnection = driverClass.getMethod("createConnection", String.class, Properties.class);
        createConnection.setAccessible(true);
        return (Connection) createConnection.invoke(driverClass, "jdbc:sqlite:" + databasePath.toFile().getAbsolutePath(), new Properties());
    }

    @Override
    public String getJdbcUrl(String host, int port, String database) {
        return "jdbc:sqlite:"+databasePath.toFile().getAbsolutePath();
    }

    @Override
    public String getName() {
        return "SQLite";
    }

    @Override
    public String getClassName() {
        return "org.sqlite.JDBC";
    }

    @Override
    public Dependency getDependency() {
        return Dependency.SQLITE;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
