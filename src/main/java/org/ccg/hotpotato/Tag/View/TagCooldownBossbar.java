package org.ccg.hotpotato.Tag.View;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ccg.hotpotato.Disposable.IDisposable;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Tag.Events.GameStagesEvents.*;
import org.ccg.hotpotato.Utils.Text.Formatter;

import java.util.Objects;

public class TagCooldownBossbar implements Listener, IDisposable {
    private final TagData _data;
    private final BossBar _bossBar;
    private final NamespacedKey _bossBarKey;
    
    public TagCooldownBossbar(TagData tagData) {
        _data = tagData;
        HotPotato.register(this);
        _bossBarKey = new NamespacedKey(HotPotato.getInstance(), "tag_cooldown_bossbar_" + _data.getGameID());
        _bossBar = Bukkit.createBossBar(_bossBarKey,"§c§lИгра скоро начнется", BarColor.RED, BarStyle.SOLID);
        _bossBar.setProgress(1);
        _bossBar.setVisible(false);
    }

    @Override
    public void Dispose() {
        Bukkit.getLogger().info("Disposing bossbar");
        HotPotato.unregister(this);
        _bossBar.removeAll();
        _bossBar.setVisible(false);
        Bukkit.removeBossBar(_bossBarKey);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onGamePreparingStart(TagGameStartedPreparing e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _bossBar.setVisible(true);
        _bossBar.setColor(BarColor.RED);
        _bossBar.setTitle("§c§lИгра скоро начнется");
        for (var player : _data.getCurrentPlayers()) {
            _bossBar.addPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundPreparingStart(TagRoundStartedPreparing e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _bossBar.setColor(BarColor.RED);
        _bossBar.setTitle("§c§lСледующий раунд скоро начнется");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onRoundStart(TagRoundStarted e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        _bossBar.setColor(BarColor.GREEN);
        _bossBar.setTitle("§a" + Formatter.time(_data.getConfig().get_roundDuration()));
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameEnd(TagGameEnded e) {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        Dispose();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onGameSecond (GameSecondPassed e)
    {
        if (!Objects.equals(e.getTagData().getGameID(), _data.getGameID())) return;
        if (_data.getGameState() != TagData.GameState.Running) return;
        _bossBar.setTitle("§a" + Formatter.time(_data.getTimeLeftInSeconds()));
        _bossBar.setProgress((double)_data.getTimeLeftInSeconds() / _data.getConfig().get_roundDuration());
        switch (_data.getSecondsBeforeDeletingBlocks()) {
            case 1 -> _bossBar.setColor(BarColor.RED);
            case 2 -> _bossBar.setColor(BarColor.YELLOW);
            default -> _bossBar.setColor(BarColor.GREEN);
        }
        
    }
}
