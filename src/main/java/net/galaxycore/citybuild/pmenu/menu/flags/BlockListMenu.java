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
    public static final int PIVOT = 5*9+1;
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
        itemStacks.clear();

        for (Material value : Material.values()) {
            if (!value.isBlock()) continue;
            if (value.isAir()) continue;
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
        int raw = inventoryClickEvent.getRawSlot();

        if(raw == 5*9) {
            blockListFlagMenu.open();
        } else if(raw == 5*9+3){
            page--;
            if(page < 0)
                page = 0;
            open();
        } else if(raw == 5*9+5) {
            page++;
            open();
        }

        if (raw >= 5*9) {
            return;
        }

        ItemStack item = inventoryClickEvent.getCurrentItem();
        if(item == null) return;

        System.out.println(item.getType());
    }

    @Override
    public void setMenuItems() {
        int len = itemStacks.size();
        int start = page * PIVOT;
        int end = Math.min(((page+1)*PIVOT)-1, len-(page*PIVOT));
        while(start > end) {
            page--;
            start = page * PIVOT;
            end = Math.min(((page+1)*PIVOT)-1, len-(page*PIVOT));
        }

        for (int i = start; i < end; i++) {
            inventory.addItem(itemStacks.get(i));
        }

        inventory.setItem(5*9, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        inventory.setItem(5*9+3, makeItem(Material.ARROW, PMenuI18N.PREV.get(player)));
        inventory.setItem(5*9+4, makeItem(Material.PAPER, PMenuI18N.PAGE.get(player) + (page + 1)));
        inventory.setItem(5*9+5, makeItem(Material.ARROW, PMenuI18N.NEXT.get(player)));
        setFillerGlass();
    }
}
