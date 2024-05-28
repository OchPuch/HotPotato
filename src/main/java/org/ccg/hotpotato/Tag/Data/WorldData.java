package org.ccg.hotpotato.Tag.Data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WorldData {
    private final World world;
    private final List<Block> placedBlocks;
    private final List<Block> blocksToDelete;
    private Location spawnLocation;

    private int borderSize;

    private boolean spawnLocationSet = false;

    public WorldData(World world) {
        this.world = world;
        blocksToDelete = new ArrayList<>();
        placedBlocks = new ArrayList<>();
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location.toHighestLocation().add(0, 1, 0);
        spawnLocationSet = true;
    }

    public void UpdateBorderSizeByPlayers(int playersCount) {
        var barrier = 13 * playersCount + 2;
        if (barrier > 150) barrier = 150;
        borderSize = barrier;
    }

    public boolean IsOutsideBorder(Location loc) {
        int halfSize = borderSize / 2;
        return IsOutsideBorder(loc, halfSize);
    }

    public boolean IsOutsideSafeZone(Location loc) {
        int buffer = (borderSize / 2) + 1;
        return IsOutsideBorder(loc, buffer);
    }

    public boolean IsOutsideSecondSafeZone(Location loc) {
        int buffer = (borderSize / 2) + 2;
        return IsOutsideBorder(loc, buffer);
    }

    public boolean IsOutsideBorder(Location loc, int halfBorderSize) {
        return (loc.getX() > (spawnLocation.getX() + halfBorderSize))
                || (loc.getX() < (spawnLocation.getX() - halfBorderSize))
                || (loc.getZ() > (spawnLocation.getZ() + halfBorderSize))
                || (loc.getZ() < (spawnLocation.getZ() - halfBorderSize));
    }

    public boolean IsOutsideBorderByX(Location loc) {
        int halfSize = borderSize / 2;
        return (loc.getX() > (spawnLocation.getX() + halfSize))
                || (loc.getX() < (spawnLocation.getX() - halfSize));
    }

    public boolean IsOutsideBorderByZ(Location loc) {
        int halfSize = borderSize / 2;
        return (loc.getZ() > (spawnLocation.getZ() + halfSize))
                || (loc.getZ() < (spawnLocation.getZ() - halfSize));
    }

    public boolean CloseToUpX(Location loc, int howClose) {
        int halfSize = borderSize / 2 - howClose;
        return (loc.getX() > (spawnLocation.getX() + halfSize));
    }

    public boolean CloseToDownX(Location loc, int howClose) {
        int halfSize = borderSize / 2 - howClose;
        return (loc.getX() < (spawnLocation.getX() - halfSize));
    }

    public boolean CloseToUpZ(Location loc, int howClose) {
        int halfSize = borderSize / 2 - howClose;
        return (loc.getZ() > (spawnLocation.getZ() + halfSize));
    }

    public boolean CloseToDownZ(Location loc, int howClose) {
        int halfSize = borderSize / 2 - howClose;
        return (loc.getZ() < (spawnLocation.getZ() - halfSize));
    }
    
    public Location GetBorderTopXTopZ() {
        int halfSize = borderSize/2;
        return spawnLocation.clone().add(new Vector(halfSize,0,halfSize));
    }

    public Location GetBorderTopXDownZ() {
        int halfSize = borderSize/2;
        return spawnLocation.clone().add(new Vector(halfSize,0,-halfSize));
    }

    public Location GetBorderDownXTopZ() {
        int halfSize = borderSize/2;
        return spawnLocation.clone().add(new Vector(-halfSize,0,halfSize));
    }

    public Location GetBorderDownXDownZ() {
        int halfSize = borderSize/2;
        return spawnLocation.clone().add(new Vector(-halfSize,0,-halfSize));
    }


}
