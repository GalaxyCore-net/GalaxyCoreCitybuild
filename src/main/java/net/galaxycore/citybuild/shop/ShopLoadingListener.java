package net.galaxycore.citybuild.shop;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.galaxycore.citybuild.Essential;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class ShopLoadingListener extends BukkitRunnable implements Listener{
    private final Essential essential;
    private final HashMap<String, ShopChunkData> dataHashMap = new HashMap<>();

    public ShopLoadingListener() {
        essential = Essential.getInstance();

        loadSnapshot();

        this.runTaskTimerAsynchronously(essential, 20*60, 20*60);
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
    public void onChat(AsyncChatEvent event) {
        String serialized = PlainComponentSerializer.plain().serialize(event.message());

        if(serialized.equalsIgnoreCase("set")) {
            Chunk chunk = event.getPlayer().getChunk();
            if(!dataHashMap.containsKey(getKey(chunk))) {loadChunk(chunk);}
            ShopChunkData shopChunkData = dataHashMap.get(getKey(chunk));
            Player player = event.getPlayer();
            shopChunkData.getShopsInThisChunk().add(new Shop(1, 100, player.getInventory().getItemInMainHand().serialize(), 27, player.getLocation().getBlockX() - (chunk.getX() * 16), player.getLocation().getBlockY(), player.getLocation().getBlockZ() - (chunk.getZ() * 16)));
            shopChunkData.save();
        }

        if(serialized.equalsIgnoreCase("get")) {
            Chunk chunk = event.getPlayer().getChunk();
            if(!dataHashMap.containsKey(getKey(chunk))) {loadChunk(chunk);}
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
        if(dataHashMap.containsKey(getKey(chunk)) || !hasChunk(chunk)) {
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

    private static String getKey(Chunk chunk) {
        return chunk.getWorld().getUID() + "." + chunk.getChunkKey();
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
