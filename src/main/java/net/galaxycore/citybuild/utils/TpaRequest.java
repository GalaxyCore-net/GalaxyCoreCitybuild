package net.galaxycore.citybuild.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class TpaRequest {
    @Getter
    private final Player requester;
    @Getter
    private final Player requested;
    @Getter
    private boolean reversed;

    public void accept() {
        if (reversed) {
            requested.teleport(requester.getLocation());
            return;
        }
        requester.teleport(requested.getLocation());
    }

    public void delayedAccept() {
        //TODO Ablauf der Tpa nach 5 Minuten
        if (reversed) {
            requester.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requester, "citybuild.tpahere.self.accept"), new LuckPermsApiWrapper(requested)));
            requested.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requester, "citybuild.tpa.load"), new LuckPermsApiWrapper(requester)));
        } else {
            requested.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requested, "citybuild.tpa.load"), new LuckPermsApiWrapper(requester)));
            requester.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requester, "citybuild.tpa.self.accept"), new LuckPermsApiWrapper(requested)));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                accept();
            }
        }.runTaskLater(Essential.getInstance(), 90);
    }
}
