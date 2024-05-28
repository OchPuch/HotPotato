package org.ccg.hotpotato.Tag.Data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.ccg.hotpotato.Config.Config;
import org.jetbrains.annotations.NotNull;

@Getter
public class TagConfig extends Config {

    //region Game settings
    @SerializedName("gameDuration")
    private int _roundDuration = 120;
    @SerializedName("roundStartDelay")
    private int _roundStartDelay = 10;
    @SerializedName("gameStartDelay")
    private int _gameStartDelay = 10;
    //endregion

    //region Player settings
    @SerializedName("huntersPercentage")
    private float _huntersPercentage = 0.4f;
    @SerializedName("deletePlacedBlocksTime")
    private int _deletePlacedBlocksTime = 15;
    @SerializedName("noHitFallHeight")
    private int _noHitFallHeight = 9;
    //endregion

    //region Pickaxe
    @SerializedName("pickaxeBlockBreakLimit")
    private int _pickaxeBlockBreakLimit = 7;
    @SerializedName("pickaxeReturnAfterBreakTime")
    private int _pickaxeReturnAfterBreakTime = 20;
    @SerializedName("pickaxeMaxHeightBeforeBreakOnFallingHit")
    private int _pickaxeMaxHeightBeforeBreakOnFallingHit = 30;
    @SerializedName("pickaxeBoostMultiplier")
    private float _pickaxeBoostMultiplier = 1.0f;
    //endregion

    //region Totem
    @SerializedName("totemCooldown")
    private int _totemCooldown = 15;
    @SerializedName("totemVelocityUpOnActivation")
    private double _totemVelocityUpOnActivation = 10;
    @SerializedName("totemCancelDamageEvent")
    private boolean _totemCancelDamageEvent = true;
    @SerializedName("totemMaxDamage")
    private double _totemMaxDamage = 0.1;
    // endregion

    //region Hook
    @SerializedName("giveHookFallDistance")
    private float _giveHookFallDistance = 0.5f;

    @SerializedName("hookVelocity")
    private float _hookVelocity = 0.5f;

    @SerializedName("hookBaseVelocityY")
    private float _hookBaseVelocityY = 1.8f;

    @SerializedName("hookCheckForSolidBlockRadius")
    private int _hookCheckForSolidBlockRadius = 2;

    //endregion

    //region Ultra Feather

    @SerializedName("giveUltraFeatherFallDistance")
    private int _giveUltraFeatherFallDistance = 20;

    @SerializedName("ultraFeatherVelocity")
    private float _ultraFeatherVerticalVelocityForHunters = 10f;

    @SerializedName("ultraFeatherVelocityForRunners")
    private float _ultraFeatherVerticalVelocityForRunners = 2f;

    //endregion

    //region Misc
    @SerializedName("tntComboTimer")
    private int _tntComboTimer = 5;

    @SerializedName("tntSpawnComboCount")
    private int _tntSpawnComboCount = 3;
    
    @SerializedName("borderBoost")
    private float _borderBoost = 5f;


    //endregion

    public final boolean SetGameDuration(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _roundDuration = value;
        return Save();
    }

    public final boolean SetGameStartDelay(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _gameStartDelay = value;
        return Save();
    }

    public final boolean SetRoundStartDelay(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _roundStartDelay = value;
        return Save();
    }

    public final boolean SetHuntersPercentage(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _huntersPercentage = value;
        return Save();
    }

    public final boolean SetDeletePlacedBlocksTime(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _deletePlacedBlocksTime = value;
        return Save();
    }

    public final boolean SetNoHitFallHeight(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _noHitFallHeight = value;
        return Save();
    }

    public final boolean SetPickaxeBlockBreakLimit(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _pickaxeBlockBreakLimit = value;
        return Save();
    }

    public final boolean SetPickaxeReturnAfterBreakTime(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _pickaxeReturnAfterBreakTime = value;
        return Save();
    }

    public final boolean SetPickaxeMaxHeightBeforeBreakOnFallingHit(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _pickaxeMaxHeightBeforeBreakOnFallingHit = value;
        return Save();
    }

    public final boolean SetPickaxeBoostMultiplier(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _pickaxeBoostMultiplier = value;
        return Save();
    }

    public final boolean SetTotemCooldown(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _totemCooldown = value;
        return Save();
    }

    public final boolean SetTotemVelocityUpOnActivation(double value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _totemVelocityUpOnActivation = value;
        return Save();
    }

    public final boolean SetTotemCancelDamageEvent(boolean value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _totemCancelDamageEvent = value;
        return Save();
    }

    public final boolean SetTotemMaxDamage(double value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _totemMaxDamage = value;
        return Save();
    }

    public final boolean SetGiveHookFallDistance(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _giveHookFallDistance = value;
        return Save();
    }

    public final boolean SetHookVelocity(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _hookVelocity = value;
        return Save();
    }

    public final boolean SetHookCheckForSolidBlockRadius(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _hookCheckForSolidBlockRadius = value;
        return Save();
    }

    public final boolean SetGiveUltraFeatherFallDistance(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _giveUltraFeatherFallDistance = value;
        return Save();
    }

    public final boolean SetUltraFeatherVelocityForHunters(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _ultraFeatherVerticalVelocityForHunters = value;
        return Save();
    }

    public final boolean SetUltraFeatherVelocityForRunners(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _ultraFeatherVerticalVelocityForRunners = value;
        return Save();
    }

    public final boolean SetBaseHookVelocityY(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _hookBaseVelocityY = value;
        return Save();
    }

    public final boolean SetTntComboTimer(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _tntComboTimer = value;
        return Save();
    }

    public final boolean SetTntSpawnComboCount(int value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _tntSpawnComboCount = value;
        return Save();
    }

    public final boolean SetBorderBoost(float value, @NotNull TagConfigRedactor tagConfigRedactor) {
        _borderBoost = value;
        return Save();
    }
}
