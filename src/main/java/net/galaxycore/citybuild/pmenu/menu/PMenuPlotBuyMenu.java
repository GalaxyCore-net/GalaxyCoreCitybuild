package net.galaxycore.citybuild.pmenu.menu;

import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.spice.KMenu;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PMenuPlotBuyMenu extends KMenu {
    private final LuckPerms luckPerms;

    public PMenuPlotBuyMenu(Player player) {
        this.luckPerms = LuckPermsProvider.get();
        item(11, Material.PAPER, PMenuI18N.AKUPLOTS.get(player) + getMaxAllowedPlots(player));
        item(15, Material.EMERALD, PMenuI18N.NEWPLOT.get(player)).then(kMenuItem -> {
            System.out.println("Deine Mutter stinkt nach Butter");
            return null;
        });
    }

    public void addPermission(User user, String permission) {
        user.data().add(Node.builder(permission).build());

        luckPerms.getUserManager().saveUser(user);
    }

    private int getMaxAllowedPlots(Player paramPlayer) {
        for (int c = 25000; c >= 0; c--) {
            if (paramPlayer.hasPermission("plots.plot." + c))
                return c;
        }
        return 0;
    }


    @NotNull
    @Override
    public String getNameI18NKey() {
        return PMenuI18N.PLOTBYMENU.getKey();
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
