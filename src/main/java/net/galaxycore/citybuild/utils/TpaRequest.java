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
    @Getter
    private long timeOfCreation;

    public void accept() {
        if (reversed) {
            requester.teleport(requested.getLocation());
            return;
        }
        requested.teleport(requester.getLocation());
    }

    public void delayedAccept() {
        if(timeOfCreation + (300000 /*300000ms = 5 min*/) < System.currentTimeMillis()) {
            requested.sendMessage(I18N.getByPlayer(requested, "citybuild.tpa.expired"));
            requester.sendMessage(I18N.getByPlayer(requester, "citybuild.tpa.expired"));
            return;
        }
        if (reversed) {
            requested.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requested, "citybuild.tpahere.self.accept"), new LuckPermsApiWrapper(requester)));
            requester.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requester, "citybuild.tpa.load"), new LuckPermsApiWrapper(requested)));
        } else {
            requester.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requester, "citybuild.tpa.load"), new LuckPermsApiWrapper(requested)));
            requested.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(requested, "citybuild.tpa.self.accept"), new LuckPermsApiWrapper(requester)));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                accept();
            }
        }.runTaskLater(Essential.getInstance(), 60);
    }
}
