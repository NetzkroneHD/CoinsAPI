package de.netzkronehd.coins.database.impl;


import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.dependency.Dependency;

import java.nio.file.Path;

public class SqlLiteDriver extends Database {

    private final Path databasePath;

    public SqlLiteDriver(Path databasePath) {
        this.databasePath = databasePath;
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
