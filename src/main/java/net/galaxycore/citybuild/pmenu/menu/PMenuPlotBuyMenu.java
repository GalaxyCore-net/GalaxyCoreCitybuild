package net.galaxycore.citybuild.pmenu.menu;

import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.spice.KMenu;
import net.galaxycore.galaxycorecore.spice.reactive.Reactive;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class PMenuPlotBuyMenu extends KMenu {
    private final LuckPerms luckPerms;
    private final Player player;
    private final Reactive<Integer> allowed = new Reactive<>(0);

    private final Function<Integer, Long> priceCalculator = (plotNr) -> Long.parseLong(Math.pow(plotNr-2, 2)+"")*380L+10000L;


    public PMenuPlotBuyMenu(Player player) {
        this.player = player;
        this.luckPerms = LuckPermsProvider.get();

        PlayerLoader loadedPlayer = PlayerLoader.load(player);
        CoinDAO dao = new CoinDAO(loadedPlayer, Essential.getInstance());
        KMenuItem allowedItem = item(11, Material.PAPER, " ");
        item(15, Material.EMERALD, PMenuI18N.NEWPLOT.get(player)).then(kMenuItem -> {
            long priceForPlot = priceCalculator.apply(allowed.getItem());

            if (dao.get() < priceForPlot) {
                PMenuI18N.COINSPOOR.send(player);
                return null;
            }

            allowed.setItem(allowed.getItem()+1);
            dao.transact(null, priceForPlot, "PLOT:BUY:NR%d:FR%d".formatted(allowed.getItem(), priceForPlot));
            addPermission(Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId())), "plots.plot." + allowed.getValue());
            addPermission(Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId())), "plots.merge." + allowed.getValue());
            return null;
        });

        allowed.updatelistener(integer -> {
            allowedItem.getItemStack().update(itemStack -> {
                itemStack.editMeta(itemMeta -> itemMeta.displayName(Component.text(PMenuI18N.AKUPLOTS.get(player) + getMaxAllowedPlots(player))));
                return null;
            });
            return null;
        });
    }

    public void open() {
        open(player);

        allowed.setItem(getMaxAllowedPlots(player));
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
