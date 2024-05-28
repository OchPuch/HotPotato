package org.ccg.hotpotato.Tag.View;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStarted;

import java.util.Objects;

public class TagSound implements Listener, IDisposable {
    
    private final TagData _data;
    
    public TagSound(TagData data) {
        _data = data;
        HotPotato.register(this);
    }
    
    public void Dispose() {
        Bukkit.getLogger().info("Disposing sound");
        HotPotato.unregister(this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onGameStart(TagGameStarted e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1f, 1f);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTag(PlayerTaggedSuccessfully e) {
        if (!_data.getCurrentPlayers().contains(e.get_tagger()) || !_data.getCurrentPlayers().contains(e.get_receiver())) return;
        e.get_tagger().playSound(e.get_tagger(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
        e.get_receiver().playSound(e.get_receiver(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1.5f);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onGameEnd(TagGameEnded e){
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, 1f);
        }
        Dispose();
    }
}
