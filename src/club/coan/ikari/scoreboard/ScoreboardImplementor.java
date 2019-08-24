package club.coan.ikari.scoreboard;

import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.scoreboard.AssembleAdapter;
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
        Faction f = Faction.getFaction(player.getLocation(), true);
        if(f != Faction.getFaction("Wilderness")) {
            lines.add("&eYour current location:");
            lines.add(" &7* " + f.getColor() + f.getName());
            lines.add(" &7* Relation: " + f.getRelationTo(player.getUniqueId()).getDisplayName());
        }
        lines.add("&7&m--------------------");
        return (lines.size() <= 2 ? null : lines);
    }
}
