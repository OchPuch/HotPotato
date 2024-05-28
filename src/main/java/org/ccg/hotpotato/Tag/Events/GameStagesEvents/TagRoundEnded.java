package org.ccg.hotpotato.Tag.Events.GameStagesEvents;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.jetbrains.annotations.NotNull;

@Getter
public class TagRoundEnded extends Event {
    private final TagData tagData;

    private static final HandlerList handlers = new HandlerList();

    public TagRoundEnded(TagData data) {tagData = data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
