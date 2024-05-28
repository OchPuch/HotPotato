package org.ccg.hotpotato.Tag.GameHandlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.TagGameEnded;
import org.ccg.hotpotato.Tag.Events.PlayerEvents.PlayerTaggedSuccessfully;

import java.util.Objects;

public class TagEffectsHandler implements Listener, IDisposable {

    private final TagData _data;

    public TagEffectsHandler(TagData data) {
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
    private void onTagSuccessful(PlayerTaggedSuccessfully e) {
        if (!_data.getCurrentPlayers().contains(e.get_tagger())) return;
        int amplifier;
        PotionEffect potionEffect = e.get_tagger().getPotionEffect(PotionEffectType.SPEED);
        if (potionEffect != null) {
            amplifier = potionEffect.getAmplifier() + 1;
            if (amplifier >= 5) amplifier = 5;
        } else amplifier = 1;
        e.get_tagger().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (20 + 20 * amplifier), amplifier));
    }

}
