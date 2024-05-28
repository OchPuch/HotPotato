package org.ccg.hotpotato.Utils;

import org.bukkit.Location;

public class BlockFinder {
    public static boolean HaveSolidBlockInRadius(Location location, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (location.clone().add(x, y, z).getBlock().getType().isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
}
