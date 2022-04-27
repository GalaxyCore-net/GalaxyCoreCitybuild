package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.GlobalFlagContainer;
import com.plotsquared.core.plot.flag.types.BlockTypeWrapper;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockListMenu extends Menu {
    public static final int PIVOT = 5 * 9;
    private final Player player;
    private final Plot plot;
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final List<Boolean> highlight = new ArrayList<>();
    private final BlockListFlagMenu blockListFlagMenu;
    private int page;

    public BlockListMenu(Player player, Plot plot, BlockListFlagMenu blockListFlagMenu, int page) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.blockListFlagMenu = blockListFlagMenu;
        this.page = page;
        itemStacks.clear();
        highlight.clear();

        List<BlockTypeWrapper> blockTypeWrappers = new ArrayList<>(plot.getFlag(blockListFlagMenu.getFlagClass()));

        for (Material value : Material.values()) {
            if (!value.isBlock()) continue;
            if (value.isAir()) continue;
            //noinspection deprecation Filter
            if (value.isLegacy()) continue;
            if (!value.isItem()) continue;
            if (value.name().contains("WALL")) continue;
            itemStacks.add(new ItemStack(value, 1));
            BlockType type = BlockTypes.parse(value.name());
            highlight.add(blockTypeWrappers.stream().anyMatch(blockTypeWrapper -> Objects.equals(blockTypeWrapper.getBlockType(), type)));
        }
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_EDIT.get(player);
    }

    @Override
    public int getSlots() {
        return 6 * 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        int raw = inventoryClickEvent.getRawSlot();

        if (raw == 5 * 9) {
            blockListFlagMenu.open();
        } else if (raw == 5 * 9 + 3) {
            page--;
            if (page < 0)
                page = 0;
            open();
        } else if (raw == 5 * 9 + 5) {
            page++;
            open();
        }

        if (raw >= 5 * 9) {
            return;
        }

        ItemStack item = inventoryClickEvent.getCurrentItem();
        if (item == null) return;

        List<BlockTypeWrapper> blockTypeWrappers = new ArrayList<>(plot.getFlag(blockListFlagMenu.getFlagClass()));
        BlockType type = BlockTypes.parse(item.getType().name());

        if (blockTypeWrappers.stream().noneMatch(blockTypeWrapper -> Objects.equals(blockTypeWrapper.getBlockType(), type))) {
            blockTypeWrappers.add(BlockTypeWrapper.get(type));
        } else {
            blockTypeWrappers.remove(BlockTypeWrapper.get(type));
        }

        plot.setFlag(GlobalFlagContainer.getInstance().getFlag(blockListFlagMenu.getFlagClass()).createFlagInstance(blockTypeWrappers));
        plot.getFlag(blockListFlagMenu.getFlagClass());
        new BlockListMenu(player, plot, blockListFlagMenu, page).open();
    }

    @Override
    public void setMenuItems() {
        int start = page * PIVOT;
        int end = (page + 1) * PIVOT;
        while (start >= itemStacks.size()) {
            page--;
            start = page * PIVOT;
            end = (page + 1) * PIVOT;
        }
        for (int i = start; i < end; i++) {
            if (i < itemStacks.size()) {
                inventory.addItem(ItemStackUtils.glimmerIf(itemStacks.get(i), highlight.get(i)));
            }
        }

        inventory.setItem(5 * 9, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        inventory.setItem(5 * 9 + 1, FILLER_GLASS);
        inventory.setItem(5 * 9 + 2, FILLER_GLASS);
        inventory.setItem(5 * 9 + 3, makeItem(Material.ARROW, PMenuI18N.PREV.get(player)));
        inventory.setItem(5 * 9 + 4, makeItem(Material.PAPER, PMenuI18N.PAGE.get(player) + (page + 1)));
        inventory.setItem(5 * 9 + 5, makeItem(Material.ARROW, PMenuI18N.NEXT.get(player)));
        inventory.setItem(5 * 9 + 6, FILLER_GLASS);
        inventory.setItem(5 * 9 + 7, FILLER_GLASS);
        inventory.setItem(5 * 9 + 8, FILLER_GLASS);
    }
}
