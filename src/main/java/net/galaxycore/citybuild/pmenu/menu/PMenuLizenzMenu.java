package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuLizenzMenu extends Menu {
    private final Player player;

    public PMenuLizenzMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.LIZENZMENU.get(player);
    }

    @Override
    public int getSlots() {
        return 9 * 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        new CoinDAO(PlayerLoader.load(player), Essential.getCore());
        switch (inventoryClickEvent.getRawSlot()) {
            case 11 -> new CoinDAO(PlayerLoader.load(player), Essential.getCore());


        }
    }


    @Override
    public void setMenuItems() {
        setFillerGlass();

        inventory.setItem(11, makeItem(Material.PAPER, PMenuI18N.SHOP1.get(player), "----------", "§4", PMenuI18N.SHOPA1.get(player), "§4", PMenuI18N.SHOPA2.get(player), "§4", PMenuI18N.SHOPA3.get(player)));
        inventory.setItem(13, makeItem(Material.PAPER, PMenuI18N.SHOP2.get(player), "----------", "§4", PMenuI18N.SHOPB1.get(player), "§4", PMenuI18N.SHOPB2.get(player), "§4", PMenuI18N.SHOPB3.get(player)));
        inventory.setItem(15, makeItem(Material.PAPER, PMenuI18N.SHOP3.get(player), "----------", "§4", PMenuI18N.SHOPC1.get(player), "§4", PMenuI18N.SHOPC2.get(player), "§4", PMenuI18N.SHOPC3.get(player)));

    }
}
