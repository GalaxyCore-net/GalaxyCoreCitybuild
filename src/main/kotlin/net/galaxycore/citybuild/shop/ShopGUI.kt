package net.galaxycore.citybuild.shop

import lombok.Getter
import lombok.SneakyThrows
import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.apiutils.CoreProvider
import net.galaxycore.galaxycorecore.coins.CoinDAO
import net.galaxycore.galaxycorecore.coins.PartnerTransactionError
import net.galaxycore.galaxycorecore.coins.PlayerTransactionError
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import net.galaxycore.galaxycorecore.spice.KMenu
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*

@Getter
class ShopGUI(private val player: Player, private val shop: Shop, block: Block) : KMenu() {
    private val amount = Reactive(shop.len)

    init {
        if (shop.state == Shop.STATE_BUY || shop.state == Shop.STATE_BTH)
            item(11, Skull.ARROW_UP.getSkull(ShopI18N.get<ShopGUI>(player, "sell"),
                ShopI18N.get<ShopGUI>(player, "sell.l1"),
                ShopI18N.get<ShopGUI>(player, "sell.l2"))).then { kMenuItem ->
                var price = 0L
                val dao = CoinDAO(PlayerLoader.load(player), Essential.getInstance())
                val shopOwner = buildLoader(shop.player)
                val ownerDAO = CoinDAO(shopOwner, Essential.getInstance())
                if (howManyItemsOfTypeHasPlayer() < shop.itemStack.amount ) {
                    player.sendMessage(ShopI18N.get<ShopRefillGUI>(player, "notenoughitems"))
                    return@then
                }
                if (ownerDAO.get() < shop.price) {
                    player.sendMessage(ShopI18N.get<ShopRefillGUI>(player, "notenoughcoinsother").replace("%s", shopOwner!!.lastname))
                    return@then
                }
                if (kMenuItem.clickType == ClickType.SHIFT_LEFT || kMenuItem.clickType == ClickType.SHIFT_RIGHT) {
                    while (true) {
                        val playerHas = howManyItemsOfTypeHasPlayer()
                        price += shop.price
                        if (playerHas < shop.itemStack.amount || ownerDAO.get() < price) {
                            price -= shop.price
                            break
                        }

                        val amountToInsert = shop.itemStack.amount

                        removeAmountOfItemFromPlayerInventory(amountToInsert)
                        amount.update {
                            it + amountToInsert
                        }
                    }
                } else {
                    val amountToInsert = shop.itemStack.amount
                    price += shop.price

                    removeAmountOfItemFromPlayerInventory(amountToInsert)
                    amount.update {
                        it + amountToInsert
                    }
                }

                try {
                    dao.transact(shopOwner, -price, "SHOP:INSERT:${block.location.toBlockKey()}:${amount.value}:TIME:${System.currentTimeMillis()}")
                } catch (playerTransactionException: PlayerTransactionError) {
                    player.sendMessage(ShopI18N.get<ShopRefillGUI>(player, "notenoughcoins"))
                    return@then
                } catch (partnerTransactionException: PartnerTransactionError) {
                    Essential.getInstance().logger.warning(String.format("%s tried to buy from %s but failed", player.name, shopOwner!!.lastname))
                    player.sendMessage("§cERROR")
                    return@then
                }

                player.playNote(player.location, Instrument.CHIME, Note.natural(1, Note.Tone.A))

            }
        item(13, shop.itemStack.clone())

        if (shop.state == Shop.STATE_BUY || shop.state == Shop.STATE_BTH)
            item(15, Skull.ARROW_DOWN.getSkull(ShopI18N.get<ShopGUI>(player, "buy"),
                ShopI18N.get<ShopGUI>(player, "buy.l1"),
                ShopI18N.get<ShopGUI>(player, "buy.l2"))).then {

            }

        val loadedPlayer = PlayerLoader.load(player)

        if (player.hasPermission("citybuild.shop.admin") || loadedPlayer.id == shop.len)
            item(18, Material.TNT, ShopI18N.get<ShopGUI>(player, "settings")).then {
                ShopEditGUI(player, shop, block).open(player)
            }

        amount.updatelistener {

        }
    }

    private fun howManyItemsOfTypeHasPlayer(): Int {
        var count = 0
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(shop.itemStack)) {
                count += itemStack.amount
            }
        }
        return count
    }

    private fun howManySpacesAreLeftForItemStack(): Int {
        var count = 0
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(shop.itemStack)) {
                count += shop.itemStack.maxStackSize - itemStack.amount
            } else if (itemStack == null) {
                count += shop.itemStack.maxStackSize
            }
        }
        return count
    }

    private fun removeAmountOfItemFromPlayerInventory(amount: Int) {
        var count = amount
        for (itemStack in player.inventory.contents) {
            if (itemStack != null && itemStack.isSimilar(shop.itemStack)) {
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

    @SneakyThrows
    private fun buildLoader(id: Int): PlayerLoader? {
        val core = CoreProvider.getCore()
        val connection = core.databaseConfiguration.connection
        val load = connection.prepareStatement("SELECT * FROM core_playercache WHERE id = ?")
        load.setInt(1, id)
        val loadResult = load.executeQuery()
        if (!loadResult.next()) {
            loadResult.close()
            load.close()
            return null
        }
        val playerLoader = PlayerLoader(
            loadResult.getInt("id"),
            UUID.fromString(loadResult.getString("uuid")),
            loadResult.getString("lastname"),
            parse(loadResult, "firstlogin"),
            parse(loadResult, "lastlogin"),
            parse(loadResult, "last_daily_reward"),
            loadResult.getInt("banpoints"),
            loadResult.getInt("mutepoints"),
            loadResult.getInt("warnpoints"),
            loadResult.getInt("reports"),
            loadResult.getBoolean("teamlogin"),
            loadResult.getBoolean("debug"),
            loadResult.getBoolean("socialspy"),
            loadResult.getBoolean("commandspy"),
            loadResult.getBoolean("vanished"),
            loadResult.getBoolean("nicked"),
            loadResult.getInt("lastnick"),
            loadResult.getLong("coins")
        )
        loadResult.close()
        load.close()
        val update = connection.prepareStatement(
            "UPDATE core_playercache SET lastname=?, lastlogin=CURRENT_TIMESTAMP WHERE id=?"
        )
        update.setString(1, playerLoader.lastname)
        update.setInt(2, playerLoader.id)
        update.executeUpdate()
        update.close()
        return playerLoader
    }

    @SneakyThrows
    private fun parse(resultSet: ResultSet, field: String): Date? {
        return if (CoreProvider.getCore().databaseConfiguration.internalConfiguration.connection == "sqlite") {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultSet.getString(field))
        } else resultSet.getDate(field)
    }

    override fun getNameI18NKey() = ShopI18N.get<ShopGUI>(player, "title")
        .replace("%s",
            shop.itemStack.type.name.toLowerCase().split("_").joinToString(" ") { it.capitalize() })

    override fun getSize() = 3 * 9
}