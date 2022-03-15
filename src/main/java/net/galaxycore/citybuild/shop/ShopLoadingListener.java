package net.galaxycore.citybuild.shop;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramAccessor;
import com.github.unldenis.hologram.HologramPool;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.Getter;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.utils.Both;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopLoadingListener extends BukkitRunnable implements Listener {
    @Getter
    private static final HashMap<Both<Shop, Player>, Hologram> hologramsPerShop = new HashMap<>();
    @Getter
    private static Essential essential;
    @Getter
    private static HologramPool hologramPool;
    private final HashMap<String, ShopChunkData> dataHashMap = new HashMap<>();

    public ShopLoadingListener() {
        essential = Essential.getInstance();
        hologramPool = new HologramPool(essential, 7);

        loadSnapshot();

        this.runTaskTimerAsynchronously(essential, 20 * 60, 20 * 60);
    }

    private static String getKey(Chunk chunk) {
        return chunk.getWorld().getUID() + "." + chunk.getChunkKey();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        loadChunk(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkLoadEvent event) {
        saveChunk(event.getChunk());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        to = to.toBlockLocation();
        from = from.toBlockLocation();

        if (from.distance(to) < 1)
            return;

        List<Both<Location, Shop>> shopsInDistanceTo = getShopsInDistance(to);
        List<Both<Location, Shop>> shopsInDistanceFrom = getShopsInDistance(from);

        if (shopsInDistanceTo.size() == 0 && shopsInDistanceFrom.size() == 0) return;

        AtomicReference<Stream<Both<Location, Shop>>> streamTo = new AtomicReference<>(shopsInDistanceTo.stream());
        AtomicReference<Stream<Both<Location, Shop>>> streamFrom = new AtomicReference<>(shopsInDistanceFrom.stream());
        shopsInDistanceFrom.forEach(locationShopBoth -> streamTo.set(streamTo.get().filter(filterBoth -> !filterBoth.getT().equals(locationShopBoth.getT()))));
        shopsInDistanceTo.forEach(locationShopBoth -> streamFrom.set(streamFrom.get().filter(filterBoth -> !filterBoth.getT().equals(locationShopBoth.getT()))));

        List<Both<Location, Shop>> load = streamTo.get().collect(Collectors.toList());
        List<Both<Location, Shop>> unload = streamFrom.get().collect(Collectors.toList());

        if (load.size() != 0) {
            load.forEach(locationShopBoth -> new ShopAnimation(event.getPlayer(), locationShopBoth).open());
        }

        if (unload.size() != 0) {
            unload.forEach(locationShopBoth -> new ShopAnimation(event.getPlayer(), locationShopBoth).close());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        List<Both<Location, Shop>> shopsInDistance = getShopsInDistance(event.getPlayer().getLocation());

        if (shopsInDistance.size() == 0) return;

        shopsInDistance.forEach(locationShopBoth -> hologramPool.remove(hologramsPerShop.remove(new Both<>(locationShopBoth.getR(), event.getPlayer()))));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        hologramsPerShop.values().forEach(hologram -> HologramAccessor.hide(hologram, event.getPlayer()));
    }

    private List<Both<Location, Shop>> getShopsInDistance(Location location) {
        List<Both<Location, Shop>> shops = new ArrayList<>();
        List<Chunk> chunks = List.of
                (
                        location.getChunk(),
                        location.clone().add(16, 0, -16).getChunk(),
                        location.clone().add(16, 0, 0).getChunk(),
                        location.clone().add(16, 0, 16).getChunk(),
                        location.clone().add(0, 0, -16).getChunk(),
                        location.clone().add(0, 0, 16).getChunk(),
                        location.clone().add(-16, 0, -16).getChunk(),
                        location.clone().add(-16, 0, 0).getChunk(),
                        location.clone().add(-16, 0, 16).getChunk()
                );

        chunks.forEach(chunk -> {
            if (dataHashMap.containsKey(getKey(chunk))) {
                dataHashMap.get(getKey(chunk)).getShopsInThisChunk().forEach(shop -> {
                    Location shopLocation = new Location(chunk.getWorld(), (chunk.getX() * 16) + shop.getCx(), shop.getCy(), (chunk.getZ() * 16) + shop.getCz());
                    if (shopLocation.distance(location) > 7) {
                        return;
                    }

                    shops.add(new Both<>(shopLocation, shop));
                });
            }
        });

        return shops;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String serialized = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (serialized.equalsIgnoreCase("set")) {
            Chunk chunk = event.getPlayer().getChunk();
            if (!dataHashMap.containsKey(getKey(chunk))) {
                loadChunk(chunk);
            }
            ShopChunkData shopChunkData = dataHashMap.get(getKey(chunk));
            Player player = event.getPlayer();
            Shop shop = new Shop(1, 100, player.getInventory().getItemInMainHand().serialize(), 27, player.getLocation().getBlockX() - (chunk.getX() * 16), player.getLocation().getBlockY(), player.getLocation().getBlockZ() - (chunk.getZ() * 16));
            shopChunkData.getShopsInThisChunk().add(shop);
            shopChunkData.save();
            new ShopAnimation(event.getPlayer(), new Both<>(player.getLocation().toCenterLocation(), shop)).open();
        }

        if (serialized.equalsIgnoreCase("get")) {
            Chunk chunk = event.getPlayer().getChunk();
            if (!dataHashMap.containsKey(getKey(chunk))) {
                loadChunk(chunk);
            }
            ShopChunkData shopChunkData = dataHashMap.get(getKey(chunk));
            System.out.println(shopChunkData.getShopsInThisChunk());
        }
    }

    public void saveSnapshot() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                saveChunk(loadedChunk);
            }
        }
    }

    public void loadSnapshot() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadChunk(loadedChunk);
            }
        }
    }

    private String parseName(Chunk chunk) {
        return chunk.getWorld().getName() + "." + chunk.getX() + "." + chunk.getZ();
    }

    public boolean hasChunk(Chunk chunk) {
        return new File(essential.getDataFolder(), parseName(chunk)).exists();
    }

    public void loadChunk(Chunk chunk) {
        ShopChunkData shopChunkData = new ShopChunkData(new File(essential.getDataFolder(), parseName(chunk)), chunk);
        if (dataHashMap.containsKey(getKey(chunk)) || !hasChunk(chunk)) {
            saveChunk(chunk);
        }
        shopChunkData.load();
        dataHashMap.put(getKey(chunk), shopChunkData);
    }

    public void saveChunk(Chunk chunk) {
        dataHashMap.computeIfAbsent(getKey(chunk), s -> new ShopChunkData(new File(essential.getDataFolder(), parseName(chunk)), chunk));
        ShopChunkData shopChunkData = dataHashMap.get(getKey(chunk));

        shopChunkData.save();
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        saveSnapshot();
    }
}
