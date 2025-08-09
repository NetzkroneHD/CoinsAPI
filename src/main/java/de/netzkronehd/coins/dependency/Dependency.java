package de.netzkronehd.coins.dependency;

import lombok.Getter;

import java.util.Arrays;
import java.util.Base64;

@Getter
public enum Dependency {

    // Remote databases
    MYSQL(
            "com{}mysql",
            "mysql-connector-j",
            "9.4.0",
            "Se2TyLK+qcsJKbhaiiiDexkdD46saRn9zvFuNuLNU7M=",
            "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL(
            "org{}postgresql",
            "postgresql",
            "42.7.7",
            "FXlj1grmbWB+CUZujAzfgIfpyyDQFZiZ/8qWvKJShGA=",
            "org.postgresql.Driver"),

    // Local databases
    SQLITE(
            "org{}xerial",
            "sqlite-jdbc",
            "3.50.3.0",
            "o/U6KqFa6UJannk7vpyOUoj+vrS2XvXBpOgNTCBFzwg=",
            "org.sqlite.JDBC");

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    private final String mavenRepoPath;
    private final String version;
    private final byte[] checksum;
    private final String initialClassDriver;

    Dependency(String groupId, String artifactId, String version, String checksum, String initialClassDriver) {
        this.initialClassDriver = initialClassDriver;
        this.mavenRepoPath = String.format(MAVEN_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(artifactId),
                version,
                rewriteEscaping(artifactId),
                version
        );
        this.version = version;
        this.checksum = Base64.getDecoder().decode(checksum);
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public String getFileName() {
        final String name = name().toLowerCase().replace('_', '-');
        return name + "-" + this.version + ".jar";
    }

    public boolean checksumMatches(byte[] hash) {
        return Arrays.equals(this.checksum, hash);
    }

}
