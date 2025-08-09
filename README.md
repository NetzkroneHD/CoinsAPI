# NetzCoinsApi

This is a Minecraft plugin that allows you to create and manage coins in your Minecraft server. 
It provides a simple API for developers to interact with coins, including creating, deleting, and modifying coins.

## How to use

1. Download the CoinsAPI plugin from the releases page.
2. Place the plugin jar file in your server's `plugins` directory.
3. Start your server.
4. Use the provided API to create and manage coins in your plugin.

## For developers

To use the CoinsAPI in your plugin, you need to add it as a dependency in your `plugin.yml` file:

```yaml
depend: [CoinsAPI]
```

### Api structure

The CoinsAPI provides a simple interface to interact with coins. You can access the API through the `CoinsPlugin` class.
There are 2 Coins sources available:
- `CacheCoinsSource`: Represents the state of coins of a player in memory.
- `PlayerCoinsSource`: Represents the state of coins of an online player.

The class `CoinsCache` is used to manage the coins of all players in the server and in memory.

**After modifying the coins of a source, you should call `saveAsync()` to persist the changes to the database.**

### How to add coins

To add coins to a player, you can use the following code snippet:

```java

import de.netzkronehd.coins.api.CoinsApi;
import org.bukkit.entity.Player;

void example(Player player) {
    CoinsApi api = CoinsPlugin.getInstance().getCoinsApi();
    api.getSource(p.getUniqueId()).ifPresent(coinsSource -> {
        coinsSource.addCoins(10);
        coinsSource.saveAsync();
    });
}
```
