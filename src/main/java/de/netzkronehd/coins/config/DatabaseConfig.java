package de.netzkronehd.coins.config;

import de.netzkronehd.coins.database.Database;
import de.netzkronehd.coins.database.impl.MySQLDriver;
import de.netzkronehd.coins.database.impl.PostgresDriver;
import de.netzkronehd.coins.database.impl.SqlLiteDriver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.Destroyable;
import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatabaseConfig implements Destroyable {

    private String driver;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public Database createDatabase(Path sqlitePath) {
        return switch (driver.toLowerCase()) {
            case "mysql" -> new MySQLDriver();
            case "postgresql" -> new PostgresDriver();
            default -> new SqlLiteDriver(sqlitePath);
        };
    }

    @Override
    public void destroy() {
        this.host = null;
        this.port = 0;
        this.database = null;
        this.username = null;
        this.password = null;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "username='" + username + '\'' +
                ", database='" + database + '\'' +
                ", port=" + port +
                ", host='" + host + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }
}
