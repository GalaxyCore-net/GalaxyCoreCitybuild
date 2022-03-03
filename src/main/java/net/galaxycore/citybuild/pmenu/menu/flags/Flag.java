package net.galaxycore.citybuild.pmenu.menu.flags;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.PlotFlag;
import com.plotsquared.core.plot.flag.implementations.*;
import com.plotsquared.core.plot.flag.types.BlockTypeListFlag;
import com.plotsquared.core.plot.flag.types.BooleanFlag;
import com.plotsquared.core.plot.flag.types.LongFlag;
import com.plotsquared.core.plot.flag.types.StringFlag;
import lombok.Getter;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public enum Flag {
    EXPLOSION(ExplosionFlag.class, "§eExplosion", "§eExplosions", "§eAktiviere oder deaktiviere Explosionen", "§eActivate or deactivate explosions", "explosion"),
    UNTRUSTED(UntrustedVisitFlag.class, "§eNicht vertraue Spiele", "§eUntrusted Players", "§eErlaube nicht vertraute Spieler auf deinem Plot", "§eAllow untrusted Players on your plot", "untrusted"),
    DESCRIPTION(DescriptionFlag.class, "§eBeschreibung", "§eDescription", "§eSetzte eine Beschreibung für dein Plot", "§eSet a description for your plot", "description"),
    GREETING(GreetingFlag.class, "§eBegrüßung", "§eGreeting", "§eSetzte eine Begrüßung für dein Plot", "§eSet a greeting for your plot", "greeting"),
    FAREWELL(FarewellFlag.class, "§eLebewohl", "§eFarewell", "§eSetzte ein Lebewohl für dein Plot", "§eSet a farewell for your plot", "farewell"),
    WEATHER(WeatherFlag.class, "§eWetter", "§eWeather", "§eSetze das Plot-Wetter", "§eSet the Plot-Weather", "weather"),
    ANIMAL_ATTACK(AnimalAttackFlag.class, "§eTier-PVE", "§eAnimal Attack", "§eErlaube es, anderen Spielern Tiere auf deinem Plot anzugreifen", "§eAllow other players to attack animals on your plot", "animal-attack"),
    BLOCK_BURN(BlockBurnFlag.class, "§eBlock-Verbrennen", "§eBlock-Burn", "§eErlaube es Blöcken auf deinem Plot zu verbrennen", "§eAllow blocks to burn on your plot", "block-burn"),
    BLOCK_IGNITION(BlockIgnitionFlag.class, "§eBlock Anzünden", "§eBlock Ignition", "§eErlaube es, auf deinem Plot Blöcke anzuzünden", "§eAllow blocks on your plot to be ignited", "block-ignition"),
    BREAK(BreakFlag.class, "§eAbbau", "§eBreak", "§eErlaube es anderen Spielern, bestimmte Blöcke auf deinem Plot abzubauen", "§eAllow other players to break certain blocks on your plot", "break"),
    DEVICE_INTERACT(DeviceInteractFlag.class, "§eGeräte-Interation", "§eDevice Interaction", "§eErlaube es anderen Spielern mit Gerätschaften wie Druckplatten interagieren", "§eAllow other players to interact with devices like pressure plates", "device-interact"),
    HOSTILE_ATTACK(HostileAttackFlag.class, "§eGegner-PVE", "§eHostile Attack", "§eErlaube es, anderen Spielern Gegner auf deinem Plot anzugreifen", "§eAllow other players to attack hostile mobs on your plot", "hostile-attack"),
    HOSTILE_INTERACT(HostileInteractFlag.class, "§eGegner-Interaktion", "§eHostile Interaction", "§eErlaube es, anderen Spielern mit Gegnern auf deinem Plot zu interagieren", "§eAllow other players to interact with hostile mobs on your plot", "hostile-interact"),
    PLACE(PlaceFlag.class, "§ePlazieren", "§ePlace", "§eErlaube es anderen Spielern, bestimmte Blöcke auf deinem Plot zu plazieren", "§eAllow other players to place certain blocks on your plot", "place"),
    PVE(PveFlag.class, "§ePVP", "§ePVP", "§eErlaube Spieler-versus-Lebewesen Kämpfe", "§eAllow Player-vs-Entity Fights", "pve"),
    PVP(PvpFlag.class, "§ePVE", "§ePVE", "§eErlaube Spieler-versus-Spieler Kämpfe", "§eAllow Player-vs-Player Fights", "pvp"),
    REDSTONE(RedstoneFlag.class, "§eRedstone", "§eRedstone", "§eErlaube Redstone auf diesem Plot", "§eAllow Redstone on this Plot", "redstone"),
    CROP_GROW(CropGrowFlag.class, "§ePflanzenwachstum", "§eCrop Growth", "§eErlaube Pflanzenwachstum", "§eAllow Crop-Growth", "crop-grow"),
    TIME(TimeFlag.class, "§eZeit", "§eTime", "§eSetzte die Plot-Zeit", "§eSet the Plot-Time", "time"),
    USE(UseFlag.class, "§eNutzung", "§eUse", "§eErlaube es anderen Spielern, bestimmte Blöcke auf deinem Plot zu nutzen", "§eAllow other players to use certain blocks on your plot", "use"),
    VEHICLE_BREAK(VehicleBreakFlag.class, "§eFahrzeug-Abbau", "§eVehicle Break", "§eErlaube es anderen Spielern Fahrzeuge abzubauen", "§eAllow other Players to break vehicles on your plot", "vehicle-break"),
    VEHICLE_PLACE(VehiclePlaceFlag.class, "§eFahrzeug-Plazierung", "§eVehicle Placement", "§eErlaube es anderen Spielern Fahrzeuge zu plazieren", "§eAllow other Players to place vehicles on your plot", "vehicle-place"),
    VEHICLE_USE(VehicleUseFlag.class, "§eFahrzeug-Nutzung", "§eVehicle Use", "§eErlaube es anderen Spielern Fahrzeuge zu nutzen", "§eAllow other Players to use vehicles on your plot", "vehicle-use"),
    VILLAGER_INTERACT(VillagerInteractFlag.class, "§eDorfbewohner-Interaktion", "§eVillager-Interaction", "§eErlaube es anderen Spielern, mit Dorfbewohnern auf deinem Plot zu handeln", "§eAllow other players to trade with villagers on your plot", "villager-interact"),
    LECTERN_READ_BOOK(LecternReadBookFlag.class, "§eLesepult-Interaktion", "§eLectern-Interaction", "§eErlaube anderen Spielern, auf deinem Plot Bücher über Lesepulte zu lesen", "§eAllow other players to read books on a lectern within your plot", "lectern-read-book"),
    LEAF_DECAY(LeafDecayFlag.class, "§eBlätter-Zerfall", "§eLeaf Decay", "§eErlaube Blättern auf deinem Plot zu zerfallen", "§eAllow Leafs on your plot to decay", "leaf-decay");

    private final Class<? extends PlotFlag<?, ?>> flagClass;
    private final String nameGer;
    private final String nameEn;
    private final String descGer;
    private final String descEn;
    private final String flagName;

    Flag(Class<? extends PlotFlag<?, ?>> flagClass, String nameGer, String nameEn, String descGer, String descEn, String flagName) {
        this.flagClass = flagClass;
        this.nameGer = nameGer;
        this.nameEn = nameEn;
        this.descGer = descGer;
        this.descEn = descEn;
        this.flagName = flagName;

        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.flag." + flagName, nameGer, false);
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.flag.desc." + flagName, descGer, false);
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.flag." + flagName, nameEn, false);
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.flag.desc." + flagName, descEn, false);
    }

    public ItemStack item(Plot plot, Player player) {
        Optional<PlotFlag<?, ?>> flag = plot.getFlags().stream().filter(plotFlag -> plotFlag.getName().equalsIgnoreCase(flagName)).findFirst();
        List<Component> lore = new ArrayList<>();
        Material material = Material.LIME_STAINED_GLASS;

        if (flag.isPresent()) {
            material = Material.LIME_TERRACOTTA;
            lore.add(Component.text(PMenuI18N.FLAGS_CURRENTVAL.get(player) + flag.get()));
        }

        lore.add(Component.text(I18N.getByPlayer(player, "citybuild.pmenu.flag.desc." + flagName)));

        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(I18N.getByPlayer(player, "citybuild.pmenu.flag." + flagName)));
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    public void openUI(Player player, Plot plot) {
        player.closeInventory();
        Class<?> superclass = flagClass.getSuperclass();

        if (superclass == BooleanFlag.class) {
            new BooleanFlagMenu(player, plot, this).open();
        } else if (superclass == StringFlag.class) {
            new StringFlagMenu(player, plot, this).open();
        } else if (superclass == PlotFlag.class) {
            new WeatherFlagMenu(player, plot, this).open();
        } else if (superclass == LongFlag.class) {
            new LongFlagMenu(player, plot, this).open();
        } else if (superclass == BlockTypeListFlag.class) {
            new BlockListFlagMenu(player, plot, this).open();
        }
    }
}
