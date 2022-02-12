package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class PMenuPlotsMenu extends Menu {
    private final Player player;
    private final PlotAPI plotAPI;

    public PMenuPlotsMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotAPI = new PlotAPI();
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.TITLE_PLOTS.get(player);
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getRawSlot() > getSlots())
            return;
        int plotX = Integer.parseInt(ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(",")[0]),
                plotZ = Integer.parseInt(ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(",")[1]);
        int offsetX, offsetY, offsetZ;
        offsetX = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_tp_offset").split("\\|")[0]);
        offsetY = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_tp_offset").split("\\|")[1]);
        offsetZ = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_tp_offset").split("\\|")[2]);

        int yaw, pitch;
        yaw = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_tp_offset").split("\\|")[3]);
        pitch = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_tp_offset").split("\\|")[4]);

        int roadWidth = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".road_width"));
        int plotWidth = Integer.parseInt(Essential.getInstance().getConfigNamespace().get(player.getWorld().getName().toLowerCase() + ".plot_width"));

        int posX, posZ;
        posX = plotX * roadWidth + plotX * plotWidth + offsetX;
        posZ = plotZ * roadWidth + plotZ * plotWidth + offsetZ;


        player.teleport(new Location(player.getWorld(), posX, (player.getWorld().getHighestBlockYAt(posX, posZ) + offsetY), posZ, yaw, pitch));
    }

    @Override
    public void setMenuItems() {
        if(plotAPI.wrapPlayer(player.getUniqueId()) == null) {
            inventory.setItem(4, makeItem(Material.BARRIER, i18n("error_title"), i18n("error_lore")));
            return;
        }
        boolean changed = false;
        for(Plot plot : plotAPI.getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(player.getUniqueId())))) {
            if(!Objects.requireNonNull(plot.getArea()).getWorldName().equals(player.getWorld().getName()))
                continue;
            String plotID = plot.getId().toCommaSeparatedString();
            inventory.addItem(makeItem(Material.GRASS_BLOCK, "§5" + plotID, Objects.requireNonNull(plot.getArea()).getWorldName()));
            changed = true;
        }
        if(!changed) {
            inventory.setItem(4, makeItem(Material.BARRIER, i18n("no_plots_title"), i18n("no_plots_lore")));
        }
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
