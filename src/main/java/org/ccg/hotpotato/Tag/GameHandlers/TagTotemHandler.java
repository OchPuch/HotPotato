package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.GameSecondPassed;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStarted;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTagged;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;
import org.joml.Math;

import java.util.HashMap;
import java.util.Objects;

public class TagTotemHandler implements Listener, IDisposable {

    private final HashMap<Player, Integer> _runnersTime = new HashMap<>(); 
    
    private final TagData _data;

    public TagTotemHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }
    
    @EventHandler
    private void onGameStartPreparing(TagGameStartedPreparing e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player p : _data.getCurrentPlayers()) {
            _runnersTime.put(p, 0);
        }
    }
    
    @EventHandler
    private void onRoundStarted(TagRoundStarted e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player p : _data.getCurrentPlayers()) {
            _runnersTime.put(p, 0);
        }
    }
    
    @EventHandler
    private void onSecond(GameSecondPassed e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player p : _data.getRunners()) {
            if (_runnersTime.containsKey(p)) {
                _runnersTime.put(p, _runnersTime.get(p) + 1);
            }
            
            if (_runnersTime.get(p) >= _data.getConfig().get_totemCooldown()) {
                p.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_AXE_WAX_OFF, 1, 1);
                _runnersTime.put(p, 0);
            }
        }
    }
    
    @EventHandler
    private void onTagSuccess(PlayerTaggedSuccessfully e)
    {
        if (_runnersTime.containsKey(e.get_receiver())) {
            _runnersTime.put(e.get_receiver(), 0);
        }
    }

    @EventHandler
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onTag(PlayerTagged e) {
        if (e.isCancelled()) return;
        if (!_data.getCurrentPlayers().contains(e.get_receiver()) || !_data.getCurrentPlayers().contains(e.get_tagger()))
            return;
        boolean hasTotemInRightHand = e.get_receiver().getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING;
        boolean hasTotemInLeftHand = e.get_receiver().getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
        if (hasTotemInRightHand || hasTotemInLeftHand) {

            //remove totem
            if (hasTotemInRightHand) e.get_receiver().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            else e.get_receiver().getInventory().setItemInOffHand(new ItemStack(Material.AIR));

            //model
            e.setCancelled(true);
            if (_data.getConfig().is_totemCancelDamageEvent()) e.get_damageEvent().setCancelled(true);
            e.get_damageEvent().setDamage(Math.clamp(e.get_damageEvent().getDamage(), 0, _data.getConfig().get_totemMaxDamage()));
            e.get_receiver().setVelocity(new Vector(0, _data.getConfig().get_totemVelocityUpOnActivation(), 0));

            //View
            e.get_receiver().getWorld().spawnParticle(Particle.TOTEM, e.get_receiver().getLocation(), 100);
            e.get_receiver().getWorld().playSound(e.get_receiver().getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        }
    }

}
