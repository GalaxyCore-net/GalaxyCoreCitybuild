package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.speed.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.1f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.1.self"));
            } else if (args[0].equalsIgnoreCase("2")) {
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.2f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.2.self"));
            } else if (args[0].equalsIgnoreCase("3")) {
                player.setWalkSpeed(0.3f);
                player.setFlySpeed(0.3f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.3.self"));
            } else if (args[0].equalsIgnoreCase("4")) {
                player.setWalkSpeed(0.4f);
                player.setFlySpeed(0.4f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.4.self"));
            } else if (args[0].equalsIgnoreCase("5")) {
                player.setWalkSpeed(0.5f);
                player.setFlySpeed(0.5f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.5.self"));
            } else if (args[0].equalsIgnoreCase("6")) {
                player.setWalkSpeed(0.6f);
                player.setFlySpeed(0.6f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.6.self"));
            } else if (args[0].equalsIgnoreCase("7")) {
                player.setWalkSpeed(0.7f);
                player.setFlySpeed(0.7f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.7.self"));
            } else if (args[0].equalsIgnoreCase("8")) {
                player.setWalkSpeed(0.8f);
                player.setFlySpeed(0.8f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.8.self"));
            } else if (args[0].equalsIgnoreCase("9")) {
                player.setWalkSpeed(0.9f);
                player.setFlySpeed(0.9f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.9.self"));
            } else if (args[0].equalsIgnoreCase("10")) {
                player.setWalkSpeed(1.0f);
                player.setFlySpeed(1.0f);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.speed.10.self"));
            }
        } else if (args.length == 2) {
            if (!player.hasPermission("citybuild.command.gamemode.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                target.setWalkSpeed(0.2f);
                target.setFlySpeed(0.1f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.1.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.1.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("2")) {
                target.setWalkSpeed(0.2f);
                target.setFlySpeed(0.2f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.2.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.2.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("3")) {
                target.setWalkSpeed(0.3f);
                target.setFlySpeed(0.3f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.3.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.3.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("4")) {
                target.setWalkSpeed(0.4f);
                target.setFlySpeed(0.4f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.4.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.4.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("5")) {
                target.setWalkSpeed(0.5f);
                target.setFlySpeed(0.5f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.5.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.5.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("6")) {
                target.setWalkSpeed(0.6f);
                target.setFlySpeed(0.6f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.6.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.6.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("7")) {
                target.setWalkSpeed(0.7f);
                target.setFlySpeed(0.7f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.7.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.7.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("8")) {
                target.setWalkSpeed(0.8f);
                target.setFlySpeed(0.8f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.8.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.8.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("9")) {
                target.setWalkSpeed(0.9f);
                target.setFlySpeed(0.9f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.9.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.9.other.notify"), new LuckPermsApiWrapper(target)));
            }
            if (args[0].equalsIgnoreCase("10")) {
                target.setWalkSpeed(1.0f);
                target.setFlySpeed(1.0f);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.10.other"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.speed.10.other.notify"), new LuckPermsApiWrapper(target)));
            }
        }else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.speed.usage")));
        }

        return true;
    }
}
