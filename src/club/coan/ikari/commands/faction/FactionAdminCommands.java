package club.coan.ikari.commands.faction;

import club.coan.ikari.Ikari;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class FactionAdminCommands {

    @Command(name = "f.createsystem", aliases = {"faction.createsystem", "team.createsystem", "t.createsystem", "fac.createsystem"}, minArg = 1, usage = "createsystem <name>", description = "Create a system faction", inGameOnly = true, fancyUsageMessage = true, permission = "ikari.createsystem")
    public void createSystemCmd(CommandArgs c) {
        Player p = c.getPlayer();
        String[] args = c.getArgs();
        Faction f = Faction.getFaction(args[0]);
        if(f != null) {
            p.sendMessage("§cThere is already a faction with the name \"" + f.getName() + "\".");
            return;
        }
        f = new Faction(UUID.randomUUID(), args[0], null);
        f.setFlags(new ArrayList<>(Collections.singleton(Flags.SYSTEM)));
        p.sendMessage("§aCreated a System Faction with the name \"" + f.getName() + "\", to add flags to the team do /flags.");
    }

    @Command(name = "f.forcesave", aliases = {"faction.forcesave", "fac.forcesave", "t.forcesave", "team.forcesave"}, description = "Save all factions", permission = "ikari.forcesave")
    public void saveCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        org.bukkit.command.Command.broadcastCommandMessage(s, "Performed a save task.");
        new Thread(() -> Faction.saveAll(callback -> org.bukkit.command.Command.broadcastCommandMessage(s, callback))).start();
    }

    @Command(name = "f.backup", aliases = {"faction.backup", "fac.backup", "t.backup", "team.backup"}, usage = "backup <string...>", description = "Create a backup file", permission = "ikari.backup", minArg = 1, fancyUsageMessage = true)
    public void backupCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String[] args = c.getArgs();
        String name = StringUtils.join(args, ' ');
        org.bukkit.command.Command.broadcastCommandMessage(s, "Creating a backup with the name \"" + name + "\".");
        Ikari.getIkariDatabase().backup(name, callback -> {
            if(!callback) {
                org.bukkit.command.Command.broadcastCommandMessage(s, ChatColor.RED + "Failed to create the backup \"" + name + "\", maybe it already exists?");
            }else {
                org.bukkit.command.Command.broadcastCommandMessage(s, ChatColor.GREEN + "Successfully created a backup with the name \"" + name + "\".");
            }
        });
    }

}
