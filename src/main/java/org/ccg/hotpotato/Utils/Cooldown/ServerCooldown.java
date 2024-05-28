package org.ccg.hotpotato.Utils.Cooldown;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ccg.hotpotato.HotPotato;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ServerCooldown {

    private final BukkitTask task;
    protected int ticks = -1;

    public ServerCooldown(final int seconds) {
        final AtomicInteger cd = new AtomicInteger(seconds*20);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                ticks = cd.decrementAndGet();
                onTick();
                if (ticks % 20 == 0) onSecond();
                if (ticks == 0) {
                    onEnd();
                    ServerCooldown.this.cancel();
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