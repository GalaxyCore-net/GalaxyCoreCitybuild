package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.coins.PlayerTransactionError;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class PMenuLizenzMenu extends Menu {
    private final Player player;
    private final LuckPerms luckPerms;

    public PMenuLizenzMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.luckPerms = LuckPermsProvider.get();
    }

    public void addPermission(UUID userUuid, String permission) {
        luckPerms.getUserManager().modifyUser(userUuid, user -> user.data().add(Node.builder(permission).build()));
    }

    public void addPermission(User user, String permission) {
        user.data().add(Node.builder(permission).build());

        luckPerms.getUserManager().saveUser(user);
    }

    private int getMaxAllowedShops(Player paramPlayer) {
        for (int c = 25000; c >= 0; c--) {
            if (paramPlayer.hasPermission("citybuild.shop." + c))
                return c;
        }
        return 0;
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
        CoinDAO coinDAO = new CoinDAO(PlayerLoader.load(player), Essential.getCore());
        switch (inventoryClickEvent.getRawSlot()) {
            case 11 -> {
                int maxShops = getMaxAllowedShops(player);
                if (maxShops >= 50) {
                    PMenuI18N.LICENSEALREADYPURCHASED.send(player);
                    return;
                }
                try {
                    coinDAO.transact(null, 60000, "ESSENTIAL:LIC:" + System.currentTimeMillis() + ":1");
                    addPermission(player.getUniqueId(), "citybuild.shop.50");
                    addPermission(player.getUniqueId(), "citybuild.licence.1");
                } catch (PlayerTransactionError ignored) {
                    player.sendMessage(PMenuI18N.COINSPOOR.get(player));
                }
            }
            case 13 -> {
                int maxShops = getMaxAllowedShops(player);
                if (maxShops < 50) {
                    PMenuI18N.PREVLICENSENOTBOUGHT.send(player);
                    return;
                }
                if (maxShops >= 150) {
                    PMenuI18N.LICENSEALREADYPURCHASED.send(player);
                    return;
                }
                try {
                    coinDAO.transact(null, 180000, "ESSENTIAL:LIC:" + System.currentTimeMillis() + ":2");
                    addPermission(player.getUniqueId(), "citybuild.shop.150");
                    addPermission(player.getUniqueId(), "citybuild.licence.2");
                } catch (PlayerTransactionError ignored) {
                    player.sendMessage(PMenuI18N.COINSPOOR.get(player));
                }
            }
            case 15 -> {
                int maxShops = getMaxAllowedShops(player);
                if (maxShops < 150) {
                    PMenuI18N.PREVLICENSENOTBOUGHT.send(player);
                    return;
                }
                if (maxShops >= 500) {
                    PMenuI18N.LICENSEALREADYPURCHASED.send(player);
                    return;
                }
                try {
                    coinDAO.transact(null, 300000, "ESSENTIAL:LIC:" + System.currentTimeMillis() + ":3");
                    addPermission(player.getUniqueId(), "citybuild.shop.500");
                    addPermission(player.getUniqueId(), "citybuild.licence.3");
                } catch (PlayerTransactionError ignored) {
                    player.sendMessage(PMenuI18N.COINSPOOR.get(player));
                }
            }


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
