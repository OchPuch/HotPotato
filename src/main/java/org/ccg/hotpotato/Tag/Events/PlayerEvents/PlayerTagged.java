package org.ccg.hotpotato.Tag.Events.PlayerEvents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerTagged extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player _receiver;
    private final Player _tagger;
    private final Projectile _projectile;
    private final EntityDamageByEntityEvent _damageEvent;
    
    private boolean _cancelled = false;
    
    public PlayerTagged(Player receiver, Player tagger, EntityDamageByEntityEvent damageEvent, Projectile projectile) {
        _receiver = receiver;
        _tagger = tagger;
        _projectile = projectile;
        _damageEvent = damageEvent;
    }
    
    public PlayerTagged(Player receiver, Player tagger, EntityDamageByEntityEvent damageEvent) {
        _receiver = receiver;
        _tagger = tagger;
        _damageEvent = damageEvent;
        _projectile = null;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return _cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        _cancelled = b;
    }
}
