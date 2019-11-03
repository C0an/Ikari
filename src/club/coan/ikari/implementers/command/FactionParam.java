package club.coan.ikari.implementers.command;

import club.coan.christian.shared.utils.MojangUtils;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.ikari.utils.ServerType;
import club.coan.rinku.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FactionParam implements ParameterType<Faction> {

    @Override
    public Faction transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
            Faction team = Faction.getFactionOf(((Player) sender).getUniqueId());

            if (team == null) {
                sender.sendMessage(ChatColor.GRAY + "You're not in a faction!");
                return (null);
            }

            return (team);
        }

        Faction byName = Faction.getFaction(source);

        if (byName != null) {
            return (byName);
        }

        Player bukkitPlayer = Bukkit.getPlayer(source);

        if (bukkitPlayer != null) {
            Faction byMemberBukkitPlayer = Faction.getFactionOf(bukkitPlayer.getUniqueId());

            if (byMemberBukkitPlayer != null) {
                return (byMemberBukkitPlayer);
            }
        }

        Faction byMemberUUID = null;
        try {
            byMemberUUID = Faction.getFactionOf(MojangUtils.fetchUUID(source));
        } catch (Exception e) {
        }

        if (byMemberUUID != null) {
            return (byMemberUUID);
        }

        sender.sendMessage(ChatColor.RED + "No team or member with the name " + source + " found.");
        return (null);
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (Faction faction : Faction.getFactions().stream().filter(faction -> !faction.getFlags().contains(Flags.HIDE_FACTION_INFO)).collect(Collectors.toList())) {
            if (StringUtils.startsWithIgnoreCase(faction.getName(), source)) {
                completions.add(faction.getName());
            }
        }
        return completions;
    }
}
