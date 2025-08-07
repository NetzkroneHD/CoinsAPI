package de.netzkronehd.coins.database.model;

import java.util.UUID;

public record UuidAndName(UUID uuid, String name) {

    public static UuidAndName of(UUID uuid, String name) {
        return new UuidAndName(uuid, name);
    }

}
