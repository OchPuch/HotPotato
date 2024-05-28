package org.ccg.hotpotato.Utils.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ccg.hotpotato.HotPotato;
import org.ccg.hotpotato.Tag.Data.TagData;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class PlayerCooldown {

    private final BukkitTask task;
    protected int ticks = -1;

    public PlayerCooldown(final Player p, final TagData data, final int seconds) {
        final AtomicInteger cd = new AtomicInteger(seconds*20);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline() || data.getGameState() != TagData.GameState.Running) {
                    PlayerCooldown.this.cancel();
                    return;
                }
                ticks = cd.decrementAndGet();
                onTick();
                if (ticks % 20 == 0) onSecond();
                if (ticks == 0) {
                    onEnd();
                    PlayerCooldown.this.cancel();
                }
            }
        }.runTaskTimer(HotPotato.getInstance(), 0, 1);
    }

    public void cancel() {
        task.cancel();
        onCancel();
    }

    public abstract void onTick();
    public abstract void onSecond();
    public abstract void onEnd();
    public abstract void onCancel();

}
