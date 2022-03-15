package net.galaxycore.citybuild.shop;

import com.comphenix.packetwrapper.WrapperPlayServerBlockAction;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramAccessor;
import com.github.unldenis.hologram.animation.Animation;
import lombok.Getter;
import net.galaxycore.citybuild.utils.Both;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class ShopAnimation {
    private final Player player;
    private final Both<Location, Shop> shop;

    public ShopAnimation(Player player, Both<Location, Shop> shop) {
        this.player = player;
        this.shop = shop;
    }

    public void open() {
        getWrapperFor(shop.getT(), true).sendPacket(player);
        final Hologram hologram = Hologram.builder()
                .location(shop.getT().toCenterLocation().subtract(0, 1, 0))
                .addLine(ItemStack.deserialize(shop.getR().getItemStack()))
                .build(ShopLoadingListener.getHologramPool());
        hologram.setAnimation(0, Animation.CIRCLE);

        Bukkit.getOnlinePlayers().forEach(player1 -> HologramAccessor.hide(hologram, player1));
        ShopLoadingListener.getHologramsPerShop().put(new Both<>(shop.getR(), player), hologram);
    }

    public void close() {
        getWrapperFor(shop.getT(), false).sendPacket(player);

        ShopLoadingListener.getHologramPool().remove(ShopLoadingListener.getHologramsPerShop().get(new Both<>(shop.getR(), player)));
        ShopLoadingListener.getHologramsPerShop().remove(new Both<>(shop.getR(), player));
    }

    private WrapperPlayServerBlockAction getWrapperFor(Location location, boolean open) {
        // https://wiki.vg/Protocol#Block_Action
        WrapperPlayServerBlockAction packetWrapper = new WrapperPlayServerBlockAction();
        packetWrapper.setLocation(new BlockPosition(location.getBlock().getLocation().toVector()));
        packetWrapper.setBlockType(Material.CHEST);

        // https://wiki.vg/Block_Actions#Chest
        packetWrapper.setByte1(1); // Action 1 (There is only one for a chest)
        packetWrapper.setByte2(open ? 1 : 0 ); // Action Parameter 1 (Open)

        return packetWrapper;
    }
}
