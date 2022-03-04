package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.UUID;

public class PMenuPlotsMenu extends Menu {
    private final Player player;
    private final PlotAPI plotAPI;
    private final PlotPlayer<?> toOpenFor;

    public PMenuPlotsMenu(Player player, UUID toOpen) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotAPI = new PlotAPI();
        toOpenFor = plotAPI.wrapPlayer(toOpen);
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.TITLE_PLOTS.get(player);
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if (!((inventoryClickEvent.getRawSlot() >= 9) && (inventoryClickEvent.getRawSlot() < 18))) {
            return;
        }

        if(inventoryClickEvent.getCurrentItem() == null) return;
        if(inventoryClickEvent.getCurrentItem().getType() == Material.BARRIER) return;

        String worldName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(inventoryClickEvent.getCurrentItem().lore()).get(0));

        int plotX = Integer.parseInt(ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(",")[0]),
                plotZ = Integer.parseInt(ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(",")[1]);
        int offsetX, offsetY, offsetZ;
        offsetX = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_tp_offset").split("\\|")[0]);
        offsetY = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_tp_offset").split("\\|")[1]);
        offsetZ = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_tp_offset").split("\\|")[2]);

        int yaw, pitch;
        yaw = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_tp_offset").split("\\|")[3]);
        pitch = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_tp_offset").split("\\|")[4]);

        int roadWidth = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".road_width"));
        int plotWidth = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(worldName.toLowerCase() + ".plot_width"));

        int posX, posZ;
        posX = plotX * roadWidth + plotX * plotWidth + offsetX;
        posZ = plotZ * roadWidth + plotZ * plotWidth + offsetZ;


        player.teleport(new Location(Bukkit.getWorld(worldName), posX, (player.getWorld().getHighestBlockYAt(posX, posZ) + offsetY), posZ, yaw, pitch));
    }

    @Override
    public void setMenuItems() {
        if (plotAPI.wrapPlayer(player.getUniqueId()) == null) {
            inventory.setItem(13, makeItem(Material.BARRIER, i18n("error_title"), i18n("error_lore")));
            return;
        }
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, FILLER_GLASS);
        }
        boolean changed = false;
        for (Plot plot : plotAPI.getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(player.getUniqueId())))) {
            String plotID = plot.getId().toCommaSeparatedString();
            String alias = plot.getAlias();

            // Can load whatever it wants, should be in side thread
            @SuppressWarnings("deprecation") com.plotsquared.core.location.Location centerLocation = plot.getCenterSynchronous();
            org.bukkit.Location bukkitCenterLocation = Objects.requireNonNull(Bukkit.getWorld(centerLocation.getWorldName())).getHighestBlockAt(centerLocation.getX(), centerLocation.getZ()).getLocation();
            Biome biome = bukkitCenterLocation.getWorld().getBiome(bukkitCenterLocation.getBlockX(), bukkitCenterLocation.getBlockY(), bukkitCenterLocation.getBlockZ());

            inventory.addItem(makeItem(PMenuPlotInfoMenu.getBiomeMaterials().getOrDefault(biome, Material.GRASS_BLOCK), "§5" + plotID, Objects.requireNonNull(plot.getArea()).getWorldName(), alias));
            changed = true;
        }
        if (!changed) {
            inventory.setItem(13, makeItem(Material.BARRIER, i18n("no_plots_title"), i18n("no_plots_lore")));
        }
        setFillerGlass();
    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.plots." + key);
        if(i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.plots." + key);
        }
        if(i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
