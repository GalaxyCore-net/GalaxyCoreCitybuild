package net.galaxycore.citybuild.shop;

import lombok.Getter;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.utils.Both;
import net.galaxycore.citybuild.utils.RenderUtilities;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class ShopAnimation extends BukkitRunnable {
    private final Player player;
    private final Both<Location, Shop> shop;
    private int anim;

    public ShopAnimation(Player player, Both<Location, Shop> shop) {
        this.player = player;
        this.shop = shop;
    }

    public void open() {
        anim = 1;
        this.runTaskTimerAsynchronously(Essential.getInstance(), 0, 1);
    }

    public void close() {
        anim = 0;
        this.runTaskTimerAsynchronously(Essential.getInstance(), 0, 1);
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if(anim == 0)
            animationStepClose();
         else
             animationStepOpen();
    }

    private void animationStepOpen() {
        RenderUtilities.highlightBlock(shop.getT(), Color.GREEN);
        this.cancel();
    }

    private void animationStepClose() {
        RenderUtilities.highlightBlock(shop.getT(), Color.RED);
        this.cancel();
    }
}
