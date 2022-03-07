package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.database.DBFunc;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PMenuKickMenu extends Menu {

    private final Player player;
    private final UUID toKick;
    private final PlotPlayer<?> plotPlayer;

    public PMenuKickMenu(Player player, UUID toKick) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.toKick = toKick;
        this.plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
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

        switch (inventoryClickEvent.getRawSlot()) {
            case 9 + 3 -> plotKick();
            case 9 + 5 -> player.closeInventory();
        }

    }

    private void plotKick() {

        player.closeInventory();
        player.sendMessage(Component.text(i18n("kicking_players")));
        Set<PlotPlayer<?>> players = new HashSet<>();
        if (toKick == DBFunc.EVERYONE || toKick == null) {
            for (PlotPlayer<?> plotPlayerCur : plotPlayer.getCurrentPlot().getPlayersInPlot()) {
                if (plotPlayerCur.getUUID() == player.getUniqueId() || plotPlayerCur.hasPermission("plots.admin.command.kick"))
                    continue;
                players.add(plotPlayerCur);
            }
        }
        players.remove(plotPlayer); // Don't ever kick the calling player
        for (PlotPlayer<?> kickers : players) {
            if (!plotPlayer.getCurrentPlot().equals(kickers.getCurrentPlot()))
                continue;
            if (kickers.hasPermission("plots.admin.command.kick")) {
                continue;
            }
            org.bukkit.Location temp = player.getWorld().getSpawnLocation();
            Location spawn = Location.at(player.getWorld().getName(),
                    temp.getBlockX(),
                    temp.getBlockY(),
                    temp.getBlockZ(),
                    temp.getYaw(),
                    temp.getPitch());
            if (plotPlayer.getCurrentPlot().equals(spawn.getPlot())) {
                kickers.kick(i18n("kicked_no_spawn"));
                return;
            }
            kickers.plotkick(spawn);
            Objects.requireNonNullElse(Bukkit.getPlayer(kickers.getUUID()), player).sendMessage(Component.text(i18n("kicked_successfully")));

        }

    }

    @Override
    public void setMenuItems() {

        if (plotPlayer.getCurrentPlot() == null) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_on_plot")));
            return;
        }

        if (plotPlayer.getCurrentPlot().getOwner() != player.getUniqueId() && !player.hasPermission("plots.admin.command.kick")) {
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_your_plot")));
            return;
        }

        inventory.setItem(9 + 3, makeItem(Material.GREEN_CONCRETE, i18n("kick_title")
                .replace("%name%", Bukkit.getPlayer(toKick) != null ? Objects.requireNonNull(Bukkit.getPlayer(toKick)).getName() : "everyone")));

        inventory.setItem(9 + 5, makeItem(Material.RED_CONCRETE, i18n("cancel")));

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.kick." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.kick." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

}
