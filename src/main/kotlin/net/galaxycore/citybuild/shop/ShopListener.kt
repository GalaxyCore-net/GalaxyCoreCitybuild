package net.galaxycore.citybuild.shop

import com.github.unldenis.hologram.Hologram
import com.github.unldenis.hologram.HologramPool
import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.utils.Both
import net.galaxycore.galaxycorecore.configuration.PlayerLoader
import net.galaxycore.galaxycorecore.spice.KBlockData
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShopListener : Listener {

    init {
        essential = Essential.getInstance()
        hologramPool = HologramPool(essential, 7.0)
    }

    private fun onCreate(event: PlayerInteractEvent) {
        val location = event.clickedBlock!!.location.toCenterLocation()
        if (getShopsInDistance(location.toBlockLocation(), 0.5).isNotEmpty()) {
            event.player.sendMessage(ShopI18N.get<ShopListener>(event.player, "shopalreadyinarea"))
            return
        }

        // TODO: check if player is allowed to create another shop
        val chunk = event.player.chunk

        // Check if Player has non-null and non-air item in hand
        if (event.player.inventory.itemInMainHand.type == Material.AIR) {
            event.player.sendMessage(ShopI18N.get<ShopListener>(event.player, "needtoholdanitem"))
            return
        }

        ShopCreateGUI(event.player, event.player.inventory.itemInMainHand.clone()).open{
            val player = event.player
            val loadedPlayer: PlayerLoader = PlayerLoader.load(player)
            val shop = Shop(loadedPlayer.id, it.price, player.inventory.itemInMainHand, 0, location.blockX - chunk.x * 16, location.blockY, location.blockZ - chunk.z * 16, it.state.value)
            ShopAnimation(event.player, Both(location, shop)).open()
            val blockdata = KBlockData(event.clickedBlock!!, Essential.getInstance())
            blockdata.set(namespacedKey, PersistentDataType.BYTE_ARRAY, shop.compact())
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock == null) return
        if (!listOf(Material.CHEST, Material.TRAPPED_CHEST).contains(event.clickedBlock!!.type)) return
        if (event.player.isSneaking) {
            event.isCancelled = true
            onCreate(event)
            return
        }
        val location = event.clickedBlock!!.location
        val shopsInDistance = getShopsInDistance(location, 0.5)
        if (shopsInDistance.isEmpty()) return
        val shop = shopsInDistance[0]
        event.isCancelled = true
        val loadedPlayer: PlayerLoader = PlayerLoader.load(event.player)
        if (shop.r.player == loadedPlayer.id) {
            ShopEditGUI(event.player, shop.r, shop.t.block).open(event.player)
        } else {
            ShopGUI(event.player, shop.r).open()
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        var from = event.from
        var to = event.to
        to = to.toBlockLocation()
        from = from.toBlockLocation()
        if (event.player.isSneaking) {
            event.player.sendActionBar(Component.text(ShopI18N.get<ShopListener>(event.player, "sneakandinteracttocreateashop")))
        }
        if (from.distance(to) < 1) return
        val shopsInDistanceTo = getShopsInDistance(to, 7.0)
        val shopsInDistanceFrom = getShopsInDistance(from, 7.0)
        if (shopsInDistanceTo.isEmpty() && shopsInDistanceFrom.isEmpty()) return
        val streamTo = AtomicReference(shopsInDistanceTo.stream())
        val streamFrom = AtomicReference(shopsInDistanceFrom.stream())
        shopsInDistanceFrom.forEach(Consumer { locationShopBoth: Both<Location, Shop> -> streamTo.set(streamTo.get().filter { filterBoth: Both<Location, Shop> -> filterBoth.t != locationShopBoth.t }) })
        shopsInDistanceTo.forEach(Consumer { locationShopBoth: Both<Location, Shop> -> streamFrom.set(streamFrom.get().filter { filterBoth: Both<Location, Shop> -> filterBoth.t != locationShopBoth.t }) })
        val load = streamTo.get().collect(Collectors.toList())
        val unload = streamFrom.get().collect(Collectors.toList())
        if (load.size != 0) {
            load.forEach(Consumer { locationShopBoth: Both<Location, Shop>? -> ShopAnimation(event.player, locationShopBoth!!).open() })
        }
        if (unload.size != 0) {
            unload.forEach(Consumer { locationShopBoth: Both<Location, Shop>? -> ShopAnimation(event.player, locationShopBoth!!).close() })
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockEvent) {
        if (event.block.type != Material.CHEST && event.block.type != Material.TRAPPED_CHEST) return
        val shopsInDistance = getShopsInDistance(event.block.location, 0.5)
        if (shopsInDistance.isEmpty()) return
        val shop = shopsInDistance[0]
        val blockData = KBlockData(event.block, Essential.getInstance())
        blockData.remove(namespacedKey)
        event.block.location.getNearbyPlayers(7.0).forEach {
            ShopAnimation(it, shop).close()
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val shopsInDistance = getShopsInDistance(event.player.location, 7.0)
        if (shopsInDistance.isEmpty()) return
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        onMove(PlayerMoveEvent(event.player, event.player.world.getBlockAt(0, 0, 0).location, event.player.location))
    }

    private fun getShopsInDistance(location: Location, distance: Double): List<Both<Location, Shop>> {
        val shops: MutableList<Both<Location, Shop>> = ArrayList()
        val chunks = listOf(
                location.chunk,
                location.clone().add(16.0, 0.0, -16.0).chunk,
                location.clone().add(16.0, 0.0, 0.0).chunk,
                location.clone().add(16.0, 0.0, 16.0).chunk,
                location.clone().add(0.0, 0.0, -16.0).chunk,
                location.clone().add(0.0, 0.0, 16.0).chunk,
                location.clone().add(-16.0, 0.0, -16.0).chunk,
                location.clone().add(-16.0, 0.0, 0.0).chunk,
                location.clone().add(-16.0, 0.0, 16.0).chunk
        )

        val essential: Essential = Essential.getInstance()
        val data = KBlockData(location.block, essential)

        chunks.forEach(Consumer { chunk ->
            data.getBlocksWithCustomData(essential, chunk)?.forEach {
                if (it == null) return@forEach
                val shop = KBlockData(it, essential)
                val realShopLocation = it.location
                if (realShopLocation.distance(location) <= distance) {
                    val raw = shop.get(namespacedKey, PersistentDataType.BYTE_ARRAY) ?: return@forEach
                    try {
                        shops.add(Both(it.location, Shop.disect(raw)))
                    } catch (e: Exception) {
                        println("Error while disecting shop at ${it.location}. Probably someone tampered with the data. Please don't do that. That's evil. Like you.")
                        e.printStackTrace()
                    }
                }
            }
        })
        return shops
    }

    companion object {
        lateinit var essential: Essential
        lateinit var hologramPool: HologramPool
        var animationData = HashMap<Both<Player, Shop>, Hologram>()
        val namespacedKey = NamespacedKey(Essential.getInstance(), "shops")
    }
}