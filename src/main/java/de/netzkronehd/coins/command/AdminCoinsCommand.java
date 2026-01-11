package de.netzkronehd.coins.command;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.locale.Args;
import de.netzkronehd.coins.message.model.UpdateType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static de.netzkronehd.coins.locale.MessageUtils.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class AdminCoinsCommand extends Command {

    private static final Args.Args0 NO_PERMISSIONS = () -> translatable()
            .key("netzcoins.command.admincoins.no-permissions")
            .arguments(prefix()).build();

    private static final Args.Args1<String> USAGE = (commandLabel) -> translatable()
            .key("netzcoins.command.admincoins.usage")
            .arguments(prefix(), text(commandLabel)).build();

    private static final Args.Args1<String> SOURCE_NOT_FOUND = (name) -> translatable()
            .key("netzcoins.command.admincoins.source-not-found")
            .arguments(prefix(), text(name)).build();

    private static final Args.Args2<String, SQLException> FAILED_UPDATE = (name, ex) -> translatable()
            .key("netzcoins.command.admincoins.failed-update")
            .arguments(prefix(), text(name), text(ex.toString())).build();

    private static final Args.Args4<String, String, String, UpdateType> SUCCESS_UPDATE = (name, amount, currencyName, type) -> translatable()
            .key("netzcoins.command.admincoins.success-update")
            .arguments(prefix(), text(name), text(amount), text(currencyName), text(type.name())).build();


    private final CoinsPlugin plugin;

    public AdminCoinsCommand(CoinsPlugin plugin) {
        super("admincoins", "", "", List.of("admincoin", "netzadmincoins", "nac", "economyadmin", "setcoins", "addcoins", "removecoins"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("netzcoins.command.admincoins")) {
            NO_PERMISSIONS.send(sender);
            return true;
        }

        if(args.length != 3) {
            USAGE.send(sender, commandLabel);
            return true;
        }

        try {
            final var updateType = UpdateType.valueOf(args[0].toUpperCase());
            final var playerName = args[1];
            final double amount = Double.parseDouble(args[2]);

            plugin.getCoinsApi().getSource(playerName).ifPresentOrElse(source -> {
                updateType.apply(source, amount);
                source.saveAsync(
                        (updatedSource) -> SUCCESS_UPDATE.send(sender, updatedSource.getName(), plugin.getCoinsApi().formatCoins(amount), plugin.getCoinsApi().formatCurrencyName(amount), updateType),
                        (ex) -> {
                            FAILED_UPDATE.send(sender, source.getName(), ex);
                            throw new RuntimeException(ex);
                        });
            }, () -> SOURCE_NOT_FOUND.send(sender, playerName));

        } catch (IllegalArgumentException e) {
            USAGE.send(sender, commandLabel);
            return true;
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if(!sender.hasPermission("netzcoins.command.admincoins")) {
            return List.of();
        }
        if(args.length == 1) {
            final var prefix = args[0].toLowerCase();
            return Arrays.stream(UpdateType.values()).map(UpdateType::name)
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .toList();
        } else if(args.length == 2) {
            final var prefix = args[1].toLowerCase();
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .toList();
        }
        return List.of();
    }
}
