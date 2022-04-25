package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.spice.KBlockData
import net.galaxycore.galaxycorecore.spice.KMenu
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import net.kyori.adventure.text.Component
import org.bukkit.Instrument
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.lang.Math.floorDiv

class ShopRefillGUI(val player: Player, private val r: Shop, private val block: Block) : KMenu() {
    private val amount = Reactive(0)

    init {
        item(
            11,
            Skull.ARROW_UP.getSkull(
                ShopI18N.get<ShopRefillGUI>(player, "deposit"),
                ShopI18N.get<ShopRefillGUI>(player, "deposit.l1"),
                ShopI18N.get<ShopRefillGUI>(player, "deposit.l2")
            )
        ).then { kMenuItem ->
            val playerHas = howManyItemsOfTypeHasPlayer()
            if (playerHas < r.itemStack.amount) {
                player.sendMessage(ShopI18N.get<ShopRefillGUI>(player, "notenoughitems"))
                return@then
            }

            var amountToInsert = r.itemStack.amount
            if (kMenuItem.clickType == ClickType.SHIFT_LEFT || kMenuItem.clickType == ClickType.SHIFT_RIGHT) {
                amountToInsert = floorDiv(playerHas, r.itemStack.amount)
            }

            removeAmountOfItemFromPlayerInventory(amountToInsert)
            amount.update {
                it + amountToInsert
            }
            player.playNote(player.location, Instrument.CHIME, Note.natural(1, Note.Tone.A))
        }

        val amountItem = item(13, r.itemStack.clone())

        item(
            15,
            Skull.ARROW_DOWN.getSkull(
                ShopI18N.get<ShopRefillGUI>(player, "withdraw"),
                ShopI18N.get<ShopRefillGUI>(player, "withdraw.l1"),
                ShopI18N.get<ShopRefillGUI>(player, "withdraw.l2")
            )
        ).then { kMenuItem ->
            val playerCan = howManySpacesAreLeftForItemStack()
            if (r.len == 0) {
                player.sendMessage(ShopI18N.get<ShopRefillGUI>(player, "notenoughitems"))
                return@then
            }

            var amountToWithdraw = r.itemStack.amount
            println("playerCan: $playerCan")
            if (kMenuItem.clickType == ClickType.SHIFT_LEFT || kMenuItem.clickType == ClickType.SHIFT_RIGHT) {
                amountToWithdraw = floorDiv(playerCan, r.itemStack.amount)*r.itemStack.amount
            }

            println("amountToWithdraw: $amountToWithdraw")

            amountToWithdraw = amountToWithdraw.coerceAtMost(r.len)

            println("amountToWithdraw2: $amountToWithdraw")

            amount.update {
                it - amountToWithdraw
            }

            var wholeStacks = floorDiv(amountToWithdraw, r.itemStack.maxStackSize)
            val remainder = amountToWithdraw % r.itemStack.maxStackSize

            while (wholeStacks > 0) {
                player.inventory.addItem(r.itemStack.clone().asQuantity(r.itemStack.maxStackSize))
                wholeStacks--
            }

            if (remainder > 0) {
                player.inventory.addItem(r.itemStack.clone().asQuantity(remainder))
            }

            player.playNote(player.location, Instrument.CHIME, Note.natural(1, Note.Tone.A))
        }

        amount.updatelistener {am ->
            amountItem.itemStack.update { itemStack ->
                itemStack.editMeta {
                    it.displayName(Component.text(ShopI18N.get<ShopEditGUI>(player, "current").replace("%d", am.toString())))
                }
                itemStack
            }

            val blockData = KBlockData(block, Essential.getInstance())
            r.len = am
            r.compact(blockData)
        }
    }

    private fun howManyItemsOfTypeHasPlayer(): Int {
        var count = 0
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(r.itemStack)) {
                count += itemStack.amount
            }
        }
        return count
    }

    private fun howManySpacesAreLeftForItemStack(): Int {
        var count = 0
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(r.itemStack)) {
                count += r.itemStack.maxStackSize - itemStack.amount
            } else if (itemStack == null) {
                count += r.itemStack.maxStackSize
            }
        }
        return count
    }

    private fun removeAmountOfItemFromPlayerInventory(amount: Int) {
        var count = amount
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(r.itemStack)) {
                if (itemStack.amount > count) {
                    itemStack.amount -= count
                    break
                } else {
                    count -= itemStack.amount
                    player.inventory.remove(itemStack)
                }
            }
        }
    }

    override fun getNameI18NKey() = "citybuild.shop.ShopRefillGUI.title"
    override fun getSize() = 9 * 3

    fun open() {
        open(player)
        amount.setItem(r.len)
    }
}