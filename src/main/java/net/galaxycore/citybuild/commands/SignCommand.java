package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

public class SignCommand implements CommandExecutor {
    private final HashMap<Player, Long> signCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.sign")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        if (signCooldown.containsKey(player)) {
            if (!player.hasPermission("citybuild.command.sign.cooldown.bypass")) {
                if (signCooldown.get(player) < System.currentTimeMillis()) {
                    signCooldown.remove(player);
                } else {
                    player.sendMessage(I18N.getByPlayer(player, "citybuild.cooldown")
                            .replace("%time%", new SimpleDateFormat(
                                    I18N.getInstanceRef().get().getLanguages()
                                            .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat() + " " +
                                            I18N.getInstanceRef().get().getLanguages()
                                                    .get(I18N.getInstanceRef().get().getLocale(player)).getTimeFormat())
                                    .format(new Date(signCooldown.get(player)))));
                    return true;
                }
            }
        }

        String text = "";

        if (args.length > 0) {
            text = String.join(" ", args);
        }

        signCooldown.put(player, System.currentTimeMillis() + 86400000); // 86400000ms = 24H
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();

        if (itemMeta.lore() != null)
            lore.addAll(itemMeta.lore());

        if(!text.equals(""))
            lore.add(Component.text("§7" + text));

        lore.add(Component.text(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.sign.text"), new LuckPermsApiWrapper(player)).replaceAll("%time%", new SimpleDateFormat(
                I18N.getInstanceRef().get().getLanguages()
                        .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat())
                .format(new Date(System.currentTimeMillis())))));

        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        player.sendMessage(I18N.getByPlayer(player, "citybuild.sign.success"));
        return true;
    }
}
