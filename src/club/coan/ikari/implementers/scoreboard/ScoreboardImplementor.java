package club.coan.ikari.implementers.scoreboard;

import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.utils.ServerType;
import club.coan.rinku.scoreboard.AssembleAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardImplementor implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return "&6&l" + IkariSettings.getServerName() + " &c[Map " + IkariSettings.getMap() + "]";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m--------------------");
        if(ServerType.getActiveServerType().isPlayerDataOnSB()) {
            lines.add(" * " + ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + "0");
            lines.add(" * " + ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + "0");
            lines.add(" * " + ChatColor.YELLOW + "Balance: " + ChatColor.WHITE + IkariSettings.getServerCurrency() + "0");
        }
        lines.add("&7&m--------------------");
        return (lines.size() <= 2 ? null : lines);
    }
}
