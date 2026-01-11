package de.netzkronehd.coins.database.impl;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.dependency.Dependency;

import java.nio.file.Path;

public class SqlLiteDriver extends Database {

    private final Path databasePath;

    public SqlLiteDriver(Path databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public void loadDataSource(String host, int port, String database, String user, String password, int maximumPoolSize, int minimumIdle) {
        var cfg = new HikariConfig();
        cfg.setJdbcUrl(getJdbcUrl(host, port, database));
        cfg.setUsername(user);
        cfg.setPassword(password);
        cfg.setMaximumPoolSize(1);
        cfg.setMinimumIdle(0);
        cfg.setAutoCommit(true);
        cfg.setPoolName("netzcoinsapi-"+getName().toLowerCase());
        this.dataSource = new HikariDataSource(cfg);
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
}
