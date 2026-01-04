package de.netzkronehd.coins.database.impl;


import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.dependency.Dependency;

public class MySQLDriver extends Database {

    public MySQLDriver() {
    }

    @Override
    public String getJdbcUrl(String host, int port, String database) {
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
    }

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public String getClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public Dependency getDependency() {
        return Dependency.MYSQL;
    }
}
