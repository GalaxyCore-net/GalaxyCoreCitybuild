package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.database.DBFunc;
import com.plotsquared.core.player.PlotPlayer;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class PMenuRemoveMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> toRemove;
    private final PlotPlayer<?> plotPlayer;

    public PMenuRemoveMenu(Player player, PlotPlayer<?> toRemove) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.toRemove = toRemove;
        this.plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
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

        switch (inventoryClickEvent.getRawSlot()) {
            case 9 + 3 -> remove();
            case 9 + 5 -> player.closeInventory();
        }

    }

    private void remove() {
        UUID remove = toRemove != null ? toRemove.getUUID() : DBFunc.EVERYONE;
        plotPlayer.getCurrentPlot().removeTrusted(remove);
        plotPlayer.getCurrentPlot().removeMember(remove);
        plotPlayer.getCurrentPlot().removeDenied(remove);
        player.sendMessage(Component.text(i18n("removed_successfully")));
        player.closeInventory();
    }

    @Override
    public void setMenuItems() {

        if (plotPlayer.getCurrentPlot() == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_on_plot")));
            return;
        }

        if (plotPlayer.getCurrentPlot().getOwner() != player.getUniqueId() && !player.hasPermission("plots.admin.command.remove")) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_your_plot")));
            return;
        }

        inventory.setItem(9 + 3, makeItem(Material.GREEN_CONCRETE, i18n("remove_title")
                .replace("%name%", toRemove != null ? toRemove.getName() : "everyone")));

        inventory.setItem(9 + 5, makeItem(Material.RED_CONCRETE, i18n("cancel")));
        setFillerGlass();

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.remove." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.remove." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
