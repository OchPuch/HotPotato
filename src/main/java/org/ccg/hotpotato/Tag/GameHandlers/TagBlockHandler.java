package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.Global.Blocks;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStartedPreparing;
import org.joml.Math;


import java.util.HashMap;
import java.util.Objects;

public class TagBlockHandler implements Listener, IDisposable {
    private final TagData _data;
    
    private final HashMap<Player, Material> playersBlocks = new HashMap<>();

    public TagBlockHandler(TagData data) {
        _data = data;
        HotPotato.register(this);
    }

    @Override
    public void Dispose() {
        HotPotato.unregister(this);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onGameStartedPreparing(TagGameStartedPreparing e)
    {
        Bukkit.getLogger().severe("Blocks give");
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        int index = (int) ((Math.random() * Blocks.getBlocks().size()-1));
        for (Player p : _data.getCurrentPlayers()) {
            index++;
            index = index % Blocks.getBlocks().size();
            Material block = Blocks.getBlocks().get(index);
            p.getInventory().addItem(new ItemStack(block, 32));
            playersBlocks.put(p, block);
        }
    }

    @EventHandler
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
        if (_data.getGameState() == TagData.GameState.WaitingForPlayers) return;
        var p = e.getPlayer();
        if (!_data.getCurrentPlayers().contains(p)) return;
        e.setDropItems(false);
    }
    
    @EventHandler
    void onPlace(BlockPlaceEvent e) {
        if (_data.getGameState() == TagData.GameState.WaitingForPlayers) return;
        var p = e.getPlayer();
        if (playersBlocks.containsKey(p) && e.getBlockPlaced().getType() == playersBlocks.get(p)){
            if (p.getInventory().getItemInMainHand().getType() == e.getBlockPlaced().getType())
                p.getInventory().getItemInMainHand().setAmount(32);
            else if (p.getInventory().getItemInOffHand().getType() == e.getBlockPlaced().getType())
                p.getInventory().getItemInOffHand().setAmount(32);
        }
        
        if (isBlockShouldBeDeleted(e.getBlockPlaced())) {
            _data.getWorldData().getBlocksToDelete().add(e.getBlockPlaced());
            if (_data.getSecondsBeforeDeletingBlocks() == 0) {
                e.getBlockPlaced().setType(Material.AIR);
                e.setCancelled(true);
                return;
            }
        }
        _data.getWorldData().getPlacedBlocks().add(e.getBlockPlaced());
        
    }
    
    private boolean isBlockShouldBeDeleted(Block block)
    {
        Location[] locations = new Location[] {
                block.getLocation().add(0, 1, 0),
                block.getLocation().add(0, -1, 0),
                block.getLocation().add(-1, 0, 0),
                block.getLocation().add(1, 0, 0),
                block.getLocation().add(0, 0, 1),
                block.getLocation().add(0, 0, -1)
        };
        
        for (var location : locations) {
            if (location.getBlock().getType() != Material.AIR && !_data.getWorldData().getPlacedBlocks().contains(location.getBlock())) {
                return false;
            }
        }
        
        return true;
    }
}
