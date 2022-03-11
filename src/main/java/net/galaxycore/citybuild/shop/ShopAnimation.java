package net.galaxycore.citybuild.shop;

import com.comphenix.packetwrapper.WrapperPlayServerBlockAction;
import com.comphenix.protocol.wrappers.BlockPosition;
import lombok.Getter;
import net.galaxycore.citybuild.utils.Both;
import net.galaxycore.citybuild.utils.RenderUtilities;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
public class ShopAnimation {
    private final Player player;
    private final Both<Location, Shop> shop;

    public ShopAnimation(Player player, Both<Location, Shop> shop) {
        this.player = player;
        this.shop = shop;
    }

    public void open() {
        RenderUtilities.highlightBlock(shop.getT(), Color.GREEN);

        // https://wiki.vg/Protocol#Block_Action
        WrapperPlayServerBlockAction packetWrapperOpen = new WrapperPlayServerBlockAction();
        packetWrapperOpen.setLocation(new BlockPosition(shop.getT().getBlock().getLocation().toVector()));
        packetWrapperOpen.setBlockType(Material.CHEST);

        // https://wiki.vg/Block_Actions#Chest
        packetWrapperOpen.setByte1(1); // Action 1 (There is only one for a chest)
        packetWrapperOpen.setByte2(1); // Action Parameter 1 (Open)
    }

    public void close() {
        RenderUtilities.highlightBlock(shop.getT(), Color.RED);
    }

    private WrapperPlayServerBlockAction getWrapperFor(Location location, )
}
