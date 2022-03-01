package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.GlobalFlagContainer;
import com.plotsquared.core.plot.flag.PlotFlag;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.pmenu.menu.PMenuFlagsMenu;
import net.galaxycore.citybuild.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

@Getter
public class BooleanFlagMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private final Flag flag;
    private final Class<? extends PlotFlag<Boolean, ?>> flagClass;

    public BooleanFlagMenu(Player player, Plot plot, Flag flag) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.flag = flag;
        //noinspection unchecked
        flagClass = (Class<? extends PlotFlag<Boolean, ?>>) flag.getFlagClass();
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_EDIT.get(player);
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        switch (inventoryClickEvent.getRawSlot()) {
            case 12 -> // Activate
                    plot.setFlag(GlobalFlagContainer.getInstance().getFlag(flagClass).createFlagInstance(true));
            case 14 -> // Deactivate
                    plot.setFlag(GlobalFlagContainer.getInstance().getFlag(flagClass).createFlagInstance(false));

            case 18 -> // Reset
                    plot.removeFlag(flagClass);
        }

        if (List.of(12, 14, 18).contains(inventoryClickEvent.getRawSlot())) {
            player.closeInventory();
            new PMenuFlagsMenu(player, plot).open();
        }

    }

    @Override
    public void setMenuItems() {
        boolean value = plot.getFlag(flagClass);

        inventory.setItem(12, ItemStackUtils.glimmerIf(makeItem(Material.LIME_STAINED_GLASS, PMenuI18N.FLAGS_ACTIVATE.get(player)), value));
        inventory.setItem(14, ItemStackUtils.glimmerIf(makeItem(Material.RED_STAINED_GLASS, PMenuI18N.FLAGS_DEACTIVATE.get(player)), !value));
        inventory.setItem(18, makeItem(Material.BARRIER, PMenuI18N.RESET.get(player)));
    }
}
