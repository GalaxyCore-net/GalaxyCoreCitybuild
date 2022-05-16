package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.GlobalFlagContainer;
import com.plotsquared.core.plot.flag.PlotFlag;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.pmenu.menu.PMenuFlagsMenu;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

@Getter
public class PriceFlagMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private final Flag flag;
    private final Class<? extends PlotFlag<Double, ?>> flagClass;

    public PriceFlagMenu(Player player, Plot plot, Flag flag) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.flag = flag;
        //noinspection unchecked
        flagClass = (Class<? extends PlotFlag<Double, ?>>) flag.getFlagClass();
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
        Double value = plot.getFlag(flagClass);

        switch (inventoryClickEvent.getRawSlot()) {
            case 15 -> // Deactivate
                    new AnvilGUI.Builder()
                            .onClose(player1 -> open())
                            .onComplete((player1, s) -> {
                                if (s.equals(PMenuI18N.PLOTINFO_FLAGS_NONE.get(player))) {
                                    plot.removeFlag(flagClass);
                                } else {
                                    try {
                                        plot.setFlag(GlobalFlagContainer.getInstance().getFlag(flagClass).createFlagInstance(Double.parseDouble(Integer.parseInt(s) + "")));
                                        plot.getFlag(flagClass);
                                    } catch (NumberFormatException ignored) {
                                        return AnvilGUI.Response.text(PMenuI18N.FLAGS_NFE.get(player));
                                    }
                                }
                                PMenuFlagsMenu pMenuFlagsMenu = new PMenuFlagsMenu(player, plot);
                                pMenuFlagsMenu.setInventory(Bukkit.createInventory(pMenuFlagsMenu, pMenuFlagsMenu.getSlots(), Component.text(pMenuFlagsMenu.getMenuName())));
                                pMenuFlagsMenu.setMenuItems();
                                return AnvilGUI.Response.openInventory(pMenuFlagsMenu.getInventory());
                            })
                            .text(String.valueOf(value%24000))
                            .title(PMenuI18N.FLAGS_EDIT.get(player))
                            .plugin(Essential.getInstance())
                            .open(player);

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
        @NonNull Double value = plot.getFlag(flagClass);

        inventory.setItem(11, makeItem(Material.BARRIER, PMenuI18N.RESET.get(player)));
        inventory.setItem(13, makeItem(Material.SPRUCE_SIGN, PMenuI18N.FLAGS_CURRENTVAL.get(player) + (value == 0D ? PMenuI18N.FLAGS_DEACTIVATE.get(player) : value)));
        inventory.setItem(15, makeItem(Material.FEATHER, PMenuI18N.FLAGS_CHANGE.get(player)));
    }
}
