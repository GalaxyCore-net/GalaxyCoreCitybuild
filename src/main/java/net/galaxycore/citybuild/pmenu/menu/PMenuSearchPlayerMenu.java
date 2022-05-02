package net.galaxycore.citybuild.pmenu.menu;

import com.sk89q.worldedit.util.formatting.text.format.TextColor;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.utils.Skull;
import net.galaxycore.galaxycorecore.spice.KMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.bukkit.Material.*;

public class PMenuSearchPlayerMenu extends KMenu {
    private Player player;
    private final Consumer<OfflinePlayer> callback;
    private final KMenuItem pageItem;
    private int page = 1;
    private String nameFilter = "";
    private List<OfflinePlayer> players;
    private List<OfflinePlayer> filtered;

    public PMenuSearchPlayerMenu(Player player, Consumer<OfflinePlayer> callback) {
        this.player = player;
        this.callback = callback;
        players = List.of(Bukkit.getOfflinePlayers());

        item(5*9+1, Skull.ARROW_LEFT.getSkull(PMenuI18N.NEXT.get(player))).then(kMenuItem -> {
            filtered = players.stream().filter(fp -> Objects.requireNonNull(fp.getName()).toLowerCase().contains(nameFilter.toLowerCase())).skip((long) (page-1) *5*9).limit(5*9).collect(java.util.stream.Collectors.toList());
            page = Math.max(page - 1, 1);
            update();
            return null;
        });

        pageItem = item(5*9+3, PAPER, PMenuI18N.PAGE.get(player) + page);
        item(5*9+5, SPRUCE_SIGN, PMenuI18N.SEARCH.get(player)).then(kMenuItem -> {
            new AnvilGUI.Builder().onComplete((player1, text) -> {
                page = 1;
                nameFilter = text;
                filtered = players.stream().filter(fp -> Objects.requireNonNull(fp.getName()).toLowerCase().contains(nameFilter.toLowerCase())).limit(5*9).collect(java.util.stream.Collectors.toList());
                update();
                return AnvilGUI.Response.openInventory(getInventory());
            }).plugin(Essential.getInstance()).title(PMenuI18N.SEARCH.get(player)).text(nameFilter).open(player);

            return null;
        });

        item(6*9-2, Skull.ARROW_RIGHT.getSkull(PMenuI18N.PREV.get(player))).then(kMenuItem -> {
            filtered = players.stream().filter(fp -> Objects.requireNonNull(fp.getName()).toLowerCase().contains(nameFilter.toLowerCase())).skip((long) (page-1) *5*9).limit(5*9).collect(java.util.stream.Collectors.toList());
            page = Math.min(page + 1, (int)Math.ceil(players.size() / (double)(5*9)));
            update();
            return null;
        });
    }

    public void update() {
        pageItem.getItemStack().update(itemStack -> {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(PMenuI18N.PAGE.get(player) + page));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        });

        ItemStack spacer = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        for (int i = 0; i < 5*9; i++) {
            item(i, spacer).place(getInventory());
        }

        for (OfflinePlayer offlinePlayer : filtered) {
            ItemStack skull = new ItemStack(PLAYER_HEAD);
            ItemMeta itemMeta = skull.getItemMeta();
            itemMeta.displayName(Component.text("§e" + Objects.requireNonNull(offlinePlayer.getName())));
            SkullMeta meta = (SkullMeta) itemMeta;
            meta.setOwningPlayer(offlinePlayer);
            skull.setItemMeta(meta);

            item(filtered.indexOf(offlinePlayer), skull).then(kMenuItem -> {
                player.closeInventory();
                callback.accept(offlinePlayer);
                return null;
            }).place(getInventory());
        }
    }

    public void open() {
        filtered = players.stream().filter(fp -> Objects.requireNonNull(fp.getName()).toLowerCase().contains(nameFilter.toLowerCase())).skip((long) (page-1) *5*9).limit(5*9).collect(java.util.stream.Collectors.toList());
        super.open(player);
        update();
    }

    @NotNull
    @Override
    public String getNameI18NKey() {
        return PMenuI18N.SEARCH_FOR_PLAYER.getKey();
    }

    @Override
    public int getSize() {
        return 6*9;
    }
}
