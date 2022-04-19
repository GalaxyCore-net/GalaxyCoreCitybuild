package net.galaxycore.citybuild.shop

import com.github.unldenis.hologram.Hologram
import com.github.unldenis.hologram.HologramAccessor
import com.github.unldenis.hologram.HologramPool
import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.utils.Both
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import java.util.stream.Collectors

class ShopListener : BukkitRunnable(), Listener {
    private val dataHashMap = HashMap<String, ShopChunkData>()

    init {
        essential = Essential.getInstance()
        hologramPool = HologramPool(essential, 7.0)
        hologramsPerShop = HashMap()
        loadSnapshot()
        runTaskTimerAsynchronously(essential, (20 * 60).toLong(), (20 * 60).toLong())
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        loadChunk(event.chunk)
    }

    @EventHandler
    fun onChunkUnload(event: ChunkLoadEvent) {
        saveChunk(event.chunk)
    }

    private fun onCreate(event: PlayerInteractEvent) {
        val location = event.clickedBlock!!.location.toCenterLocation()
        if (getShopsInDistance(location.toBlockLocation(), 0.5).isNotEmpty()) {
            event.player.sendMessage(ChatColor.RED.toString() + "There is already a shop in this area!")
            return
        }

        // TODO: check if player is allowed to create another shop
        val chunk = event.player.chunk
        if (!dataHashMap.containsKey(getKey(chunk))) {
            loadChunk(chunk)
        }
        ShopCreateGUI(event.player).open()
        val shopChunkData = dataHashMap[getKey(chunk)]
        val player = event.player
        val shop = Shop(1, 100, player.inventory.itemInMainHand.serialize(), 27, location.blockX - chunk.x * 16, location.blockY, location.blockZ - chunk.z * 16)
        shopChunkData!!.shopsInThisChunk += shop
        shopChunkData!!.save()
        ShopAnimation(event.player, Both(location, shop)).open()
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
        val chunk = location.chunk
        if (!dataHashMap.containsKey(getKey(chunk))) {
            loadChunk(chunk)
        }
        val shopsInDistance = getShopsInDistance(location, 0.5)
        if (shopsInDistance.isEmpty()) return
        val shop = shopsInDistance[0]
        event.isCancelled = true
        ShopGUI(event.player, shop.r).open()
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        var from = event.from
        var to = event.to
        to = to.toBlockLocation()
        from = from.toBlockLocation()
        if (event.player.isSneaking) {
            event.player.sendActionBar(Component.text(ChatColor.GREEN.toString() + "Sneak and interact with a chest to create a shop!"))
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
    fun onQuit(event: PlayerQuitEvent) {
        val shopsInDistance = getShopsInDistance(event.player.location, 7.0)
        if (shopsInDistance.isEmpty()) return
        shopsInDistance.forEach(Consumer { locationShopBoth: Both<Location, Shop> -> hologramPool.remove(hologramsPerShop.remove(Both(locationShopBoth.r, event.player))!!) })
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        hologramsPerShop.values.forEach(Consumer { hologram: Hologram? -> HologramAccessor.hide(hologram, event.player) })
        onMove(PlayerMoveEvent(event.player, event.player.location, event.player.location))
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
        chunks.forEach(Consumer { chunk: Chunk ->
            if (dataHashMap.containsKey(getKey(chunk))) {
                dataHashMap[getKey(chunk)]!!.shopsInThisChunk.forEach(Consumer Shopper@ { shop: Shop ->
                    val shopLocation = Location(chunk.world, (chunk.x * 16 + shop.cx).toDouble(), shop.cy.toDouble(), (chunk.z * 16 + shop.cz).toDouble())
                    if (shopLocation.toBlockLocation().distance(location) > distance) {
                        return@Shopper
                    }
                    shops.add(Both(shopLocation, shop))
                })
            }
        })
        return shops
    }

    fun saveSnapshot() {
        for (world in Bukkit.getWorlds()) {
            for (loadedChunk in world.loadedChunks) {
                saveChunk(loadedChunk)
            }
        }
    }

    private fun loadSnapshot() {
        for (world in Bukkit.getWorlds()) {
            for (loadedChunk in world.loadedChunks) {
                loadChunk(loadedChunk)
            }
        }
    }

    private fun parseName(chunk: Chunk): String {
        return chunk.world.name + "." + chunk.x + "." + chunk.z
    }

    private fun hasChunk(chunk: Chunk): Boolean {
        return File(essential.dataFolder, parseName(chunk)).exists()
    }

    private fun loadChunk(chunk: Chunk) {
        val shopChunkData = ShopChunkData(File(essential.dataFolder, parseName(chunk)), chunk)
        if (!hasChunk(chunk)) {
            saveChunk(chunk)
        }
        shopChunkData.load()
        dataHashMap[getKey(chunk)] = shopChunkData
    }

    private fun saveChunk(chunk: Chunk) {
        dataHashMap.computeIfAbsent(getKey(chunk)) { ShopChunkData(File(essential.dataFolder, parseName(chunk)), chunk) }
        val shopChunkData = dataHashMap[getKey(chunk)]
        shopChunkData!!.save()
    }

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see Thread.run
     */
    override fun run() {
        saveSnapshot()
    }

    companion object {
        lateinit var hologramsPerShop: HashMap<Both<Shop, Player>, Hologram>
        lateinit var essential: Essential
        lateinit var hologramPool: HologramPool
        fun getKey(chunk: Chunk): String {
            return chunk.world.uid.toString() + "." + chunk.chunkKey
        }
    }
}