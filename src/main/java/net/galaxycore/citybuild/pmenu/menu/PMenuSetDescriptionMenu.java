package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.implementations.DescriptionFlag;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuSetDescriptionMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> plotPlayer;
    private String description;

    public PMenuSetDescriptionMenu(Player player, String description) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
        this.description = description;
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
    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.setdescription." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.setdescription." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

    @Override
    public void open() {

        Plot plot = plotPlayer.getCurrentPlot();

        if (plot == null) {
            PMenuI18N.NOT_ON_PLOT.send(player);
            return;
        }

        if (plot.getOwner() != player.getUniqueId() && !player.hasPermission("plots.admin.command.setdescription")) {
            PMenuI18N.NOT_YOUR_PLOT.send(player);
            return;
        }

        player.closeInventory();
        if (description != null) {
            setDescription();
            return;
        }
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder
                .onComplete((ignore, description) -> {
                    this.description = description;
                    setDescription();
                    return AnvilGUI.Response.close();
                })
                .text(i18n("what_new_description"))
                .itemLeft(makeItem(Material.WRITABLE_BOOK, ""))
                .title(getMenuName())
                .plugin(Essential.getInstance())
                .open(player);
    }

    private void setDescription() {

        player.closeInventory();
        if (description.isEmpty()) {
            plotPlayer.getCurrentPlot().removeFlag(DescriptionFlag.class);
            player.sendMessage(Component.text(i18n("description_unset")));
            return;
        }

        if (!plotPlayer.getCurrentPlot().setFlag(DescriptionFlag.class, description)) {
            player.sendMessage(Component.text(i18n("description_not_set")));
        } else {
            player.sendMessage(Component.text(i18n("description_set")));
        }

    }

}
