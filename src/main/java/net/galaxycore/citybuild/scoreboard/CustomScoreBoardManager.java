package net.galaxycore.citybuild.scoreboard;

import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.scoreboards.IScoreBoardCallback;
import net.galaxycore.galaxycorecore.utils.ServerNameUtil;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.entity.Player;

public class CustomScoreBoardManager implements IScoreBoardCallback {
    @Override
    public String[] getKV(Player player, int id) {
        String[] kv = new String[]{"Info", "Nr.", id + ""};
        if (id == 0)
            kv = new String[]{I18N.getByPlayer(player, "citybuild.score.rank"), StringUtils.replaceRelevant("%rank_color%%rank_displayname%", new LuckPermsApiWrapper(player)), ""};
        if (id == 1)
            kv = new String[]{I18N.getByPlayer(player, "citybuild.score.coins"), "§7" + new CoinDAO(PlayerLoader.load(player), Essential.getInstance()).get(), ""};
        if (id == 2)
            kv = new String[]{I18N.getByPlayer(player, "citybuild.score.onlinetime"), "§7Kommt Bald", ""};
        if (id == 3)
            kv = new String[]{I18N.getByPlayer(player, "citybuild.score.server"), "§7" + ServerNameUtil.getName().substring(0, Math.min(ServerNameUtil.getName().length(), 12)), ""};
        if (id == 4)
            kv = new String[]{I18N.getByPlayer(player, "citybuild.score.teamspeak"), "§7GalaxyCore.net", ""};
        return kv;
    }

    @Override
    public String getTitle(Player player) {
        return "§7» §5Galaxy§6Core §7«";
    }

    @Override
    public boolean active(Player player) {
        return true;
    }
}