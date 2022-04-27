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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PMenuPlotBuyMenu extends KMenu {
    private final LuckPerms luckPerms;
    private final Player player;
    private final Reactive<Integer> allowed;
    private final KMenuItem allowedItem;
    private final KMenuItem priceItem;

    private final Function<Integer, Long> priceCalculator = (plotNr) -> new BigInteger(String.valueOf(plotNr-2)).pow(2).longValue() *380L+10000L;


    public PMenuPlotBuyMenu(Player player) {
        this.player = player;
        allowed = new Reactive<>(getMaxAllowedPlots(player));
        this.luckPerms = LuckPermsProvider.get();

        PlayerLoader loadedPlayer = PlayerLoader.load(player);
        CoinDAO dao = new CoinDAO(loadedPlayer, Essential.getInstance());
        allowedItem = item(11, Material.PAPER, PMenuI18N.CURRENTPLOTS.get(player) + getMaxAllowedPlots(player));
        priceItem = item(15, Material.EMERALD, PMenuI18N.NEWPLOT.get(player), PMenuI18N.PRICE.get(player) + priceCalculator.apply(getMaxAllowedPlots(player)+1)).then(kMenuItem -> {
            long priceForPlot = priceCalculator.apply(allowed.getItem()+1);

            if (dao.get() < priceForPlot) {
                PMenuI18N.COINSPOOR.send(player);
                return null;
            }

            dao.transact(null, priceForPlot, "PLOT:BUY:NR%d:FR%d".formatted(allowed.getItem(), priceForPlot));
            allowed.setItem(allowed.getItem()+1);
            player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1, 1);
            return null;
        });

        allowed.updatelistener(integer -> {
            addPermission(Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId())), "plots.plot." + integer);
            addPermission(Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId())), "plots.merge." + integer);

            allowedItem.getItemStack().update(itemStack -> {
                ItemMeta meta = itemStack.getItemMeta();
                meta.displayName(Component.text(PMenuI18N.CURRENTPLOTS.get(player) + getMaxAllowedPlots(player)));
                itemStack.setItemMeta(meta);
                return itemStack;
            });

            priceItem.getItemStack().update(itemStack -> {
                ItemMeta meta = itemStack.getItemMeta();
                meta.lore(List.of(Component.text(PMenuI18N.PRICE.get(player) + priceCalculator.apply(integer+1))));
                itemStack.setItemMeta(meta);
                return itemStack;
            });

            return null;
        });
    }

    public void open() {
        open(player);

        allowedItem.getItemStack().update(itemStack -> {
            ItemMeta meta = itemStack.getItemMeta();
            meta.displayName(Component.text(PMenuI18N.CURRENTPLOTS.get(player) + getMaxAllowedPlots(player)));
            itemStack.setItemMeta(meta);
            return itemStack;
        });
    }

    public void addPermission(User user, String permission) {
        user.data().add(Node.builder(permission).build());

        luckPerms.getUserManager().saveUser(user);
    }

    private int getMaxAllowedPlots(Player paramPlayer) {
        for (int c = 50000; c >= 0; c--) {
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
