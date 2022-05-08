package net.galaxycore.citybuild.pmenu.menu;

import lombok.SneakyThrows;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.pmenu.utils.Warp;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;

public class PMenuWarpMenu extends Menu {
    private final Player player;
    private final Warp[] mapping = new Warp[9];

    public PMenuWarpMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.TITLE_WARP.get(player);
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if (!((inventoryClickEvent.getRawSlot() >= 9) && (inventoryClickEvent.getRawSlot() < 18))) {
            return;
        }
        Warp warp = mapping[inventoryClickEvent.getRawSlot() - 9];
        if (warp == null) return;
        player.closeInventory();

        player.teleport(warp.getTarget());
        player.sendMessage(PMenuI18N.WARP_SUCCESS.get(player));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 1);
    }

    @SneakyThrows
    @Override
    public void setMenuItems() {
        Connection conn = Essential.getCore().getDatabaseConfiguration().getConnection();
        ResultSet resultSet = conn.prepareStatement("SELECT * FROM galaxycity_warps").executeQuery();

        while (resultSet.next()) Warp.map(resultSet, mapping);

        resultSet.close();

        setFillerGlass();

        for (Warp warp : mapping) {
            if (warp != null)
                inventory.setItem(warp.getPos() + 9, makeItem(warp.getDisplay(), "§7" + warp.getName()));
        }
    }
}
