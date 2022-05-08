package net.galaxycore.citybuild.pmenu.menu;

import com.sk89q.worldedit.world.biome.BiomeType;
import me.kodysimpson.menumanagersystem.menusystem.PaginatedMenu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BiomesMenu extends PaginatedMenu {

    public static final int PIVOT = 9 * 5;
    private final Player player;
    private final Collection<BiomeType> biomeTypes = new ArrayList<>();
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final int maxpage;
    private final int size;
    int page = 0;
    private final boolean useUpperLine;
    private final String menuTitle;

    public BiomesMenu(Player player, Collection<BiomeType> biomes, String menuTitle) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.menuTitle = menuTitle;
        this.biomeTypes.addAll(biomes);

        size = biomes.size();
        if (size <= PIVOT) {
            maxpage = 0;
        } else {
            maxpage = (size / PIVOT) + 1;
        }

        useUpperLine = size <= 9;
    }

    @Override
    public String getMenuName() {
        return menuTitle;
    }

    @Override
    public int getSlots() {
        if (maxpage == 0) {
            return useUpperLine ? 9 * 3 : ((int) Math.ceil(size / 9F)) * 9;
        } else {
            return 9 * 6;
        }
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

        if (inventoryClickEvent.getRawSlot() > getSlots())
            return;
        if (inventoryClickEvent.getRawSlot() >= 9 * 5) {
            switch (inventoryClickEvent.getRawSlot()) {
                case 9 * 5 + 3 -> page = Math.max(0, page - 1);
                case 9 * 5 + 5 -> page = Math.min(maxpage - 1, page + 1);
            }
            super.open();
        }

    }

    @Override
    public void open() {
        new Thread(() -> {

            Semaphore semaphore = new Semaphore(-biomeTypes.size() + 1);

            for (BiomeType type : biomeTypes) {
                Biome biome = Biome.valueOf(type.getId().split(":")[1].toUpperCase());
                ItemStack itemStack = makeItem(PMenuPlotInfoMenu.getBiomeMaterials().getOrDefault(biome, Material.GRASS_BLOCK), "§5" + type.getId().split(":")[1]);
                itemStacks.add(itemStack);
                semaphore.release();
            }

            try {
                semaphore.acquire();
            } catch (InterruptedException exc) {
                Essential.getInstance().getLogger().warning("Semaphore Acquire cancelled for Plot Loader in Task " + Thread.currentThread().getName());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    BiomesMenu.super.open();
                }
            }.runTask(Essential.getInstance());

        }).start();
    }

    @Override
    public void setMenuItems() {

        if (itemStacks.size() == 0) {
            inventory.setItem(13, makeItem(Material.BARRIER,
                    "§cERROR"/*I can hardcode this because it works just like the normal i18n method in other menus when there is an UNEXPECTED error*/));
        } else {
            if (maxpage != 0) {
                inventory.setItem(9 * 5 + 3, makeItem(Material.ARROW, PMenuI18N.PREV.get(player)));
                inventory.setItem(9 * 5 + 4, makeItem(Material.PAPER, PMenuI18N.PAGE.get(player) + (page + 1)));
                inventory.setItem(9 * 5 + 5, makeItem(Material.ARROW, PMenuI18N.NEXT.get(player)));
                inventory.setItem(9 * 5 + 6, FILLER_GLASS);
            }
            if (!useUpperLine) {
                for (int i = 0; i < PIVOT; i++) {
                    if (size > page * PIVOT + i)
                        inventory.setItem(i, itemStacks.get(page * PIVOT + i));
                }
                setFillerGlass();
                return;
            }

            for (int i = 0; i < 9; i++) {
                if (size > i)
                    inventory.setItem(9 + i, itemStacks.get(page * PIVOT + i));
            }
        }
        setFillerGlass();

    }

}
