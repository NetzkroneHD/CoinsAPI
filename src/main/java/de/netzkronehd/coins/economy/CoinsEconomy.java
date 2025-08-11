package de.netzkronehd.coins.economy;

import de.netzkronehd.coins.CoinsPlugin;
import de.netzkronehd.coins.api.CoinsApi;
import de.netzkronehd.coins.source.CoinsSource;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.sql.SQLException;
import java.util.List;

import static java.util.Collections.emptyList;
import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.*;

public class CoinsEconomy extends AbstractEconomy {

    private final CoinsPlugin plugin;
    private final CoinsApi coinsApi;

    public CoinsEconomy(CoinsPlugin plugin, CoinsApi coinsApi) {
        this.plugin = plugin;
        this.coinsApi = coinsApi;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return coinsApi.getConfig().getEconomyName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        // IEEE 754 standard for double precision floating point numbers
        return 15;
    }

    @Override
    public String format(double amount) {
        return coinsApi.getConfig().getDecimalFormat().format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return coinsApi.getConfig().getCurrencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return coinsApi.getConfig().getCurrencyNameSingular();
    }

    @Override
    public boolean hasAccount(String playerName) {
        return coinsApi.getSource(playerName).isPresent();
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName) {
        return coinsApi.getSource(playerName).map(CoinsSource::getCoins).orElse(0D);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return coinsApi.getSource(playerName)
                .map(source -> source.getCoins() >= amount)
                .orElse(false);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return coinsApi.getSource(playerName)
                .map(coinsSource -> {
                    final var newBalance = coinsSource.removeCoins(amount);
                    coinsSource.saveAsync();
                    return new EconomyResponse(amount, newBalance, SUCCESS, "");
                })
                .orElseGet(() -> new EconomyResponse(0, 0, FAILURE, "Could not find any source for name "+playerName));
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return coinsApi.getSource(playerName)
                .map(coinsSource -> {
                    final var newBalance = coinsSource.addCoins(amount);
                    coinsSource.saveAsync();
                    return new EconomyResponse(amount, newBalance, SUCCESS, "");
                })
                .orElseGet(() -> new EconomyResponse(0, 0, FAILURE, "Could not find any source for name "+playerName));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, NOT_IMPLEMENTED, "Banking is not supported.");
    }

    @Override
    public List<String> getBanks() {
        return emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        throw new UnsupportedOperationException("Creating player accounts with only names is not supported.");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        try {
            return coinsApi.createCacheSource(player.getUniqueId(), player.getName(), 0).isPresent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        throw new UnsupportedOperationException("Creating player accounts with only names is not supported.");
    }

    public void register() {
        plugin.getServer().getServicesManager().register(Economy.class, this, plugin, ServicePriority.Normal);
    }
}
