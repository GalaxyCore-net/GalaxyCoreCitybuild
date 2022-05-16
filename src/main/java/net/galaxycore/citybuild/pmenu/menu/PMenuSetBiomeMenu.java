package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.biome.BiomeTypes;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuSetBiomeMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> plotPlayer;
    private final String optionalBiome;

    public PMenuSetBiomeMenu(Player player, String optionalBiome) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
        this.optionalBiome = optionalBiome;
    }

    @Override
    public String getMenuName() {
        return i18n("title");
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {

        Plot plot = plotPlayer.getCurrentPlot();
        if (plot == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, PMenuI18N.NOT_ON_PLOT.get(player)));
            return;
        }

        if (plot.getOwner() != player.getUniqueId() && !player.hasPermission("plots.admin.command.setbiome")) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, PMenuI18N.NOT_YOUR_PLOT.get(player)));
            return;
        }

        BiomeType biome = null;
        try {
            biome = BiomeTypes.get(optionalBiome.toLowerCase());
        } catch (Exception ignore) {
        }

        if (biome == null) {
            player.sendMessage(Component.text(i18n("need_biome")));
            if (BiomeType.REGISTRY.values().size() < 9 * 3) {
                int index = 0;
                for (BiomeType type : BiomeType.REGISTRY.values()) {
                    String name = type.getId().split(":")[1];
                    inventory.setItem(index, makeItem(Material.GRASS_BLOCK, name));
                    index++;
                }
            } else {
                new BiomesMenu(player, BiomeType.REGISTRY.values(), i18n("title")).open();
            }
            return;
        }

        if (plot.getRunning() > 0) {
            player.sendMessage(Component.text(i18n("wait_for_timer")));
            return;
        }

        if (plot.getVolume() > Integer.MAX_VALUE) {
            player.sendMessage(Component.text(i18n("schematic_too_large")));
            return;
        }

        plot.addRunning();
        plot.getPlotModificationManager().setBiome(biome, () -> {
            plot.removeRunning();
            player.sendMessage(i18n("biome_set_to") + optionalBiome.toLowerCase());
        });
        setFillerGlass();

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.setbiome." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.setbiome." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
