package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.GameSecondPassed;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameStarted;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;

import java.util.HashMap;
import java.util.Objects;


public class TagTntHandler implements Listener, IDisposable {
    
    private final TagData _data;
    
    private final HashMap<Player, Integer> playersSerialTags = new HashMap<>();
    private final HashMap<Player, Integer> playersTagsTimers = new HashMap<>();
    
    public TagTntHandler (TagData data)
    {
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
    private void onGameStart(TagGameStarted e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            playersTagsTimers.put(player, 0);
            playersSerialTags.put(player, 0);
        }
        
    }
    
    @EventHandler
    private void onSecond(GameSecondPassed e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        for (Player player : _data.getCurrentPlayers()) {
            playersTagsTimers.put(player, playersTagsTimers.get(player) + 1);
            if (playersTagsTimers.get(player) > _data.getConfig().get_tntComboTimer()) {
                playersSerialTags.put(player, 0);
            }
        }
    }
    
    @EventHandler
    private void onSuccessfulTag(PlayerTaggedSuccessfully e)
    {
        if (!_data.getCurrentPlayers().contains(e.get_receiver())) return;
        if (!_data.getCurrentPlayers().contains(e.get_tagger())) return;
        
        playersTagsTimers.put(e.get_receiver(), 0);
        playersSerialTags.put(e.get_receiver(), playersSerialTags.get(e.get_receiver()) + 1);
        
        if (playersSerialTags.get(e.get_receiver()) >= _data.getConfig().get_tntSpawnComboCount()) {
            Location tagLoc = e.get_tagger().getLocation();
            Location recLoc = e.get_receiver().getLocation();
            
            Vector line = tagLoc.toVector().subtract(recLoc.toVector());
            double finalLength = line.length() / 2f;
            
            Location betweenReceiverAndTagger = recLoc.add(line.normalize().multiply(finalLength));
            
            int explosionPower = playersSerialTags.get(e.get_receiver());

            _data.getWorldData().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, betweenReceiverAndTagger, 1);
            _data.getWorldData().getWorld().playSound(betweenReceiverAndTagger, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            _data.getWorldData().getWorld().createExplosion(betweenReceiverAndTagger, explosionPower);
            
            Vector fromExplosionToTagger = tagLoc.toVector().subtract(betweenReceiverAndTagger.toVector()).normalize();
            Vector fromExplosionToReceiver = recLoc.toVector().subtract(betweenReceiverAndTagger.toVector()).normalize();
            
            e.get_tagger().setVelocity(fromExplosionToTagger.clone().multiply(explosionPower));
            e.get_receiver().setVelocity(fromExplosionToReceiver.clone().multiply(explosionPower));
            
        }
    }
}
