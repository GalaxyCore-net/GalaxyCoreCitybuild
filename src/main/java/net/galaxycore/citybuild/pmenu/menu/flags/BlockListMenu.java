package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockListMenu extends Menu {
    public static final int PIVOT = 5*9;
    private final Player player;
    private final Plot plot;
    private final Flag flag;
    private int page = 0;
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final BlockListFlagMenu blockListFlagMenu;


    public BlockListMenu(Player player, Plot plot, Flag flag, BlockListFlagMenu blockListFlagMenu) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.flag = flag;
        this.blockListFlagMenu = blockListFlagMenu;

        for (Material value : Material.values()) {
            if (!value.isBlock()) continue;
            itemStacks.add(new ItemStack(value, 1));
        }
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_EDIT.get(player);
    }

    @Override
    public int getSlots() {
        return 6*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {
        int len = itemStacks.size();
        page = (int) Math.min(page, Math.ceil(len/(float)PIVOT));
        int start = page * PIVOT;
        int end = Math.min((page*PIVOT)-1, len-(page*PIVOT));

        for (int i = start; i < end; i++) {
            inventory.addItem(itemStacks.get(i));
        }
    }
}
