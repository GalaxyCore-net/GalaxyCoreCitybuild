package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuClaimMenu extends Menu {

    private final Player player;
    private final PlotAPI plotAPI;

    public PMenuClaimMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotAPI = new PlotAPI();
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

        PlotPlayer<?> plotPlayer = plotAPI.wrapPlayer(player.getUniqueId());
        if (plotPlayer == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("fipoewjfweopijfewpoijfewpoifjweopfijwe"))); // Key won't be found, so it will say "§c§lERROR"
            return;
        }
        Plot currentPlot = plotPlayer.getCurrentPlot();

        if (currentPlot == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_on_plot")));
            return;
        }

        if (currentPlot.getOwner() != null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("plot_already_claimed")));
            return;
        }

        int plotLimit = Integer.parseInt(Essential.getInstance().getConfigNamespace().get("max_player_plots"));

        if (plotPlayer.getPlotCount() == plotLimit) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("plot_limit_exceeded")));
            return;
        }

        StringBuilder bobTheBuilder = new StringBuilder();

        int plots = plotPlayer.getPlotCount() + 1;
        bobTheBuilder.append(plots);

        switch (plots) {
            case 1 -> bobTheBuilder.append("st");
            case 2 -> bobTheBuilder.append("nd");
            case 3 -> bobTheBuilder.append("rd");
            default -> bobTheBuilder.append("th");
        }

        inventory.setItem(9 + 3, makeItem(Material.GREEN_CONCRETE, i18n("claim_title"), i18n("claim_lore")
                .replace("%plot%", bobTheBuilder.toString())));

        inventory.setItem(9 + 5, makeItem(Material.RED_CONCRETE, i18n("cancel")));

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.claim." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.claim." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
