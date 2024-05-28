package org.ccg.hotpotato.Tag.Events.PlayerEvents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerTaggedSuccessfully extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player _receiver;
    private final Player _tagger;
    private final Projectile _projectile;
    
    public PlayerTaggedSuccessfully(PlayerTagged event) {
        _receiver = event.get_receiver();
        _tagger = event.get_tagger();
        _projectile = event.get_projectile();
    }
    
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}

