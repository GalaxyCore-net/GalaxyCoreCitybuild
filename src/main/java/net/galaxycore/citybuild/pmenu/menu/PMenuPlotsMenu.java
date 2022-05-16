package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.TeleportCause;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.query.PlotQuery;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class PMenuPlotsMenu extends Menu {
    public static final int PIVOT = 9 * 5;
    private final Player player;
    private final UUID toOpen;
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final List<Plot> plotList = new ArrayList<>();
    private final Logger logger = Essential.getInstance().getLogger();
    private final int size;
    private final int maxpage;
    private final boolean useUpperLine;
    int page = 0;

    public PMenuPlotsMenu(Player player, UUID toOpen) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.toOpen = toOpen;
        PlotAPI plotAPI = new PlotAPI();

        size = PlotQuery.newQuery().ownedBy(toOpen).asList().size();
        if (size <= PIVOT) {
            maxpage = 0;
        } else {
            maxpage = (size / PIVOT) + 1;
        }

        useUpperLine = size <= 9;
    }

    @Override
    public String getMenuName() {
        if (player.getUniqueId().equals(toOpen)) {
            return PMenuI18N.TITLE_PLOTS.get(player);
        } else {
            return PMenuI18N.TITLE_PLOTS_OTHER.get(player).replace("%player%", Objects.requireNonNull(Bukkit.getOfflinePlayer(toOpen).getName()));
        }
    }

    @Override
    public int getSlots() {
        if (maxpage == 0) {
            if (useUpperLine) {
                return 3 * 9;
            } else {
                return ((int) Math.ceil(size / 9F)) * 9;
            }
        } else {
            return 6 * 9;
        }
    }

    public Inventory inv() {
        this.inventory = Bukkit.createInventory(this, this.getSlots(), Component.text(this.getMenuName()));
        this.setMenuItems();

        return this.inventory;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getRawSlot() > getSlots()) return;
        if (inventoryClickEvent.getRawSlot() >= 9 * 5) {
            switch (inventoryClickEvent.getRawSlot()) {
                case 5 * 9 + 3 -> {
                    page = Math.max(0, page - 1);
                    super.open();
                }
                case 5 * 9 + 5 -> {
                    page = Math.min(maxpage - 1, page + 1);
                    super.open();
                }
            }

            return;
        }

        if (inventoryClickEvent.getCurrentItem() == null) return;
        if (inventoryClickEvent.getCurrentItem().getType() == Material.BARRIER) return;
        if (inventoryClickEvent.getCurrentItem() == FILLER_GLASS) return;

        int position = (page * PIVOT) + inventoryClickEvent.getRawSlot() - (useUpperLine ? 9 : 0);
        if (size < position) return;
        Plot plot;
        try {
            plot = plotList.get(position);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        plot.getHome(location -> {
            player.teleport(new Location(Bukkit.getWorld(location.getWorldName()), location.getX() + 0.5, location.getY(), location.getZ() + 0.5, location.getYaw(), location.getPitch()));
            PMenuI18N.TELEPORTED.send(player);
        });
    }

    @Override
    public void open() {
        new Thread(() -> {

            Set<Plot> plots = PlotQuery.newQuery().ownedBy(toOpen).asSet();
            Semaphore semaphore = new Semaphore(-plots.size() + 1);

            if (plotList.size() == 0) {
                for (Plot plot : plots) {
                    String plotID = plot.getId().toCommaSeparatedString();
                    String alias = plot.getAlias();

                    plot.getCenter(location -> {
                        Location bukkitCenterLocation = Objects.requireNonNull(Bukkit.getWorld(location.getWorldName())).getHighestBlockAt(location.getX(), location.getZ()).getLocation();
                        Biome biome = bukkitCenterLocation.getWorld().getBiome(bukkitCenterLocation.getBlockX(), bukkitCenterLocation.getBlockY(), bukkitCenterLocation.getBlockZ());
                        ItemStack itemStack = makeItem(PMenuPlotInfoMenu.getBiomeMaterials().getOrDefault(biome, Material.GRASS_BLOCK), "§5" + plotID, Objects.requireNonNull(plot.getArea()).getWorldName(), alias);
                        itemStacks.add(itemStack);
                        plotList.add(plot);
                        semaphore.release();
                    });
                }
            }

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                logger.warning("Semaphore Aquire Cancelled for Plot Loader in Task " + Thread.currentThread().getName());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    PMenuPlotsMenu.super.open();
                }
            }.runTask(Essential.getInstance());
        }).start();
    }

    @Override
    public void setMenuItems() {
        if (itemStacks.size() == 0) {
            if (player.getUniqueId().equals(toOpen)) {
                inventory.setItem(13, makeItem(Material.BARRIER, PMenuI18N.THIS_USER_DOESNT_HAVE_ANY_PLOTS.get(player)));
            } else {
                inventory.setItem(13, makeItem(Material.BARRIER, i18n("no_plots_title"), i18n("no_plots_lore")));
            }
        } else {
            if (maxpage != 0) {
                inventory.setItem(5 * 9 + 3, makeItem(Material.ARROW, PMenuI18N.PREV.get(player)));
                inventory.setItem(5 * 9 + 4, makeItem(Material.PAPER, PMenuI18N.PAGE.get(player) + (page + 1)));
                inventory.setItem(5 * 9 + 5, makeItem(Material.ARROW, PMenuI18N.NEXT.get(player)));
                inventory.setItem(5 * 9 + 6, FILLER_GLASS);
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

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.plots." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.plots." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
