package de.netzkronehd.coins.database.impl;


import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.dependency.Dependency;

public class PostgresDriver extends Database {

    @Override
    public String getJdbcUrl(String host, int port, String database) {
        return "jdbc:postgresql://" + host + ":" + port + "/" + database + "?reWriteBatchedInserts=true";
    }

    @Override
    public String getName() {
        return "PostgreSQL";
    }

    @Override
    public String getClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public Dependency getDependency() {
        return Dependency.POSTGRESQL;
    }
}
