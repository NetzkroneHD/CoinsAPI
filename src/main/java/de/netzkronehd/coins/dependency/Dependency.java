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
            "9.6.0",
            "Zt8dRTeJ3Iy3WafcF/WGRok78oSD8mIyhlDxcEcqbq0=",
            "com.mysql.cj.jdbc.Driver"),
    POSTGRESQL(
            "org{}postgresql",
            "postgresql",
            "42.7.10",
            "yrHNZ8+iXCXeQ0jlMimAKCiKh3ugHHfRYZ/kVBYZM4c=",
            "org.postgresql.Driver"),

    // Local databases
    SQLITE(
            "org{}xerial",
            "sqlite-jdbc",
            "3.51.3.0",
            "NoSsXpvYk8vmjrCsFwg0P2a+gpsdK1qzF+eD0FrNWu8=",
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
