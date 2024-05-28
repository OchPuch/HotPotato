package org.ccg.hotpotato.Tag;

import org.bukkit.entity.Player;
import org.ccg.hotpotato.Tag.Data.TagData;

import java.util.ArrayList;
import java.util.List;

public class TagHunterSelector {
    public static void RandomSelection(TagData data) {
        ClearHuntersAndRunners(data);
        //Choose random hunters
        int huntersCount = (int) Math.ceil(data.getCurrentPlayers().size() * data.getConfig().get_huntersPercentage());
        huntersCount = Math.max(1, huntersCount); //At least 1 hunter
        huntersCount = Math.min(data.getCurrentPlayers().size() - 1, huntersCount); //At most players.size() - 1 hunters
        List<Player> playersWithoutHunters = new ArrayList<>(data.getCurrentPlayers());
        for (int i = 0; i < huntersCount; i++) {
            int index = (int) Math.floor(Math.random() * playersWithoutHunters.size());
            data.getHunters().add(playersWithoutHunters.get(index));
            playersWithoutHunters.remove(index);
        }
        data.getRunners().addAll(playersWithoutHunters);
        data.setHuntersSelected(true);
    }
    
    private static void ClearHuntersAndRunners(TagData data) {
        data.getHunters().clear();
        data.getRunners().clear();
    }
}
