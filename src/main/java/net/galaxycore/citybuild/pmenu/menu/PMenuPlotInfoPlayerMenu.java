package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.function.Function;

@Getter
public class PMenuPlotInfoPlayerMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private boolean open = true;

    public PMenuPlotInfoPlayerMenu(Player player, Plot plot) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;

        if (plot != null && !Objects.requireNonNull(plot.getOwner()).toString().equals(player.getUniqueId().toString()) && open) {
            open = false;
            PMenuI18N.NO_PLOT_PERMISSIONS.send(player);
        }
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_TITLE.get(player);
    }

    @Override
    public int getSlots() {
        return 3 * 9;
    }

    private void openPlayerRequestThen(Function<PlotPlayer<?>, Menu> menuCallback) {
        new PMenuSearchPlayerMenu(player, (offlinePlayer -> {
            PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(offlinePlayer.getUniqueId());
            menuCallback.apply(plotPlayer).open();
        }));

    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();
        switch (inventoryClickEvent.getRawSlot()) {
            case 18 -> new PMenuPlotInfoMenu(player, plot).open();
            case 9 -> openPlayerRequestThen((plotPlayer) -> new PMenuAddMenu(player, plotPlayer));
            case 11 -> openPlayerRequestThen((plotPlayer) -> new PMenuDenyMenu(player, plotPlayer));
            case 13 -> openPlayerRequestThen((plotPlayer) -> new PMenuKickMenu(player, plotPlayer.getUUID()));
            case 15 -> openPlayerRequestThen((plotPlayer) -> new PMenuTrustMenu(player, plotPlayer));
            case 17 -> openPlayerRequestThen((plotPlayer) -> new PMenuRemoveMenu(player, plotPlayer));
        }
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(9, makeItem(Material.GREEN_STAINED_GLASS, I18N.getByPlayer(player, "citybuild.pmenu.add.title")));
        inventory.setItem(11, makeItem(Material.ANVIL, I18N.getByPlayer(player, "citybuild.pmenu.deny.title")));
        inventory.setItem(13, makeItem(Material.LEATHER_BOOTS, I18N.getByPlayer(player, "citybuild.pmenu.kick.title")));
        inventory.setItem(15, makeItem(Material.EMERALD, I18N.getByPlayer(player, "citybuild.pmenu.trust.title")));
        inventory.setItem(17, makeItem(Material.REDSTONE, I18N.getByPlayer(player, "citybuild.pmenu.remove.title")));
        inventory.setItem(18, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        setFillerGlass();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
