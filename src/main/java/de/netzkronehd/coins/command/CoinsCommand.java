package de.netzkronehd.coins.command;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.locale.Args;
import de.netzkronehd.coins.source.CoinsSource;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.netzkronehd.coins.locale.MessageUtils.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class CoinsCommand extends Command {

    private static final Args.Args0 NO_PERMISSIONS = () -> translatable()
            .key("netzcoins.command.coins.no-permissions")
            .arguments(prefix()).build();

    private static final Args.Args0 ONLY_PLAYERS = () -> translatable()
            .key("netzcoins.command.coins.only-players")
            .arguments(prefix()).build();

    private static final Args.Args1<String> PLAYER_NOT_FOUND = (playerName) -> translatable()
            .key("netzcoins.command.coins.player-not-found")
            .arguments(prefix(), text(playerName)).build();

    private static final Args.Args1<String> USAGE = (commandLabel) -> translatable()
            .key("netzcoins.command.coins.usage")
            .arguments(prefix(), text(commandLabel)).build();

    private static final Args.Args2<String, String> OWN_COINS_BALANCE = (coins, currencyName) -> translatable()
            .key("netzcoins.command.coins.own-balance")
            .arguments(prefix(), text(coins), text(currencyName)).build();

    private static final Args.Args3<CoinsSource, String, String> OTHER_COINS_BALANCE = (source, coins, currencyName) -> translatable()
            .key("netzcoins.command.coins.other-balance")
            .arguments(prefix(), text(source.getName()), text(coins), text(currencyName)).build();


    private final CoinsPlugin plugin;

    public CoinsCommand(CoinsPlugin plugin) {
        super("coins", "", "", List.of("coin", "netzcoins", "nc", "money", "balance", "bal"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if (notHasPermissions(sender, "netzcoinsapi.coins")) {
            return true;
        }
        if(args.length == 0) {
            handleSingleCommand(sender);
        } else if (args.length == 1) {
            handleOtherCommand(sender, args);
        } else {
            USAGE.send(sender, commandLabel);
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if(args.length == 1 && sender.hasPermission("netzcoinsapi.coins.other")) {
            final var prefix = args[0].toLowerCase();
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .toList();
        }
        return List.of();
    }

    private void handleSingleCommand(CommandSender sender) {
        if(!(sender instanceof final Player player)) {
            ONLY_PLAYERS.send(sender);
            return;
        }
        plugin.getCoinsApi().getSource(player.getUniqueId()).ifPresentOrElse(
                source -> OWN_COINS_BALANCE.send(sender, String.valueOf(source.getCoins()), plugin.getCoinsApi().formatCurrencyName(source.getCoins())),
                () -> PLAYER_NOT_FOUND.send(sender, player.getName())
        );
    }

    private void handleOtherCommand(CommandSender sender, String[] args) {
        if (notHasPermissions(sender, "netzcoinsapi.coins.other")) {
            return;
        }
        plugin.getCoinsApi().getSource(args[0]).ifPresentOrElse(
                source -> OTHER_COINS_BALANCE.send(sender, source, String.valueOf(source.getCoins()), plugin.getCoinsApi().formatCurrencyName(source.getCoins())),
                () -> PLAYER_NOT_FOUND.send(sender, args[0])
        );

    }

    private boolean notHasPermissions(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            NO_PERMISSIONS.send(sender);
            return true;
        }
        return false;
    }

}
