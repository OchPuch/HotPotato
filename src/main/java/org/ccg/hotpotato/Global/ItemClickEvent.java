package org.ccg.hotpotato.Global;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.ccg.hotpotato.Utils.Item.ItemUtil;
import org.jetbrains.annotations.NotNull;

@Getter
public class ItemClickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final Action action;

    private final ItemStack itemStack;

    @Setter
    private boolean cancelled;

    public ItemClickEvent(Player p, Action a, ItemStack is) {
        player = p;
        action = a;
        itemStack = is;
    }

    public ItemStack getClickedItem() {
        return itemStack;
    }

    public boolean isSimilar(ItemStack is) {
        return ItemUtil.isSimilar(itemStack, is);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Action {
        LEFT_CLICK,
        RIGHT_CLICK
    }

}