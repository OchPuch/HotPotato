package org.ccg.hotpotato.Tag.Events.PlayerEvents;

import lombok.Getter;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerEliminated extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player _player;
    private final TagData _data;
    private final EliminationCause _cause;

    public PlayerEliminated(Player player, TagData data, EliminationCause cause) {
        _player = player;
        _cause = cause;
        _data = data;
    }
    
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public enum EliminationCause {
        Tagged,
        Quit,
        Disconnected,
        Kicked,
        Banned,
        Other
    }

}