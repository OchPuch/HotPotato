package org.ccg.hotpotato.Tag.Events.GameStagesEvents;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.jetbrains.annotations.NotNull;

@Getter
public class TagGameStarted extends Event {
    private final TagData tagData;
    
    private static final HandlerList handlers = new HandlerList();

    public TagGameStarted(TagData data) {
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
