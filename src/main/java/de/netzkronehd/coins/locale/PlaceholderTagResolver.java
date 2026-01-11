package de.netzkronehd.coins.locale;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class PlaceholderTagResolver {

    public static TagResolver papiTag(Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            // Get the placeholder name inside the tag (e.g., "player_name" from <papi:player_name>)
            String placeholder = argumentQueue.popOr("papi tag requires an argument").value();

            // Parse it with PlaceholderAPI
            String parsed = PlaceholderAPI.setPlaceholders(player, "%" + placeholder + "%");

            // Return it as a parsed component (allows colors inside the placeholder to work)
            return Tag.selfClosingInserting(LegacyComponentSerializer.legacySection().deserialize(parsed));
        });
    }

}
