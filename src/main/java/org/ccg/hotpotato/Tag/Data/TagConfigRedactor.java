package org.ccg.hotpotato.Tag.Data;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ccg.hotpotato.HotPotato;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TagConfigRedactor {
    private static final HashMap<String, TagConfig> _configs = new HashMap<>();
    private static boolean _isInitialized = false;

    public static void Init(){
        if (_isInitialized)
            return;
        TagConfigRedactor _instance = new TagConfigRedactor();
        HotPotato.getAnnotationParser().parse(_instance);
        _isInitialized = true;
    }
    
    public static void AddConfig(@NotNull TagConfig config){
        if (_configs.containsKey(config.get_configName()))
            return;
        if (_configs.containsValue(config))
            return;
        _configs.put(config.get_configName(), config);
        Bukkit.getLogger().info("Added config " + config.get_configName());
    }
    
    
    @CommandMethod("gameConfig <string> duration <int>")
    public void setGameDuration(final Player p, @Argument("string") String configName ,@Argument("int") int gameDuration) {
        if( _configs.get(configName).SetGameDuration(gameDuration, this)) 
            p.sendMessage(Component.text("Game duration set to " + gameDuration));
    }

    @CommandMethod("gameConfig <string> startDelay <int>")
    public void setGameStartDelay(final Player p, @Argument("string") String configName ,@Argument("int") int gameStartDelay) {
        if (_configs.get(configName).SetGameStartDelay(gameStartDelay, this)) 
            p.sendMessage(Component.text("Game start delay set to " + gameStartDelay));
    }
    
    @CommandMethod("gameConfig <string> roundStartDelay <int>")
    public void setRoundStartDelay(final Player p, @Argument("string") String configName ,@Argument("int") int roundStartDelay) {
        if (_configs.get(configName).SetRoundStartDelay(roundStartDelay, this)) 
            p.sendMessage(Component.text("Round start delay set to " + roundStartDelay));
    }

    @CommandMethod("gameConfig <string> huntersPercentage <float>")
    public void setHuntersPercentage(final Player p, @Argument("string") String configName ,@Argument("float") float huntersPercentage) {
        if (_configs.get(configName).SetHuntersPercentage(huntersPercentage, this)) 
            p.sendMessage(Component.text("Hunters percentage set to " + huntersPercentage));
    }

    @CommandMethod("gameConfig <string> deletePlacedBlocksTime <int>")
    public void setDeletePlacedBlocksTime(final Player p, @Argument("string") String configName ,@Argument("int") int deletePlacedBlocksTime) {
        if (_configs.get(configName).SetDeletePlacedBlocksTime(deletePlacedBlocksTime, this)) 
            p.sendMessage(Component.text("Delete placed blocks time set to " + deletePlacedBlocksTime));
    }

    @CommandMethod("gameConfig <string> noHitFallHeight <int>")
    public void setNoHitFallHeight(final Player p, @Argument("string") String configName ,@Argument("int") int noHitFallHeight) {
        if (_configs.get(configName).SetNoHitFallHeight(noHitFallHeight, this)) 
            p.sendMessage(Component.text("No hit fall height set to " + noHitFallHeight));
    }

    @CommandMethod("gameConfig <string> pickaxeBlockBreakLimit <int>")
    public void setPickaxeBlockBreakLimit(final Player p, @Argument("string") String configName ,@Argument("int") int pickaxeBlockBreakLimit) {
        if (_configs.get(configName).SetPickaxeBlockBreakLimit(pickaxeBlockBreakLimit, this)) 
            p.sendMessage(Component.text("Pickaxe block break limit set to " + pickaxeBlockBreakLimit));
    }

    @CommandMethod("gameConfig <string> pickaxeReturnAfterBreakTime <int>")
    public void setPickaxeReturnAfterBreakTime(final Player p, @Argument("string") String configName ,@Argument("int") int pickaxeReturnAfterBreakTime) {
        if (_configs.get(configName).SetPickaxeReturnAfterBreakTime(pickaxeReturnAfterBreakTime, this)) 
            p.sendMessage(Component.text("Pickaxe return after break time set to " + pickaxeReturnAfterBreakTime));
    }

    @CommandMethod("gameConfig <string> pickaxeMaxHeightBeforeBreakOnFallingHit <int>")
    public void setPickaxeMaxHeightBeforeBreakOnFallingHit(final Player p, @Argument("string") String configName ,@Argument("int") int pickaxeMaxHeightBeforeBreakOnFallingHit) {
        if (_configs.get(configName).SetPickaxeMaxHeightBeforeBreakOnFallingHit(pickaxeMaxHeightBeforeBreakOnFallingHit, this)) 
            p.sendMessage(Component.text("Pickaxe max height before break on falling hit set to " + pickaxeMaxHeightBeforeBreakOnFallingHit));
    }

    @CommandMethod("gameConfig <string> pickaxeBoostMultiplier <float>")
    public void setPickaxeBoostMultiplier(final Player p, @Argument("string") String configName ,@Argument("float") float value) {
        if (_configs.get(configName).SetPickaxeBoostMultiplier(value, this))
            p.sendMessage(Component.text("Pickaxe max height before break on falling hit set to " + value));
    }
    
    @CommandMethod("gameConfig <string> totemCooldown <int>")
    public void setTotemCooldown(final Player p, @Argument("string") String configName ,@Argument("int") int totemCooldown) {
        if (_configs.get(configName).SetTotemCooldown(totemCooldown, this)) 
            p.sendMessage(Component.text("Totem cooldown set to " + totemCooldown));
    }
    
    @CommandMethod("gameConfig <string> totemVelocityUpOnActivation <double>")
    public void setTotemVelocityUpOnActivation(final Player p, @Argument("string") String configName ,@Argument("double") double totemVelocityUpOnActivation) {
        if (_configs.get(configName).SetTotemVelocityUpOnActivation(totemVelocityUpOnActivation, this)) 
            p.sendMessage(Component.text("Totem velocity up on activation set to " + totemVelocityUpOnActivation));
    }
    
    @CommandMethod("gameConfig <string> totemCancelDamageEvent <boolean>")
    public void setTotemCancelDamageEvent(final Player p, @Argument("string") String configName ,@Argument("boolean") boolean totemCancelDamageEvent) {
        if (_configs.get(configName).SetTotemCancelDamageEvent(totemCancelDamageEvent, this)) 
            p.sendMessage(Component.text("Totem cancel damage event set to " + totemCancelDamageEvent));
    }
    
    @CommandMethod("gameConfig <string> totemMaxDamage <double>")
    public void setTotemMaxDamage(final Player p, @Argument("string") String configName ,@Argument("double") double totemMaxDamage) {
        if (_configs.get(configName).SetTotemMaxDamage(totemMaxDamage, this)) 
            p.sendMessage(Component.text("Totem max damage set to " + totemMaxDamage));
    }
    
    @CommandMethod("gameConfig <string> giveHookFallDistance <float>")
    public void setGiveHookFallDistance(final Player player,  @Argument("string") String configName ,@Argument("float") float giveHookFallDistance) {
        if (_configs.get(configName).SetGiveHookFallDistance(giveHookFallDistance, this)) 
            player.sendMessage(Component.text("Give hook fall distance set to " + giveHookFallDistance));
    }
    
    @CommandMethod("gameConfig <string> hookVelocity <float>")
    public void setHookVelocity(final Player player,  @Argument("string") String configName ,@Argument("float") float hookVelocity) {
        if (_configs.get(configName).SetHookVelocity(hookVelocity, this)) 
            player.sendMessage(Component.text("Hook velocity set to " + hookVelocity));
    }
    
    @CommandMethod("gameConfig <string> hookSolidBlockRadiusCheck <int>")
    public void setHookSolidBlockRadiusCheck(final Player player,  @Argument("string") String configName ,@Argument("int") int hookSolidBlockRadiusCheck) {
        if (_configs.get(configName).SetHookCheckForSolidBlockRadius(hookSolidBlockRadiusCheck, this)) 
            player.sendMessage(Component.text("Hook solid block radius check set to " + hookSolidBlockRadiusCheck));
    }
    
    @CommandMethod("gameConfig <string> giveUltraFeatherFallDistance <int>")
    public void setGiveUltraFeatherFallDistance(final Player player,  @Argument("string") String configName ,@Argument("int") int giveUltraFeatherFallDistance) {
        if (_configs.get(configName).SetGiveUltraFeatherFallDistance(giveUltraFeatherFallDistance, this)) 
            player.sendMessage(Component.text("Give ultra feather fall distance set to " + giveUltraFeatherFallDistance));
    }
    
    @CommandMethod("gameConfig <string> ultraFeatherVelocityForHunters <float>")
    public void setUltraFeatherVelocity(final Player player,  @Argument("string") String configName ,@Argument("float") float ultraFeatherVelocity) {
        if (_configs.get(configName).SetUltraFeatherVelocityForHunters(ultraFeatherVelocity, this)) 
            player.sendMessage(Component.text("Ultra feather velocity set to " + ultraFeatherVelocity));
    }
    
    @CommandMethod("gameConfig <string> ultraFeatherVelocityForRunners <float>")
    public void setUltraFeatherVelocityForRunners(final Player player,  @Argument("string") String configName ,@Argument("float") float ultraFeatherVelocity) {
        if (_configs.get(configName).SetUltraFeatherVelocityForRunners(ultraFeatherVelocity, this)) 
            player.sendMessage(Component.text("Ultra feather velocity set to " + ultraFeatherVelocity));
    }
    
    @CommandMethod("gameConfig <string> baseHookVelocityY <float>")
    public void setBaseHookVelocityY(final Player player,  @Argument("string") String configName ,@Argument("float") float baseHookVelocityY) {
        if (_configs.get(configName).SetBaseHookVelocityY(baseHookVelocityY, this))
            player.sendMessage(Component.text("Set base hook velocity Y to " + baseHookVelocityY));
    }

    @CommandMethod("gameConfig <string> tntComboTime <int>")
    public void setTntComboTime(final Player player,  @Argument("string") String configName ,@Argument("int") int tntComboTime) {
        if (_configs.get(configName).SetTntComboTimer(tntComboTime, this))
            player.sendMessage(Component.text("Tnt combo time set to " + tntComboTime));
    }

    @CommandMethod("gameConfig <string> tntSpawnComboCount <int>")
    public void setBaseHookVelocityY(final Player player,  @Argument("string") String configName ,@Argument("int") int tntComboCount) {
        if (_configs.get(configName).SetTntSpawnComboCount(tntComboCount, this))
            player.sendMessage(Component.text("Tnt spawn combo count set to " + tntComboCount));
    }

    @CommandMethod("gameConfig <string> borderBoost <float>")
    public void setBorderBoost(final Player player,  @Argument("string") String configName ,@Argument("float") float borderBoost) {
        if (_configs.get(configName).SetBorderBoost(borderBoost, this))
            player.sendMessage(Component.text("Border boost set to " + borderBoost));
    }
}
