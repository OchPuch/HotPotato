package org.ccg.hotpotato.Global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Utils.Item.ClickableItem;

public class GlobalHandler implements Listener {
    @EventHandler
    private void onHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player p) {
            e.setCancelled(true);
            p.setFoodLevel(20);
            p.setSaturation(20);
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {
        var p = e.getPlayer();
        if (e.getAction() != Action.PHYSICAL && e.getItem() != null) {
            var a = e.getAction().isLeftClick()
                    ? ItemClickEvent.Action.LEFT_CLICK : ItemClickEvent.Action.RIGHT_CLICK;
            var event = new ItemClickEvent(p, a, e.getItem());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) e.setCancelled(true);
        }
    }

    @EventHandler
    void onItemClick(ItemClickEvent e) {
        if (e.getAction() == ItemClickEvent.Action.RIGHT_CLICK) ClickableItem.getItems().forEach((item, ci) -> {
            if (item.isSimilar(e.getItemStack())) {
                ci.getConsumer().accept(e.getPlayer());
                e.setCancelled(true);
            }
        });
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onGameEnd(TagGameEnded e)
    {
        //Dispose game items for Clickable Items
        Bukkit.getLogger().severe("Total dispose items count on game End " + ClickableItem.getGameItemsToDispose().get(e.getTagData().getGameID()).size());
        ClickableItem.DisposeGameItems(e.getTagData().getGameID());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onGameStartedPreparing(TagGameStartedPreparing e)
    {
        Bukkit.getLogger().severe("Global disposing lobby items");
        //Dispose lobby items from Clickable items for joined players
        for(String playerName : e.getTagData().getJoinedPlayersNames()) {
            Player player = Bukkit.getPlayer(playerName);
            ClickableItem.DisposePlayerItems(player);
        }
            
    }
    
}
