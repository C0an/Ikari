package club.coan.ikari.commands.faction;

import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.events.FactionCreateEvent;
import club.coan.ikari.faction.events.FactionDisbandEvent;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionCommands {

    @Command(names = {"faction create", "f create", "team create", "t create", "fac create"}, permission = "", description = "Create a faction")
    public static void createCmd(Player p, @Param(name = "name") String name) {
        Faction f = Faction.getFactionOf(p);
        if(f != null) {
            p.sendMessage("§cYou are already in a faction.");
            return;
        }
        f = Faction.getFaction(name);
        if(f != null) {
            p.sendMessage("§cThere is already a faction with the name \"" + f.getName() + "\".");
            return;
        }
        f = new Faction(UUID.randomUUID(), name, p.getUniqueId());

        f.save(callback -> {
            if(callback) {
                System.out.println("[Ikari] Saved faction " + name + " to the database.");
            }else {
                System.out.println("[Ikari] Failed to save the faction " + name + " to the database.");
            }
        });
        Bukkit.getPluginManager().callEvent(new FactionCreateEvent(p, f));
    }


    @Command(names ={"faction disband", "f disband","fac disband", "team disband", "t disband"},description = "Disband your faction", permission = "")
    public static void disbandCmd(Player p) {
        Faction f = Faction.getFactionOf(p);
        if(f == null) {
            p.sendMessage("§cur not in faction kek");
            return;
        }
        if(!f.getLeader().toString().equals(p.getUniqueId().toString())) {
            p.sendMessage(ChatColor.RED + "Only faction leaders can disband factions.");
            return;
        }
        Bukkit.getPluginManager().callEvent(new FactionDisbandEvent(p, f));
        f.disband();
    }

    @Command(names ={"faction who", "f who","fac who", "team who", "t who", "factioninfo", "fac info", "team info", "t info", "f info", "faction w", "fac w", "team w", "t w", "f w"}, description = "Get information about a faction", permission = "")
    public static void whoCmd(CommandSender s, @Param(name = "faction", defaultValue = "self") Faction f) {
        f.sendInfo(s);
    }

}
