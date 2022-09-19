package net.sxmaa.itemclear;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Itemclear extends JavaPlugin {

    public static Itemclear instance;
    private Timer timer;
    private TimerTask tt;
    private final long delay = 1200000;


    public static Itemclear getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.setupTimerDelay(delay);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        tt.cancel();
        timer.cancel();
        timer.purge();
        // Plugin shutdown logic
    }

    void setupTimer(final long time) {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        if (delay > 0L) {
            final int[] i = {0};
             tt = new TimerTask() {
                @Override
                public void run() {
                    instance.getServer()
                            .sendMessage(
                                    Component.text("Clearing all items in 5 seconds.", NamedTextColor.RED)
                            );
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    getPlugin().getServer().getScheduler()
                            .scheduleSyncDelayedTask(instance, () -> getPlugin().getServer().getWorlds().forEach(
                                    world -> {

                                        world.getEntities().forEach(
                                                entity -> {
                                                    if (entity instanceof Item) {
                                                        logToOnline(String.valueOf(entity));
                                                        entity.remove();
                                                        i[0]++;
                                                    }
                                                });
                                    }));
                    instance.getServer()
                            .sendMessage(
                                    Component.text("Cleared ", NamedTextColor.WHITE).append(
                                            Component.text(String.valueOf(i[0]), Style.style(TextDecoration.BOLD))).append(
                                            Component.text(" items.", NamedTextColor.WHITE)
                                    ));
                }
            };
            this.timer.scheduleAtFixedRate(tt, new Date(time), delay);
        }
        else {
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //error
                }
            }, new Date(time));
        }
    }

    void setupTimerDelay(final long delay) {
        this.setupTimer(System.currentTimeMillis() + delay);
    }

    void logToOnline(String message) {
        instance.getServer()
                .sendMessage(Component.text(message, NamedTextColor.RED));
    }
}
