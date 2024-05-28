package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Utils.BlockFinder;
import org.ccg.hotpotato.Utils.Item.ItemUtil;

import java.util.Objects;

public class TagReturnHeightItemsHandler implements Listener, IDisposable {

    private final TagData _data;

    public TagReturnHeightItemsHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }

    @EventHandler
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }

    @EventHandler
    private void onPlayerFall(PlayerMoveEvent e) {
        if (_data.getGameState() != TagData.GameState.Running) return;
        if (!_data.getHunters().contains(e.getPlayer())) return;
        var fall = e.getPlayer().getFallDistance();
        boolean hasHook = e.getPlayer().getInventory().contains(_data.getItems().HOOK_ItemStack);
        boolean hasFeather = e.getPlayer().getInventory().contains(_data.getItems().GenerateClickableFeather.getItemStack());
        if (hasFeather) return;
        if (fall >= _data.getConfig().get_giveHookFallDistance() && !hasHook) {
            e.getPlayer().getInventory().addItem(_data.getItems().HOOK_ItemStack);
        }

        if (fall >= _data.getConfig().get_giveUltraFeatherFallDistance()) {
            e.getPlayer().getInventory().addItem(_data.getItems().GenerateClickableFeather.getItemStack());
            if (hasHook) e.getPlayer().getInventory().remove(_data.getItems().HOOK_ItemStack);
        }
    }

    @EventHandler
    private void onHook(PlayerFishEvent e) {
        var p = e.getPlayer();
        if (!_data.getCurrentPlayers().contains(p)) return;
        boolean canHook = false;
        var hookLocation = e.getHook().getLocation();
        switch (e.getState()) {
            case REEL_IN -> {
                if (BlockFinder.HaveSolidBlockInRadius(hookLocation, _data.getConfig().get_hookCheckForSolidBlockRadius())) {
                    canHook = true;
                }
            }
            case IN_GROUND -> {
                canHook = true;
            }
        }

        if (!canHook) return;
        p.setVelocity(hookLocation.toVector().subtract(p.getLocation().toVector()).multiply(_data.getConfig().get_hookVelocity()).setY(_data.getConfig().get_hookBaseVelocityY()));
        ItemUtil.clear(p, Material.FISHING_ROD);
    }
}
