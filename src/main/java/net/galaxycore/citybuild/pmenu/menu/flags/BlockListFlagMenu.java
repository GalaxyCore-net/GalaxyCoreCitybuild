package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.types.BlockTypeWrapper;
import com.plotsquared.core.plot.flag.types.ListFlag;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.pmenu.menu.PMenuFlagsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

@Getter
public class BlockListFlagMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private final Flag flag;
    private final Class<? extends ListFlag<BlockTypeWrapper, ?>> flagClass;

    public BlockListFlagMenu(Player player, Plot plot, Flag flag) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.flag = flag;
        //noinspection unchecked
        flagClass = (Class<? extends ListFlag<BlockTypeWrapper, ?>>) flag.getFlagClass();
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
        List<BlockTypeWrapper> value = plot.getFlag(flagClass);

        switch (inventoryClickEvent.getRawSlot()) {
            case 15 -> // edit
                    new BlockListMenu(player, plot, this, 0).open();

            case 11 -> // Reset
                    plot.removeFlag(flagClass);
        }

        if (Objects.equals(11, inventoryClickEvent.getRawSlot())) {
            player.closeInventory();
            new PMenuFlagsMenu(player, plot).open();
        }

    }

    @Override
    public void setMenuItems() {
        List<BlockTypeWrapper> value = plot.getFlag(flagClass);
        String[] render = new String[value.size()];
        for (int i = 0; i < value.size(); i++) {
            render[i] = "§7- " + Objects.requireNonNull(value.get(i).getBlockType()).getResource();
        }

        inventory.setItem(11, makeItem(Material.BARRIER, PMenuI18N.RESET.get(player)));
        inventory.setItem(13, makeItem(Material.SPRUCE_SIGN, PMenuI18N.FLAGS_CURRENTVAL.get(player), render));
        inventory.setItem(15, makeItem(Material.FEATHER, PMenuI18N.FLAGS_CHANGE.get(player), PMenuI18N.FLAGS_TIP_ADD.get(player)));
    }
}
