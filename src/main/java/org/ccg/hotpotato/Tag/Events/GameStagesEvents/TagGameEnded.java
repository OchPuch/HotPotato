package org.ccg.hotpotato.Tag.Events.GameStagesEvents;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Data.TagGameResult;
import org.jetbrains.annotations.NotNull;


@Getter
public class TagGameEnded extends Event {
    private final TagData tagData;
    private final TagGameResult gameResult;
    
    private static final HandlerList handlers = new HandlerList();

    public TagGameEnded(TagData data, TagGameResult gameResult) {
        this.tagData = data;
        this.gameResult = gameResult;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
   
}
