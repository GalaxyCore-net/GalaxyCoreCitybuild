package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.implementations.PriceFlag;
import lombok.SneakyThrows;
import lombok.val;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.shop.ShopGUI;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.UUID;

public class PMenuBuyPlotMenu extends Menu {

    private final Player player;
    private final PlotPlayer<?> plotPlayer;
    private final Plot currentPlot;
    private boolean shouldOpen = true;
    private boolean error = false;

    public PMenuBuyPlotMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        PlotAPI plotAPI = new PlotAPI();
        plotPlayer = plotAPI.wrapPlayer(player.getUniqueId());
        if (plotPlayer != null) currentPlot = plotPlayer.getCurrentPlot();
        else currentPlot = null;

        if (currentPlot != null) {
            if (currentPlot.getFlagContainer().getFlag(PriceFlag.class) == PriceFlag.PRICE_NOT_BUYABLE) {
                shouldOpen = false;
            }
        }
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

        if (!error)
        switch (inventoryClickEvent.getRawSlot()) {
            case 9 + 3 -> claimPlot();
            case 9 + 5 -> player.closeInventory();
        }

    }

    private double getPrice() {
        if (currentPlot == null) return 0;
        return currentPlot.getFlagContainer().getFlag(PriceFlag.class).getValue();
    }

    private void claimPlot() {
        CoinDAO coinDAO = new CoinDAO(PlayerLoader.load(player), Essential.getInstance());
        if (!(coinDAO.get() >= getPrice())) {
            player.sendMessage(Component.text(i18n("not_enough_coins")));
            return;
        }
        PlayerLoader otherPlayer = buildLoader(Objects.requireNonNull(currentPlot.getOwner()));
        coinDAO.transact(otherPlayer, (long) getPrice(), "PLOTBUY:FROM+"+currentPlot.getOwner().toString() + ":TO+" + player.getUniqueId() + ":AS+" + currentPlot.getId() + ":PRICE+" + getPrice());

        plotPlayer.getCurrentPlot().setOwner(player.getUniqueId());
        player.sendMessage(Component.text(i18n("claimed_successfully")));
        player.closeInventory();
    }

    @Override
    public void open() {
        if (shouldOpen) super.open();
        else player.sendMessage(Component.text(i18n("plot_already_claimed")));
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();

        if (plotPlayer == null) {
            error = true;
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("fipoewjfweopijfewpoijfewpoifjweopfijwe"))); // Key won't be found, so it will say "§c§lERROR"
            return;
        }

        if (currentPlot == null) {
            error = true;
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("not_on_plot")));
            return;
        }

        int plotLimit = PMenuPlotBuyMenu.getMaxAllowedPlots(player);

        if (plotPlayer.getPlotCount() >= plotLimit) {
            error = true;
            inventory.setItem(9 + 4, makeItem(Material.BARRIER, i18n("plot_limit_exceeded")));
            return;
        }

        StringBuilder bobTheBuilder = new StringBuilder();

        int plots = plotPlayer.getPlotCount() + 1;
        bobTheBuilder.append(plots);

        switch (plots) {
            case 1 -> bobTheBuilder.append("st");
            case 2 -> bobTheBuilder.append("nd");
            case 3 -> bobTheBuilder.append("rd");
            default -> bobTheBuilder.append("th");
        }

        inventory.setItem(9 + 3, makeItem(Material.GREEN_CONCRETE, i18n("claim_title").replace("%d", 3 + ""), i18n("claim_lore").replace("%plot%", bobTheBuilder.toString())));

        inventory.setItem(9 + 5, makeItem(Material.RED_CONCRETE, i18n("cancel")));


    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.buy." + key);
        if (i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.buy." + key);
        }
        if (i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

    @SneakyThrows
    private PlayerLoader buildLoader(UUID uuid) {
        val core = CoreProvider.getCore();
        val connection = core.getDatabaseConfiguration().getConnection();
        val load = connection.prepareStatement("SELECT * FROM core_playercache WHERE uuid = ?");
        load.setString(1, uuid.toString());
        val loadResult = load.executeQuery();
        if (!loadResult.next()) {
            loadResult.close();
            load.close();
            return null;
        }
        val playerLoader = new PlayerLoader(
                loadResult.getInt("id"),
                UUID.fromString(loadResult.getString("uuid")),
                loadResult.getString("lastname"),
                ShopGUI.Companion.parse(loadResult, "firstlogin"),
                ShopGUI.Companion.parse(loadResult, "lastlogin"),
                ShopGUI.Companion.parse(loadResult, "last_daily_reward"),
                loadResult.getInt("banpoints"),
                loadResult.getInt("mutepoints"),
                loadResult.getInt("warnpoints"),
                loadResult.getInt("reports"),
                loadResult.getBoolean("teamlogin"),
                loadResult.getBoolean("debug"),
                loadResult.getBoolean("socialspy"),
                loadResult.getBoolean("commandspy"),
                loadResult.getBoolean("vanished"),
                loadResult.getBoolean("nicked"),
                loadResult.getInt("lastnick"),
                loadResult.getLong("coins")
        );
        loadResult.close();
        load.close();
        return playerLoader;
    }

}
