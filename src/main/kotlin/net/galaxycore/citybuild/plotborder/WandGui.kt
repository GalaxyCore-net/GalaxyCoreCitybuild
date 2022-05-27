package net.galaxycore.citybuild.plotborder

import net.galaxycore.citybuild.utils.BorderChanger
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N
import net.galaxycore.galaxycorecore.spice.KMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class WandGui(val player: Player) : KMenu() {
    init {
        val items = listOf(
                Material.STONE to "citybuild.rand.normal",
                Material.GRANITE to "citybuild.rand.normal",
                Material.DIORITE to "citybuild.rand.normal",
                Material.ANDESITE to "citybuild.rand.normal",
                Material.DEEPSLATE to "citybuild.rand.normal",
                Material.COBBLED_DEEPSLATE to "citybuild.rand.normal",
                Material.POLISHED_DEEPSLATE to "citybuild.rand.normal",
                Material.CALCITE to "citybuild.rand.normal",
                Material.COBBLESTONE to "citybuild.rand.normal",
                Material.SAND to "citybuild.rand.normal",
                Material.DIRT to "citybuild.rand.normal",
                Material.GRAVEL to "citybuild.rand.normal",
                Material.SMOOTH_STONE to "citybuild.rand.normal",
                Material.OAK_PLANKS to "citybuild.rand.normal",
                Material.SPRUCE_PLANKS to "citybuild.rand.normal",
                Material.BIRCH_PLANKS to "citybuild.rand.normal",
                Material.JUNGLE_PLANKS to "citybuild.rand.normal",
                Material.ACACIA_PLANKS to "citybuild.rand.normal",

                null, null, null, null, null, null, null, null, null,

                Material.RED_SAND to "citybuild.rand.premium",
                Material.COAL_ORE to "citybuild.rand.premium",
                Material.CRIMSON_PLANKS to "citybuild.rand.premium",
                Material.WARPED_PLANKS to "citybuild.rand.premium",
                Material.STRIPPED_OAK_LOG to "citybuild.rand.premium",
                Material.STRIPPED_SPRUCE_LOG to "citybuild.rand.premium",
                Material.STRIPPED_BIRCH_LOG to "citybuild.rand.premium",
                Material.STRIPPED_JUNGLE_LOG to "citybuild.rand.premium",
                Material.STRIPPED_ACACIA_LOG to "citybuild.rand.premium",
                Material.STRIPPED_DARK_OAK_LOG to "citybuild.rand.troller",
                Material.CHISELED_DEEPSLATE to "citybuild.rand.troller",
                Material.POLISHED_BLACKSTONE to "citybuild.rand.troller",
                Material.GLOWSTONE to "citybuild.rand.troller",
                Material.RAW_COPPER_BLOCK to "citybuild.rand.troller",
                Material.RAW_GOLD_BLOCK to "citybuild.rand.troller",
                Material.STONE_BRICKS to "citybuild.rand.troller",
                Material.DEEPSLATE_BRICKS to "citybuild.rand.troller",
                Material.BOOKSHELF to "citybuild.rand.troller",
                Material.SMOOTH_QUARTZ to "citybuild.rand.troller",
                Material.BEDROCK to "citybuild.rand.troller",
                Material.COPPER_BLOCK to "citybuild.rand.troller",
                Material.OXIDIZED_COPPER to "citybuild.rand.troller",
                Material.IRON_BLOCK to "citybuild.rand.troller",
                Material.GOLD_BLOCK to "citybuild.rand.troller",
                Material.DIAMOND_BLOCK to "citybuild.rand.troller",
                Material.LAPIS_BLOCK to "citybuild.rand.troller",
                Material.NETHERITE_BLOCK to "citybuild.rand.troller",


                )
        for ((index, element) in items.withIndex()) {
            element?.let {
                item(index, ItemStack(element.first)).then {
                    if (!player.hasPermission(element.second)) {
                        player.sendMessage(I18N.getS(player, "citybuild.noperms"))
                        return@then
                    }
                    player.closeInventory()
                    BorderChanger.change(BorderChanger.Type.WALL, player, element.first)
                    player.sendMessage(I18N.getS(player, "citybuild.wall.changed"))
                }
            }
        }
    }

    override fun getNameI18NKey() = "wandgui.name"

    override fun getSize() = 6 * 9

}