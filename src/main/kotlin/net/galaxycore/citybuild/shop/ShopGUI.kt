package net.galaxycore.citybuild.shop

import lombok.Getter
import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import net.galaxycore.galaxycorecore.spice.KMenu
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

@Getter
class ShopGUI(private val player: Player, private val shop: Shop, block: Block) : KMenu() {
    init {
        item(11, Skull.ARROW_UP.getSkull(ShopI18N.get<ShopGUI>(player, "sell")))
        item(13, shop.itemStack.clone())
        item(15, Skull.ARROW_DOWN.getSkull(ShopI18N.get<ShopGUI>(player, "buy")))

        val loadedPlayer = PlayerLoader.load(player)

        if (player.hasPermission("citybuild.shop.admin") || loadedPlayer.id == shop.len)
            item(18, Material.TNT, "Settings").then {
                ShopEditGUI(player, shop, block).open(player)
            }
    }

    override fun getNameI18NKey() = ShopI18N.get<ShopGUI>(player, "title")
        .replace("%s", shop.itemStack.type.name.toLowerCase().replace("_", " ").capitalize())

    override fun getSize() = 6 * 9
}