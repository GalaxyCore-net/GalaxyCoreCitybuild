package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.MetaDataAccess;
import com.plotsquared.core.player.PlayerMetaDataKeys;
import com.plotsquared.core.player.PlotPlayer;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.utils.Skull;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class PMenuGrantMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> toGrant;

    public PMenuGrantMenu(Player player, UUID toGrant) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.toGrant = new PlotAPI().wrapPlayer(toGrant);
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

        MetaDataAccess<Integer> access = toGrant.accessPersistentMetaData(PlayerMetaDataKeys.PERSISTENT_GRANTED_PLOTS);
        switch (inventoryClickEvent.getRawSlot()) {
            case 9 + 3 -> set(access);
            case 9 + 5 -> check(access);
        }

    }

    private void set(MetaDataAccess<Integer> access) {

        access.set(access.get().orElse(0) + 1);
        access.close();
        player.sendMessage(Component.text(i18n("set_successfully")));
        player.closeInventory();

    }

    private void check(MetaDataAccess<Integer> access) {

        player.sendMessage(Component.text(access.get().orElse(0)));
        access.close();
        player.closeInventory();

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(9 + 3, editItem(Skull.ARROW_LEFT.getSkull(), i18n("set_title")));

        inventory.setItem(9 + 5, editItem(Skull.ARROW_RIGHT.getSkull(), i18n("check_title")));

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.grant." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.grant." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

    public static ItemStack editItem(ItemStack item, String displayName, String... lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(displayName));

        //noinspection deprecation
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

}
