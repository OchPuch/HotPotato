package org.ccg.hotpotato.Tag.Events.GameGeneralEvents;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.jetbrains.annotations.NotNull;

@Getter
public class BlocksCleared extends Event {
    private final TagData tagData;

    private static final HandlerList handlers = new HandlerList();
    
    public BlocksCleared(TagData data) {
        this.tagData = data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}