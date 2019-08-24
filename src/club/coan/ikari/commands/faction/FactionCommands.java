package club.coan.ikari.commands.faction;

import club.coan.ikari.utils.Locale;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.ikari.utils.command.Command;
import club.coan.ikari.utils.command.CommandArgs;
import club.coan.ikari.utils.command.Completer;
import org.apache.commons.lang.StringUtils;
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
            p.sendMessage("§cur in faction kek");
            return;
        }
        f = Faction.getFaction(args[0]);
        if(f != null) {
            p.sendMessage("§calready a fac with name " + f.getName());
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
        p.sendMessage("§akek made faction by name " + f.getName() + "!");
    }

    @Command(name = "f.createsystem", aliases = {"faction.createsystem", "team.createsystem", "t.createsystem", "fac.createsystem"}, minArg = 1, usage = "createsystem <name>", description = "Create a system faction", inGameOnly = true, fancyUsageMessage = true)
    public void createSystemCmd(CommandArgs c) {
        Player p = c.getPlayer();
        String[] args = c.getArgs();
        Faction f = Faction.getFaction(args[0]);
        if(f != null) {
            p.sendMessage("§calready a fac with name " + f.getName());
            return;
        }
        f = new Faction(UUID.randomUUID(), args[0], null);
        f.setFlags(new ArrayList<>(Collections.singleton(Flags.SYSTEM)));
        p.sendMessage("§akek made system faction by name " + f.getName() + "!");
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
            p.sendMessage(ChatColor.RED + "Only leaders can disband factions.");
            return;
        }
        f.disband();
    }

    @Command(name = "f.who", minArg = 1, fancyUsageMessage = true, usage = "who <faction>", description = "Information bout fac kek")
    public void whoCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String[] args = c.getArgs();
        List<Faction> f = Faction.getFactions(args[0], true);
        if(f.size() == 0) {
            s.sendMessage("fac not found");
            return;
        }
        f.forEach(faction -> faction.sendInfo(s));
    }

    @Command(name = "f.save", aliases = {"faction.save", "fac.save", "t.save", "team.save"}, description = "Save all factions")
    public void saveCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        org.bukkit.command.Command.broadcastCommandMessage(s, "Performed a save task.");
        new Thread(() -> Faction.saveAll(callback -> org.bukkit.command.Command.broadcastCommandMessage(s, callback))).start();
    }


}
