package org.ccg.hotpotato.Tag.Data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ccg.hotpotato.Abilities.Ability;
import org.ccg.hotpotato.Tag.Items;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class TagData {

    //region General
    @Getter
    private final String gameID;
    @Getter
    private final TagConfig config;
    @Getter
    private final WorldData worldData;
    @Getter
    private GameState gameState;
    @Getter
    private final AtomicInteger timeLeftInTicks = new AtomicInteger();
    @Getter @Setter
    private int secondsBeforeDeletingBlocks;
    @Getter
    private Items items;
    //endregion

    //region Players
    private final HashSet<String> joinedPlayersNames;
    @Getter
    private final HashSet<Player> currentPlayers;
    @Getter
    private final HashSet<Player> spectators;
    @Getter
    private final HashMap<Player, Ability> abilities;
    @Getter
    private final HashSet<Player> hunters;
    @Getter
    private final HashSet<Player> runners;
    @Setter @Getter
    private boolean huntersSelected;
    //endregion
    
    
    public TagData(@NotNull HashSet<Player> players, @NotNull String gameID, @NotNull TagConfig config, @NotNull HashMap<Player,Ability> abilities, @NotNull WorldData worldData) {
        Bukkit.getLogger().severe("Creating tag data " + gameID);
        
        this.gameID = gameID;
        this.config = config;
        this.worldData = worldData;
        
        this.joinedPlayersNames = new HashSet<>();
        for (Player player : players) {
            joinedPlayersNames.add(player.getName());
        }
        this.currentPlayers = new HashSet<>(players);
        this.abilities = new HashMap<>(abilities);
        hunters = new HashSet<>();
        runners = new HashSet<>();
        spectators = new HashSet<>();
        
        gameState = GameState.WaitingForPlayers;
        timeLeftInTicks.set(config.get_roundDuration() * 20);
        secondsBeforeDeletingBlocks = config.get_deletePlacedBlocksTime();

        items = new Items(this);
    }
    
    public Player GetRandomHunter()
    {
        return hunters.toArray(Player[]::new)[new Random().nextInt(hunters.size())];
    }
    

    public void setGameState(GameState state) {
        if (state == gameState){
            Bukkit.getLogger().info("game " + gameID + " state already " + state);
            return;
        }
        
        switch (state) {
            case WaitingForPlayers, PreparingGame, PreparingRound:
                if (!worldData.isSpawnLocationSet())
                    throw new IllegalStateException("Cannot prepare game without setting spawn location");
                break;
        }
        
        Bukkit.getLogger().info("game " + gameID + " state changed to " + state);
        gameState = state;
    }

    public int getTimeLeftInSeconds() {
        return timeLeftInTicks.get() / 20;
    }
    
    public int getTimePassedInSeconds() {
        return config.get_roundDuration() - getTimeLeftInSeconds();
    }
    
    public HashSet<String> getJoinedPlayersNames() {
        return new HashSet<>(joinedPlayersNames);
    }
    
    public enum GameState{
        WaitingForPlayers,
        PreparingGame,
        PreparingRound,
        Running,
        Ending
    }
    
    
}
