package club.coan.ikari.commands.faction;

import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.events.FactionCreateEvent;
import club.coan.ikari.faction.events.FactionDisbandEvent;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class FactionCommands {

    @Command(name = "f", aliases = {"faction", "team", "t", "fac"}, inGameOnly = true)
    public void facCmd(CommandArgs c) {
        Player p = c.getPlayer();
        p.sendMessage("§cfaction help here.");
    }

    @Command(name = "f.create", aliases = {"faction.create", "team.create", "t.create", "fac.create"}, fancyUsageMessage = true, minArg = 1, usage = "create <name>", description = "Create a faction", inGameOnly = true)
    public void createCmd(CommandArgs c) {
        Player p = c.getPlayer();
        String[] args = c.getArgs();
        Faction f = Faction.getFactionOf(p);
        if(f != null) {
            p.sendMessage("§cYou are already in a faction.");
            return;
        }
        f = Faction.getFaction(args[0]);
        if(f != null) {
            p.sendMessage("§cThere is already a faction with the name \"" + f.getName() + "\".");
            return;
        }
        f = new Faction(UUID.randomUUID(), args[0], p.getUniqueId());

        f.save(callback -> {
            if(callback) {
                System.out.println("[Ikari] Saved faction " + args[0] + " to the database.");
            }else {
                System.out.println("[Ikari] Failed to save the faction " + args[0] + " to the database.");
            }
        });
        Bukkit.getPluginManager().callEvent(new FactionCreateEvent(p, f));
    }


    @Command(name = "f.disband", aliases = {"faction.disband", "fac.disband", "team.disband", "t.disband"}, description = "Disband your faction", inGameOnly = true)
    public void disbandCmd(CommandArgs c) {
        Player p = c.getPlayer();
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

    @Command(name = "f.who", aliases = {"faction.who", "fac.who", "team.who", "t.who", "faction.info", "fac.info", "team.info", "t.info", "f.info", "faction.w", "fac.w", "team.w", "t.w", "f.w"}, usage = "who [faction]", description = "Get information about a faction")
    public void whoCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String[] args = c.getArgs();
        if(args.length == 0) {
            if(!(s instanceof Player)) {
                s.sendMessage(ChatColor.RED + "This command can only be ran in-game.");
                return;
            }
            Faction f = Faction.getFaction(((Player)s).getUniqueId());
            if(f == null) {
                s.sendMessage(ChatColor.RED + "You are not in a faction.");
                return;
            }
            f.sendInfo(s);
            return;
        }
        List<Faction> f = Faction.getFactions(args[0], true);
        if(f.size() == 0) {
            s.sendMessage("fac not found");
            return;
        }
        f.forEach(faction -> faction.sendInfo(s));
    }

}
