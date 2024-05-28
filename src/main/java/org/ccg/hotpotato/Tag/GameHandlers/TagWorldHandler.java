package org.ccg.hotpotato.Tag.GameHandlers;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.packet.ParticlePacket;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.*;

import java.util.Objects;

public class TagWorldHandler implements Listener, IDisposable {
    private final TagData _data;
    int _secondsBeforeDeletingBlocks;
    private ParticleNativeAPI particleApi;


    public TagWorldHandler(TagData data) {
        _data = data;
        particleApi = ParticleNativeCore.loadAPI(HotPotato.getInstance());
        HotPotato.register(this);
        _secondsBeforeDeletingBlocks = _data.getConfig().get_deletePlacedBlocksTime();
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
    private void onGameStartPreparing(TagGameStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _data.getWorldData().getWorld().setTime(0);
        _data.getWorldData().getWorld().setStorm(false);
        _data.getWorldData().getWorld().setThundering(false);
    }

    @EventHandler
    private void onRoundStart(TagRoundStarted e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _data.setSecondsBeforeDeletingBlocks(_data.getConfig().get_deletePlacedBlocksTime());
    }

    @EventHandler
    private void onRoundStartPreparing(TagRoundStartedPreparing e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _data.getWorldData().getWorld().setTime(0);
        _data.getWorldData().getWorld().setStorm(false);
        _data.getWorldData().getWorld().setThundering(false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onSecondPassed(GameSecondPassed e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        if (_data.getGameState() != TagData.GameState.Running) return;
        UpdateTimeBeforeDeletingBlocks();
        if (_data.getGameState() != TagData.GameState.Running) return;
        if (_data.getSecondsBeforeDeletingBlocks() == 0) {
            for (int i = 0; i < _data.getWorldData().getBlocksToDelete().size(); i++) {
                Block block = _data.getWorldData().getBlocksToDelete().get(i);
                _data.getWorldData().getPlacedBlocks().remove(block);
                block.setType(Material.AIR);
            }
            _data.getWorldData().getBlocksToDelete().clear();
        }
    }

    
    private void UpdateTimeBeforeDeletingBlocks() {
        _secondsBeforeDeletingBlocks--;
        if (_secondsBeforeDeletingBlocks < 0) {
            _secondsBeforeDeletingBlocks = _data.getConfig().get_deletePlacedBlocksTime();
        }
        _data.setSecondsBeforeDeletingBlocks(_secondsBeforeDeletingBlocks);
    }
    
//region Drawing for each player duh?
    
    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        if (!_data.getCurrentPlayers().contains(e.getPlayer())) return;

        DrawParticlesForBorder(e.getPlayer());

        if (_data.getWorldData().IsOutsideSecondSafeZone(e.getFrom())) {
            e.getPlayer().teleport(_data.GetRandomHunter());
            return;
        }
        if (_data.getWorldData().IsOutsideSafeZone(e.getTo())) {
            Vector playerVelocity = e.getTo().toVector().clone().subtract(e.getFrom().toVector());
            e.getPlayer().teleport(e.getFrom().subtract(playerVelocity.clone().setY(0).normalize()));
            e.setCancelled(true);
        }
        if (_data.getWorldData().IsOutsideBorder(e.getTo())) {
            var playerVelocity = e.getPlayer().getVelocity();
            if (_data.getWorldData().IsOutsideBorderByX(e.getTo())) {
                playerVelocity.setX(_data.getConfig().get_borderBoost() * Math.signum(-playerVelocity.getX()));
            } else {
                playerVelocity.setZ(_data.getConfig().get_borderBoost() * Math.signum(-playerVelocity.getZ()));
            }
            e.getPlayer().setVelocity(playerVelocity);
        }
    }

    private void DrawParticlesForBorder(Player player) {
        int howClose = 5;
        if (_data.getWorldData().CloseToDownX(player.getLocation(), howClose)) {
            DrawLine(_data.getWorldData().GetBorderDownXDownZ(), _data.getWorldData().GetBorderDownXTopZ(), player);
        } else if (_data.getWorldData().CloseToUpX(player.getLocation(), howClose)) {
            DrawLine(_data.getWorldData().GetBorderTopXDownZ(), _data.getWorldData().GetBorderTopXTopZ(), player);
        }
        if (_data.getWorldData().CloseToDownZ(player.getLocation(), howClose)) {
            DrawLine(_data.getWorldData().GetBorderTopXDownZ(), _data.getWorldData().GetBorderDownXDownZ() ,player);
        } else if (_data.getWorldData().CloseToUpZ(player.getLocation(), howClose)) {
            DrawLine(_data.getWorldData().GetBorderTopXTopZ(), _data.getWorldData().GetBorderDownXTopZ(), player);
        }
    }

    private void DrawLine(Location loc1, Location loc2, Player player)
    {
        DrawLine(loc1, loc2, player,20);
    }
    
    
    //endregion

    
    private void DrawLine(Location loc1, Location loc2, Player player, int duration)
    {
        double space = 0.4f;
        loc1.setY(player.getY() + 1);
        loc2.setY(player.getY() + 1);
        double distance = loc1.distance(loc2);
        Vector p1 = loc1.toVector();
        Vector p2 = loc2.toVector();
        Vector delta = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        while (length < distance) {
            //player.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1);
            particleApi.LIST_1_8.REDSTONE.
                    packet(true, p1, 1, duration).sendInRadiusTo(player, 10);
            length += space;
            p1.add(delta);
        }

    }
    
}