package org.ccg.hotpotato.Tag;

import cloud.commandframework.annotations.CommandMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ccg.hotpotato.Config.ConfigFactory;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagConfig;
import org.ccg.hotpotato.Tag.Data.TagConfigRedactor;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Data.WorldData;
import org.ccg.hotpotato.Tag.View.TagCooldownBossbar;
import org.ccg.hotpotato.Tag.View.TagPlayerColoring;
import org.ccg.hotpotato.Tag.View.TagSound;
import org.ccg.hotpotato.Tag.View.TagText;

import java.util.HashMap;
import java.util.HashSet;


public class TagGameManager {
    
    private TagConfig _gameConfig;
    public static final HashSet<TagData> currentGames = new HashSet<>();
    public static final HashMap<String, String> disconnectedFromTagPlayers = new HashMap<>();
   
    public void Init()
    {
        //Register commands
        HotPotato.getAnnotationParser().parse(this);
        TagGameManagerHandler tagGameManagerHandler = new TagGameManagerHandler();
        
        //Config
        _gameConfig = ConfigFactory.LoadConfig(TagConfig.class);
        if (_gameConfig == null) {
            Bukkit.getLogger().info("No game config found, also failed to create from Factory");
            _gameConfig = new TagConfig();
            ConfigFactory.SaveConfig(_gameConfig);
        }
        
        //Config redactor
        TagConfigRedactor.Init();
        TagConfigRedactor.AddConfig(_gameConfig);
    }
    
    @CommandMethod("startGame")
    public void StartNewGame(final Player p)
    {
        //Players
        HashSet<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
        
        //World
        WorldData worldData = new WorldData(p.getWorld());
        worldData.setSpawnLocation(p.getLocation());
        worldData.UpdateBorderSizeByPlayers(players.size());
        
        //Data
        var timeInMs = System.currentTimeMillis();
        TagData tagData = new TagData(players, Long.toString(timeInMs), _gameConfig, new HashMap<>(), worldData);
        currentGames.add(tagData);
        
        //Game model
        TagGame game = new TagGame(tagData);
        
        //View
        TagSound sound = new TagSound(tagData);
        TagText text = new TagText(tagData);
        TagCooldownBossbar bossbar = new TagCooldownBossbar(tagData);
        TagPlayerColoring coloring = new TagPlayerColoring(tagData);
        
        //Start game
        game.StartGame();
        
    }
}
