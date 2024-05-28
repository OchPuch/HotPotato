package org.ccg.hotpotato.Utils.Item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

public class ClickableItem {
    @Getter
    private static final HashMap<ItemStack, ClickableItem> items = new HashMap<>();
    @Getter
    private static final Multimap<String, ItemStack> gameItemsToDispose = ArrayListMultimap.create();
    @Getter
    private static final Multimap<Player, ItemStack> lobbyItemsToDispose = ArrayListMultimap.create();

    @Getter
    private final ItemStack itemStack;
    @Getter
    private final Consumer<Player> consumer;

    /**
     * For generating items during game
     */
    public static ClickableItem of(ItemStack itemStack, Consumer<Player> consumer, @NotNull String gameId) {
        ClickableItem item = new ClickableItem(itemStack, consumer);
        if (gameId.isEmpty() || gameId.isBlank()) {
            Bukkit.getLogger().severe("Creating item " +itemStack.getType() + " without game id. It will never dispose.");
            return item;
        }
        gameItemsToDispose.put(gameId, item.getItemStack());
        return item;
    }

    /**
     * For generating items not for game
     */
    public static ClickableItem of(ItemStack itemStack,  Consumer<Player> consumer, @NotNull Player player)
    {
        ClickableItem item = new ClickableItem(itemStack, consumer);
        lobbyItemsToDispose.put(player, item.getItemStack());
        return item;
    }

    private ClickableItem(ItemStack itemStack, Consumer<Player> consumer) {
        this.itemStack = itemStack;
        this.consumer = consumer;
        items.put(itemStack, this);
    }
    
    public static void DisposeGameItems(String gameId)
    {
        for(ItemStack itemStack : gameItemsToDispose.get(gameId)) {
            items.remove(itemStack);
            Bukkit.getLogger().severe("Disposing " + itemStack.getType());
        }
        gameItemsToDispose.removeAll(gameId);
    }
    
    public static void DisposePlayerItems(Player player)
    {
        for(ItemStack itemStack : lobbyItemsToDispose.get(player)) {
            items.remove(itemStack);
            Bukkit.getLogger().severe("Disposing " + itemStack.getType());
        }
        lobbyItemsToDispose.removeAll(player);
    }
}
