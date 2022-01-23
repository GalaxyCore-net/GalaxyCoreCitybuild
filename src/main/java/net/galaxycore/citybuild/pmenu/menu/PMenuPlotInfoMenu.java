package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.flag.PlotFlag;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PMenuPlotInfoMenu extends Menu {
    private static HashMap<Biome, Material> biomeMaterials = new HashMap<>();

    static {
        biomeMaterials.put(Biome.OCEAN, Material.TRIDENT);
        biomeMaterials.put(Biome.PLAINS, Material.GRASS_BLOCK);
        biomeMaterials.put(Biome.DESERT, Material.CACTUS);
        biomeMaterials.put(Biome.MOUNTAINS, Material.STONE);
        biomeMaterials.put(Biome.FOREST, Material.OAK_LOG);
        biomeMaterials.put(Biome.TAIGA, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.SWAMP, Material.LILY_PAD);
        biomeMaterials.put(Biome.RIVER, Material.SEAGRASS);
        biomeMaterials.put(Biome.NETHER_WASTES, Material.NETHERRACK);
        biomeMaterials.put(Biome.THE_END, Material.END_STONE);
        biomeMaterials.put(Biome.FROZEN_OCEAN, Material.ICE);
        biomeMaterials.put(Biome.FROZEN_RIVER, Material.ICE);
        biomeMaterials.put(Biome.SNOWY_TUNDRA, Material.SNOWBALL);
        biomeMaterials.put(Biome.SNOWY_MOUNTAINS, Material.SNOWBALL);
        biomeMaterials.put(Biome.MUSHROOM_FIELDS, Material.BROWN_MUSHROOM);
        biomeMaterials.put(Biome.MUSHROOM_FIELD_SHORE, Material.BROWN_MUSHROOM);
        biomeMaterials.put(Biome.BEACH, Material.SAND);
        biomeMaterials.put(Biome.DESERT_HILLS, Material.SANDSTONE);
        biomeMaterials.put(Biome.WOODED_HILLS, Material.DARK_OAK_WOOD);
        biomeMaterials.put(Biome.TAIGA_HILLS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.MOUNTAIN_EDGE, Material.COBBLESTONE);
        biomeMaterials.put(Biome.JUNGLE, Material.JUNGLE_LOG);
        biomeMaterials.put(Biome.JUNGLE_HILLS, Material.JUNGLE_LOG);
        biomeMaterials.put(Biome.JUNGLE_EDGE, Material.JUNGLE_LOG);
        biomeMaterials.put(Biome.DEEP_OCEAN, Material.WATER_BUCKET);
        biomeMaterials.put(Biome.STONE_SHORE, Material.STONE);
        biomeMaterials.put(Biome.SNOWY_BEACH, Material.SNOWBALL);
        biomeMaterials.put(Biome.BIRCH_FOREST, Material.BIRCH_LOG);
        biomeMaterials.put(Biome.BIRCH_FOREST_HILLS, Material.BIRCH_LOG);
        biomeMaterials.put(Biome.DARK_FOREST, Material.DARK_OAK_LOG);
        biomeMaterials.put(Biome.SNOWY_TAIGA, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.SNOWY_TAIGA_HILLS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.GIANT_TREE_TAIGA, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.GIANT_TREE_TAIGA_HILLS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.WOODED_MOUNTAINS, Material.OAK_LOG);
        biomeMaterials.put(Biome.SAVANNA, Material.ACACIA_LOG);
        biomeMaterials.put(Biome.SAVANNA_PLATEAU, Material.ACACIA_LOG);
        biomeMaterials.put(Biome.BADLANDS, Material.TERRACOTTA);
        biomeMaterials.put(Biome.WOODED_BADLANDS_PLATEAU, Material.TERRACOTTA);
        biomeMaterials.put(Biome.BADLANDS_PLATEAU, Material.TERRACOTTA);
        biomeMaterials.put(Biome.SMALL_END_ISLANDS, Material.END_STONE);
        biomeMaterials.put(Biome.END_MIDLANDS, Material.END_STONE);
        biomeMaterials.put(Biome.END_HIGHLANDS, Material.END_STONE);
        biomeMaterials.put(Biome.END_BARRENS, Material.END_STONE);
        biomeMaterials.put(Biome.WARM_OCEAN, Material.TROPICAL_FISH_BUCKET);
        biomeMaterials.put(Biome.LUKEWARM_OCEAN, Material.TROPICAL_FISH_BUCKET);
        biomeMaterials.put(Biome.COLD_OCEAN, Material.WATER_BUCKET);
        biomeMaterials.put(Biome.DEEP_WARM_OCEAN, Material.WATER_BUCKET);
        biomeMaterials.put(Biome.DEEP_LUKEWARM_OCEAN, Material.WATER_BUCKET);
        biomeMaterials.put(Biome.DEEP_COLD_OCEAN, Material.WATER_BUCKET);
        biomeMaterials.put(Biome.DEEP_FROZEN_OCEAN, Material.BLUE_ICE);
        biomeMaterials.put(Biome.THE_VOID, Material.STRUCTURE_VOID);
        biomeMaterials.put(Biome.SUNFLOWER_PLAINS, Material.SUNFLOWER);
        biomeMaterials.put(Biome.DESERT_LAKES, Material.SAND);
        biomeMaterials.put(Biome.GRAVELLY_MOUNTAINS, Material.GRAVEL);
        biomeMaterials.put(Biome.FLOWER_FOREST, Material.CORNFLOWER);
        biomeMaterials.put(Biome.TAIGA_MOUNTAINS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.SWAMP_HILLS, Material.VINE);
        biomeMaterials.put(Biome.ICE_SPIKES, Material.FROSTED_ICE);
        biomeMaterials.put(Biome.MODIFIED_JUNGLE, Material.JUNGLE_LOG);
        biomeMaterials.put(Biome.MODIFIED_JUNGLE_EDGE, Material.JUNGLE_LOG);
        biomeMaterials.put(Biome.TALL_BIRCH_FOREST, Material.BIRCH_LOG);
        biomeMaterials.put(Biome.TALL_BIRCH_HILLS, Material.BIRCH_LOG);
        biomeMaterials.put(Biome.DARK_FOREST_HILLS, Material.DARK_OAK_LOG);
        biomeMaterials.put(Biome.SNOWY_TAIGA_MOUNTAINS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.GIANT_SPRUCE_TAIGA, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.GIANT_SPRUCE_TAIGA_HILLS, Material.SPRUCE_LOG);
        biomeMaterials.put(Biome.MODIFIED_GRAVELLY_MOUNTAINS, Material.GRAVEL);
        biomeMaterials.put(Biome.SHATTERED_SAVANNA, Material.COARSE_DIRT);
        biomeMaterials.put(Biome.SHATTERED_SAVANNA_PLATEAU, Material.COARSE_DIRT);
        biomeMaterials.put(Biome.ERODED_BADLANDS, Material.TERRACOTTA);
        biomeMaterials.put(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, Material.TERRACOTTA);
        biomeMaterials.put(Biome.MODIFIED_BADLANDS_PLATEAU, Material.TERRACOTTA);
        biomeMaterials.put(Biome.BAMBOO_JUNGLE, Material.BAMBOO);
        biomeMaterials.put(Biome.BAMBOO_JUNGLE_HILLS, Material.BAMBOO);
        biomeMaterials.put(Biome.SOUL_SAND_VALLEY, Material.SOUL_SAND);
        biomeMaterials.put(Biome.CRIMSON_FOREST, Material.CRIMSON_NYLIUM);
        biomeMaterials.put(Biome.WARPED_FOREST, Material.WARPED_NYLIUM);
        biomeMaterials.put(Biome.BASALT_DELTAS, Material.BASALT);
    }

    private final Player player;
    private final PlotAPI plotApi;
    private Plot plot;
    private boolean open = true;

    public PMenuPlotInfoMenu(Player player, @Nullable Plot plot) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;

        plotApi = new PlotAPI();

        if (plot == null) {
            Optional<PlotArea> possiblePlotRegion = plotApi.getPlotAreas(player.getWorld().getName()).stream().filter(plotArea -> plotArea.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY())).findFirst();

            if (possiblePlotRegion.isEmpty()) {
                open = false;
                PMenuI18N.NOT_ON_A_PLOT.send(player);
                return;
            }

            PlotArea plotArea = possiblePlotRegion.get();

            plot = plotArea.getPlot(Location.at(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
            if (plot == null) {
                open = false;
                PMenuI18N.NOT_ON_A_PLOT.send(player);
            }
            this.plot = plot;
        }

        if (plot != null && plot.getOwner() == null) {
            open = false;
            PMenuI18N.PLOT_NOT_CLAIMED.send(player);
        }
    }

    @Override
    public void open() {
        if (open) super.open();
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.TITLE_PLOTINFO.get(player);
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {
        /*
         * Infos:
         * - Owner
         * - ID
         * - Creation
         * - Alias
         * - Biom
         * - Vertraut
         * - Mitglieder
         * - Verboten
         * - Flags
         * - Beschreibung
         * */
        List<PlotFlag<?, ?>> flags = plot.getFlags().stream().collect(Collectors.toList());
        String[] flagsRender = new String[Math.max(flags.size(), 1)];

        for (int i = 0; i < flags.size(); i++) {
            flagsRender[i] = "§7- " + flags.get(i).getName() + ": " + flags.get(i).toString();
        }

        if (flags.size() == 0) {
            flagsRender[0] = PMenuI18N.PLOTINFO_FLAGS_NONE.get(player);
        }

        // Can load whatever it wants, should be in side thread
        @SuppressWarnings("deprecation") Location centerLocation = plot.getCenterSynchronous();
        org.bukkit.Location bukkitCenterLocation = Objects.requireNonNull(Bukkit.getWorld(centerLocation.getWorldName())).getHighestBlockAt(centerLocation.getX(), centerLocation.getZ()).getLocation();
        Biome biome = bukkitCenterLocation.getWorld().getBiome(bukkitCenterLocation.getBlockX(), bukkitCenterLocation.getBlockY(), bukkitCenterLocation.getBlockZ());

        @NonNull HashSet<UUID> members = plot.getMembers();
        @NonNull HashSet<UUID> trusted = plot.getTrusted();
        @NonNull HashSet<UUID> denied = plot.getDenied();

        String[] playerRenderer = new String[
                5 +
                        (members.size() == 0 ? 1 : members.size()) +
                        (trusted.size() == 0 ? 1 : trusted.size()) +
                        (denied.size() == 0 ? 1 : denied.size())
                ];

        int i = 0;

        // Members
        playerRenderer[i] = PMenuI18N.PLOTINFO_PEOPLE_MEMBER.get(player);
        i++;

        if(members.size() == 0) {
            playerRenderer[i] = "§7- " + PMenuI18N.PLOTINFO_PEOPLE_NONE.get(player);
            i++;
        } else {
            for (UUID ppl : members) {
                playerRenderer[i] = "§7- " + Bukkit.getOfflinePlayer(ppl).getName();
                i++;
            }
        }

        playerRenderer[i] = "";
        i++;

        // Trusted
        playerRenderer[i] = PMenuI18N.PLOTINFO_PEOPLE_TRUSTED.get(player);
        i++;

        if(members.size() == 0) {
            playerRenderer[i] = "§7- " + PMenuI18N.PLOTINFO_PEOPLE_NONE.get(player);
            i++;
        } else {
            for (UUID ppl : trusted) {
                playerRenderer[i] = "§7- " + Bukkit.getOfflinePlayer(ppl).getName();
                i++;
            }
        }

        playerRenderer[i] = "";
        i++;

        // Banned
        playerRenderer[i] = PMenuI18N.PLOTINFO_PEOPLE_BANNED.get(player);
        i++;

        if(members.size() == 0) {
            playerRenderer[i] = "§7- " + PMenuI18N.PLOTINFO_PEOPLE_NONE.get(player);
        } else {
            for (UUID ppl : denied) {
                playerRenderer[i] = "§7- " + Bukkit.getOfflinePlayer(ppl).getName();
                i++;
            }
        }


        inventory.setItem(1, makeItem(
                Material.NAME_TAG,
                PMenuI18N.PLOTINFO_FLAGS.get(player),
                flagsRender
        ));

        inventory.setItem(4, makeItem(
                biomeMaterials.getOrDefault(biome, Material.GRASS_BLOCK),
                PMenuI18N.PLOTINFO_SOMEONES_PLOT.get(player).replace("%p%", Objects.requireNonNull(Bukkit.getOfflinePlayer(Objects.requireNonNull(plot.getOwner())).getName())),
                PMenuI18N.PLOTINFO_SOMEONES_PLOT_ID.get(player) + plot.getId().toSeparatedString(":"),
                PMenuI18N.PLOTINFO_SOMEONES_PLOT_ALIAS.get(player) + (plot.getAlias().equals("") ?  PMenuI18N.PLOTINFO_SOMEONES_PLOT_ALIAS_NONE.get(player) : plot.getAlias()),
                PMenuI18N.PLOTINFO_SOMEONES_PLOT_BIOME.get(player) + biome

        ));

        inventory.setItem(7, makeItem(
                Material.PLAYER_HEAD,
                PMenuI18N.PLOTINFO_PEOPLE.get(player),
                playerRenderer
        ));

        setFillerGlass();
    }
}
