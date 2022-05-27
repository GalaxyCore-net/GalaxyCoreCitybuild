package net.galaxycore.citybuild.plotborder

import net.galaxycore.citybuild.utils.BorderChanger
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import net.galaxycore.galaxycorecore.spice.KMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RandGui(val player: Player) : KMenu() {
    init {
        val items = listOf(
                Material.OAK_SLAB to "citybuild.rand.normal",
                Material.SPRUCE_SLAB to "citybuild.rand.normal",
                Material.BIRCH_SLAB to "citybuild.rand.normal",
                Material.STONE_SLAB to "citybuild.rand.normal",
                Material.COBBLESTONE_SLAB to "citybuild.rand.normal",
                Material.BRICK_SLAB to "citybuild.rand.normal",
                Material.STONE_BRICK_SLAB to "citybuild.rand.normal",
                Material.RED_NETHER_BRICK_SLAB to "citybuild.rand.normal",
                Material.SMOOTH_STONE to "citybuild.rand.normal",
                Material.SMOOTH_SANDSTONE to "citybuild.rand.premium",
                Material.SMOOTH_QUARTZ to "citybuild.rand.premium",
                Material.SMOOTH_RED_SANDSTONE to "citybuild.rand.premium",
                Material.MOSSY_COBBLESTONE_SLAB to "citybuild.rand.premium",
                Material.END_STONE_BRICK_SLAB to "citybuild.rand.premium",
                Material.SMOOTH_STONE_SLAB to "citybuild.rand.premium",
                Material.CUT_COPPER_SLAB to "citybuild.rand.premium",
                Material.OXIDIZED_CUT_COPPER_SLAB to "citybuild.rand.premium",
                Material.DEEPSLATE_BRICK_SLAB to "citybuild.rand.premium",
                Material.CUT_RED_SANDSTONE_SLAB to "citybuild.rand.troller",
                Material.CUT_SANDSTONE_SLAB to "citybuild.rand.troller",
                Material.PURPUR_SLAB to "citybuild.rand.troller",
                Material.PRISMARINE_SLAB to "citybuild.rand.troller",
                Material.PRISMARINE_BRICK_SLAB to "citybuild.rand.troller",
                Material.DARK_PRISMARINE_SLAB to "citybuild.rand.troller",
                Material.POLISHED_GRANITE_SLAB to "citybuild.rand.troller",
                Material.POLISHED_DIORITE_SLAB to "citybuild.rand.troller",
                Material.POLISHED_ANDESITE_SLAB to "citybuild.rand.troller",
                null, null, null, null, null, null, null, null, null,
                Material.OAK_LOG to "citybuild.rand.normal",
                Material.SPRUCE_LOG to "citybuild.rand.normal",
                Material.BIRCH_LOG to "citybuild.rand.normal",
                Material.JUNGLE_LOG to "citybuild.rand.normal",
                Material.ACACIA_LOG to "citybuild.rand.normal",
                Material.WAXED_COPPER_BLOCK to "citybuild.rand.normal",
                Material.NETHERITE_BLOCK to "citybuild.rand.normal",
                Material.GLOWSTONE to "citybuild.rand.normal",
                Material.QUARTZ_BLOCK to "citybuild.rand.normal",
                Material.CRIMSON_NYLIUM to "citybuild.rand.premium",
                Material.WARPED_NYLIUM to "citybuild.rand.premium",
                Material.WAXED_WEATHERED_COPPER to "citybuild.rand.premium",
                Material.IRON_BLOCK to "citybuild.rand.premium",
                Material.GOLD_BLOCK to "citybuild.rand.premium",
                Material.DIAMOND_BLOCK to "citybuild.rand.premium",
                Material.EMERALD_BLOCK to "citybuild.rand.premium",
                Material.LAPIS_BLOCK to "citybuild.rand.premium",
                Material.BEACON to "citybuild.rand.premium",

                )
        for ((index, element) in items.withIndex()) {
            element?.let {
                item(index, ItemStack(element.first)).then {
                    if (!player.hasPermission(element.second)) {
                        player.sendMessage(I18N.getS(player, "citybuild.noperms"))
                        return@then
                    }
                    player.closeInventory()
                    BorderChanger.change(BorderChanger.Type.BORDER, player, element.first)
                    player.sendMessage(I18N.getS(player, "citybuild.border.changed"))
                }
            }
        }
    }

    override fun getNameI18NKey() = "randgui.name"

    override fun getSize() = 6 * 9

}