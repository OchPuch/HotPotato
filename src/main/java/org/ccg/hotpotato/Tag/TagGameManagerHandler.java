package org.ccg.hotpotato.Tag;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;


import java.util.Objects;

public class TagGameManagerHandler implements Listener, IDisposable {

    public TagGameManagerHandler() {
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }
    
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (TagGameManager.disconnectedFromTagPlayers.containsKey(player.getName())) {
            for (TagData game : TagGameManager.currentGames) {
                if (Objects.equals(game.getGameID(), TagGameManager.disconnectedFromTagPlayers.get(player.getName()))) {
                    return;
                }
            }
        }
        
        PrepareBasicPlayer(player);
    }

    private void PrepareBasicPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setVelocity(player.getVelocity().setY(0));
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.setCollidable(true);
        player.setInvulnerable(false);
        player.setInvisible(false);
        player.closeInventory();
        
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        //TODO: give Stuff for choosing stuff


    }

}
