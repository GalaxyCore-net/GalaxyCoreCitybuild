package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.query.PlotQuery;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.utils.MathUtils;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuAliasMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> plotPlayer;
    private final String action;
    private final String alias;

    public PMenuAliasMenu(Player player, String action, String alias) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
        this.action = action;
        this.alias = alias;
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
    public void open() {
        this.inventory = Bukkit.createInventory(this, this.getSlots(), Component.text(this.getMenuName()));
        this.setMenuItems();
        this.playerMenuUtility.getOwner().openInventory(this.inventory);
        this.onOpen();
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

        if (inventoryClickEvent.getCurrentItem() == null)
            return;

        if (inventoryClickEvent.getCurrentItem().getType() == Material.FEATHER && inventoryClickEvent.getRawSlot() == 9 + 3) {
            setAliasOrShowGUI(null);
        } else if (inventoryClickEvent.getCurrentItem().getType() == Material.BARRIER && inventoryClickEvent.getRawSlot() == 9 + 5) {
            removeAlias();
        }

    }

    private void removeAlias() {
        String alias = plotPlayer.getCurrentPlot().getAlias();
        if (!alias.isEmpty()) {
            player.sendMessage(Component.text(i18n("alias_removed")));
        } else {
            player.sendMessage(Component.text(i18n("no_alias_set")));
        }
        plotPlayer.getCurrentPlot().setAlias(null);
        player.closeInventory();
    }

    private void setAlias(String alias) {
        if (plotPlayer.getCurrentPlot() == null)
            return;

        if (plotPlayer.getCurrentPlot().getArea() == null)
            return;

        if (alias.isEmpty()) {
            player.sendMessage(Component.text(i18n("empty_alias")));
        } else if (alias.length() >= 50) {
            player.sendMessage(Component.text(i18n("alias_too_long")));
        } else if (MathUtils.isInteger(alias)) {
            player.sendMessage(Component.text(i18n("alias_integer")));
        } else {

            if (PlotQuery.newQuery().inArea(plotPlayer.getCurrentPlot().getArea()).withAlias(alias).anyMatch()) {
                player.sendMessage(Component.text(i18n("alias_taken")));
                return;
            }

            plotPlayer.getCurrentPlot().setAlias(alias);
            player.sendMessage(Component.text(i18n("alias_set_successfully")));

        }
    }

    private void setAliasOrShowGUI(String maybeAlias) {
        player.closeInventory();
        if (maybeAlias != null) {
            setAlias(maybeAlias);
            return;
        }
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder
                .onComplete((ignore, alias) -> {
                    setAlias(alias);
                    return AnvilGUI.Response.close();
                })
                .text(i18n("what_new_alias"))
                .itemLeft(makeItem(Material.WRITABLE_BOOK, ""))
                .title(i18n("title"))
                .plugin(Essential.getInstance())
                .open(player);
    }

    @Override
    public void setMenuItems() {

        Plot plot = plotPlayer.getCurrentPlot();

        if (plot == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, PMenuI18N.NOT_ON_PLOT.get(player)));
            return;
        }

        if (plot.getOwner() != player.getUniqueId() && !player.hasPermission("plots.admin.command.alias")) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, PMenuI18N.NOT_YOUR_PLOT.get(player)));
            return;
        }

        inventory.setItem(9 + 3, makeItem(Material.FEATHER, i18n("set")));
        inventory.setItem(9 + 5, makeItem(Material.BARRIER, i18n("remove")));

        setFillerGlass();

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.alias." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.alias." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

    public void onOpen() {
        if (action != null)
            switch (action) {
                case "set" -> setAliasOrShowGUI(alias);
                case "remove" -> removeAlias();
            }
    }

}
