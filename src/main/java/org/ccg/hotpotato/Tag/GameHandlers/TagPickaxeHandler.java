package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagRoundStartedPreparing;
import org.ccg.hotpotato.Tag.Items;
import org.ccg.hotpotato.Utils.Cooldown.PlayerCooldown;

import java.util.Objects;

public class TagPickaxeHandler implements Listener, IDisposable {
    
    private final TagData _data;
    private static final int netheritePickaxeDurability = 2031;

    public TagPickaxeHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        Bukkit.getLogger().severe("Disposing pickaxe");
        HotPotato.unregister(this);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onGameStartPreparing(TagGameStartedPreparing e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            player.getInventory().addItem(_data.getItems().PICKAXE_ItemStack);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onRoundStartPreparing(TagRoundStartedPreparing e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            player.getInventory().addItem(_data.getItems().PICKAXE_ItemStack);
        }
    }

    @EventHandler
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }
    
    
    @EventHandler
    private void onDamageEntity(EntityDamageByEntityEvent e)
    {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!_data.getCurrentPlayers().contains(player)) return;
        if (player.getInventory().getItemInMainHand().getType() == Items.PICKAXE_MATERIAL) {
            var damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
            float damagePercent = player.getFallDistance() / (float) _data.getConfig().get_pickaxeMaxHeightBeforeBreakOnFallingHit();
            int damage = (int) (damagePercent * netheritePickaxeDurability);
            ApplyDamageToPickaxe(player, damageable, damage);
            player.setVelocity(new Vector(player.getVelocity().getX(), player.getFallDistance() * _data.getConfig().get_pickaxeBoostMultiplier(), player.getVelocity().getZ()));
        }
    }
    
    @EventHandler
    private void onBlockBreak(BlockBreakEvent e)
    {
        var p = e.getPlayer();
        if (!_data.getCurrentPlayers().contains(p)) return;
        if (p.getInventory().getItemInMainHand().getType() == Items.PICKAXE_MATERIAL) {
            var damageable = (Damageable) p.getInventory().getItemInMainHand().getItemMeta();
            int damage = netheritePickaxeDurability/ _data.getConfig().get_pickaxeBlockBreakLimit();
            if (_data.getWorldData().getPlacedBlocks().contains(e.getBlock())) {
                damageable.setDamage(damageable.getDamage() - 1);
                p.getInventory().getItemInMainHand().setItemMeta(damageable);
            } 
            else {
                ApplyDamageToPickaxe(p, damageable, damage);
            }
        }
    }

    private void ApplyDamageToPickaxe(Player player, Damageable damageable, int damage) {
        if (damageable.getDamage() + damage >= netheritePickaxeDurability) {
            new PlayerCooldown(player, _data ,_data.getConfig().get_pickaxeReturnAfterBreakTime()) {
                @Override
                public void onTick() {

                }

                @Override
                public void onSecond() {

                }

                @Override
                public void onEnd() {
                    player.getInventory().addItem(_data.getItems().PICKAXE_ItemStack);
                }

                @Override
                public void onCancel() {

                }
            };
        }
        damageable.setDamage(damageable.getDamage() + damage);
        player.getInventory().getItemInMainHand().setItemMeta(damageable);
    }


}
