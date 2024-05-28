package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTagged;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;

import java.util.Objects;

public class TagPlayersHandler implements Listener, IDisposable {
    private final TagData _data;
    
    public TagPlayersHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }
    
    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }
    
    private void TeleportToSpawn(Player p) {
        p.teleport(_data.getWorldData().getSpawnLocation());
    }

    @EventHandler
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }
    
    @EventHandler
    void onDamage(EntityDamageEvent e) {
        if (_data.getGameState() == TagData.GameState.WaitingForPlayers) return;
        if (e.getEntity() instanceof Player p) {
            if (!_data.getCurrentPlayers().contains(p)) return;
            e.setDamage(.1);
            p.setHealth(20);
            p.setFoodLevel(20);

            switch (e.getCause()) {
                case FALL -> {
                    if (p.getFallDistance() < _data.getConfig().get_noHitFallHeight()) e.setCancelled(true);
                }
                case LAVA, FIRE, FIRE_TICK -> {
                    p.setFireTicks(0);
                    e.setCancelled(true);
                }
                case VOID, WORLD_BORDER -> {
                    TeleportToSpawn(p);
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (_data.getGameState() != TagData.GameState.Running) return;

        if (e.getEntity() instanceof Player receiver) {
            if (!_data.getCurrentPlayers().contains(receiver)) return;

            if (e.getDamager() instanceof Player tagger) {
                if (_data.getRunners().contains(receiver) && _data.getHunters().contains(tagger)) {
                    PlayerTagged taggedEvent = new PlayerTagged(receiver, tagger, e);
                    receiver.getServer().getPluginManager().callEvent(taggedEvent);
                    if (taggedEvent.isCancelled()) return;
                    PlayerTaggedSuccessfully successfulTaggedEvent = new PlayerTaggedSuccessfully(taggedEvent);
                    receiver.getServer().getPluginManager().callEvent(successfulTaggedEvent);
                }
            }

            if (e.getDamager() instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Player tagger) {
                    if (_data.getRunners().contains(receiver) && _data.getHunters().contains(tagger)) {
                        PlayerTagged taggedEvent = new PlayerTagged(receiver, tagger, e, projectile);
                        receiver.getServer().getPluginManager().callEvent(taggedEvent);
                        if (taggedEvent.isCancelled()) return;
                        PlayerTaggedSuccessfully successfulTaggedEvent = new PlayerTaggedSuccessfully(taggedEvent);
                        receiver.getServer().getPluginManager().callEvent(successfulTaggedEvent);
                    }
                }
            }
        }
    }

    
    @EventHandler
    private void onPickupArrow(PlayerPickupArrowEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    private void onPlayerDeath(EntityDeathEvent e)
    {
        if (_data.getGameState() == TagData.GameState.WaitingForPlayers) return;
        if (e.getEntity() instanceof Player p) {
            if (!_data.getCurrentPlayers().contains(p)) return;
            e.setCancelled(true);
            TeleportToSpawn(p);
        }
    }

    @EventHandler
    void onPickup(PlayerAttemptPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
}
