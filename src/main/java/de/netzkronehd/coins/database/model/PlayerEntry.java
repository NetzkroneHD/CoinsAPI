package de.netzkronehd.coins.database.model;

import java.util.UUID;

public record PlayerEntry(UUID uuid, String name, double coins) {

    public static PlayerEntry of(UUID uuid, String name, double coins) {
        return new PlayerEntry(uuid, name, coins);
    }

    public static PlayerEntry of(UuidAndName uuidAndName, double coins) {
        return new PlayerEntry(uuidAndName.uuid(), uuidAndName.name(), coins);
    }
}
