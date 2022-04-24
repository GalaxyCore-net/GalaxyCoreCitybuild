package net.galaxycore.citybuild.shop

import com.comphenix.packetwrapper.WrapperPlayServerBlockAction
import com.comphenix.protocol.wrappers.BlockPosition
import com.github.unldenis.hologram.Hologram
import com.github.unldenis.hologram.HologramAccessor
import com.github.unldenis.hologram.animation.Animation
import lombok.Getter
import net.galaxycore.citybuild.utils.Both
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

@Getter
class ShopAnimation(private val player: Player, private val shop: Both<Location, Shop>) {
    fun open() {
        getWrapperFor(shop.t, true).sendPacket(player)
        val hologram = Hologram.builder()
                .location(shop.t.toCenterLocation().subtract(0.0, 1.0, 0.0))
                .addLine(shop.r.itemStack)
                .build(ShopListener.hologramPool)
        hologram.setAnimation(0, Animation.CIRCLE)
        Bukkit.getOnlinePlayers().forEach { player1: Player? -> HologramAccessor.hide(hologram, player1) }
        ShopListener.animationData[Both(player, shop.r)] = hologram
    }

    fun close() {
        getWrapperFor(shop.t, false).sendPacket(player)
        try {
            ShopListener.animationData.keys.filter { it.t == player && it.r == shop.r }[0].let {
                ShopListener.hologramPool.remove(ShopListener.animationData[it]!!)
                ShopListener.animationData.remove(it)
            }
        }catch (_: Exception) {}
    }

    private fun getWrapperFor(location: Location, open: Boolean): WrapperPlayServerBlockAction {
        // https://wiki.vg/Protocol#Block_Action
        val packetWrapper = WrapperPlayServerBlockAction()
        packetWrapper.location = BlockPosition(location.block.location.toVector())
        packetWrapper.blockType = Material.CHEST

        // https://wiki.vg/Block_Actions#Chest
        packetWrapper.byte1 = 1 // Action 1 (There is only one for a chest)
        packetWrapper.byte2 = if (open) 1 else 0 // Action Parameter 1 (Open)
        return packetWrapper
    }
}