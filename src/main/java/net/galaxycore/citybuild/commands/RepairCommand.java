package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RepairCommand implements CommandExecutor {
    private final HashMap<Player, Long> repairCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.repair.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (repairCooldown.containsKey(player)) {
                if (!player.hasPermission("citybuild.command.repair.cooldown.bypass")) {
                    if (repairCooldown.get(player) < System.currentTimeMillis()) {
                        repairCooldown.remove(player);
                    } else {
                        player.sendMessage(I18N.getByPlayer(player, "citybuild.cooldown")
                                .replace("%time%", new SimpleDateFormat(
                                        I18N.getInstanceRef().get().getLanguages()
                                                .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat() + " " +
                                                I18N.getInstanceRef().get().getLanguages()
                                                        .get(I18N.getInstanceRef().get().getLocale(player)).getTimeFormat())
                                        .format(new Date(repairCooldown.get(player)))));
                        return true;
                    }
                }
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || isNotRepairable(item.getType())) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.repair.fail"));
                return true;
            }
            ItemMeta itemMeta = item.getItemMeta();
            ((Damageable) itemMeta).setDamage(0);
            item.setItemMeta(itemMeta);
            player.sendMessage(I18N.getByPlayer(player, "citybuild.repair.self"));
            repairCooldown.put(player, System.currentTimeMillis() + 43200000); // 43200000ms = 12H
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.repair.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            ItemStack item = target.getInventory().getItemInMainHand();
            if (item == null || isNotRepairable(item.getType())) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.repair.fail"));
                return true;
            }
            ItemMeta itemMeta = item.getItemMeta();
            ((Damageable) itemMeta).setDamage(0);
            item.setItemMeta(itemMeta);
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.repair.other"), new LuckPermsApiWrapper(player)));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.repair.other.notify"), new LuckPermsApiWrapper(target)));
        }
        return true;
    }

    private boolean isNotRepairable(Material type) {
        return !(
                type == Material.DIAMOND_AXE ||
                        type == Material.DIAMOND_SWORD ||
                        type == Material.DIAMOND_SHOVEL ||
                        type == Material.DIAMOND_PICKAXE ||
                        type == Material.DIAMOND_HOE ||
                        type == Material.WOODEN_AXE ||
                        type == Material.WOODEN_SWORD ||
                        type == Material.WOODEN_SHOVEL ||
                        type == Material.WOODEN_PICKAXE ||
                        type == Material.WOODEN_HOE ||
                        type == Material.GOLDEN_AXE ||
                        type == Material.GOLDEN_SWORD ||
                        type == Material.GOLDEN_SHOVEL ||
                        type == Material.GOLDEN_PICKAXE ||
                        type == Material.GOLDEN_HOE ||
                        type == Material.IRON_AXE ||
                        type == Material.IRON_SWORD ||
                        type == Material.IRON_SHOVEL ||
                        type == Material.IRON_PICKAXE ||
                        type == Material.IRON_HOE ||
                        type == Material.NETHERITE_AXE ||
                        type == Material.NETHERITE_SWORD ||
                        type == Material.NETHERITE_SHOVEL ||
                        type == Material.NETHERITE_PICKAXE ||
                        type == Material.NETHERITE_HOE ||
                        type == Material.STONE_AXE ||
                        type == Material.STONE_SWORD ||
                        type == Material.STONE_SHOVEL ||
                        type == Material.STONE_PICKAXE ||
                        type == Material.STONE_HOE ||
                        type == Material.DIAMOND_LEGGINGS ||
                        type == Material.DIAMOND_HELMET ||
                        type == Material.DIAMOND_BOOTS ||
                        type == Material.DIAMOND_CHESTPLATE ||
                        type == Material.IRON_LEGGINGS ||
                        type == Material.IRON_HELMET ||
                        type == Material.IRON_BOOTS ||
                        type == Material.IRON_CHESTPLATE ||
                        type == Material.GOLDEN_LEGGINGS ||
                        type == Material.GOLDEN_HELMET ||
                        type == Material.GOLDEN_BOOTS ||
                        type == Material.GOLDEN_CHESTPLATE ||
                        type == Material.CHAINMAIL_LEGGINGS ||
                        type == Material.CHAINMAIL_HELMET ||
                        type == Material.CHAINMAIL_BOOTS ||
                        type == Material.CHAINMAIL_CHESTPLATE ||
                        type == Material.LEATHER_LEGGINGS ||
                        type == Material.LEATHER_HELMET ||
                        type == Material.LEATHER_BOOTS ||
                        type == Material.LEATHER_CHESTPLATE ||
                        type == Material.BOW ||
                        type == Material.CROSSBOW ||
                        type == Material.SHIELD ||
                        type == Material.SHEARS ||
                        type == Material.FISHING_ROD ||
                        type == Material.FLINT_AND_STEEL ||
                        type == Material.ELYTRA ||
                        type == Material.TURTLE_HELMET ||
                        type == Material.TRIDENT
        );
    }
}