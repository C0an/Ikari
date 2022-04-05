package club.coan.ikari.commands.faction;

import club.coan.ikari.Ikari;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.Param;
import club.coan.rinku.command.bukkit.RinkuCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class FactionAdminCommands {

    @Command(names = {"faction createsystem","f createsystem",  "team createsystem", "t createsystem", "fac createsystem"}, description = "Create a system faction", permission = "ikari.createsystem")
    public static void createSystemCmd(Player p, @Param(name = "name") String n) {
        Faction f = Faction.getFaction(n);
        if(f != null) {
            p.sendMessage("§cThere is already a faction with the name \"" + f.getName() + "\".");
            return;
        }
        f = new Faction(UUID.randomUUID(), n, null);
        f.setFlags(new ArrayList<>(Collections.singleton(Flags.SYSTEM)));
        p.sendMessage("§aCreated a System Faction with the name \"" + f.getName() + "\", to add flags to the team do /flags.");
    }

    @Command(names = {"faction forcesave", "f forcesave","fac forcesave", "t forcesave", "team forcesave"}, description = "Save all factions", permission = "ikari.forcesave", async = true)
    public static void saveCmd(CommandSender s) {
        RinkuCommand.broadcastCommandMessage(s, "Performed a save task.");
        Faction.saveAll(callback -> org.bukkit.command.Command.broadcastCommandMessage(s, callback));
    }

    @Command(names ={"faction backup", "f backup","fac backup", "t backup", "team backup"}, description = "Create a backup file", permission = "ikari.backup")
    public static void backupCmd(CommandSender s, @Param(name = "name", wildcard = true)String name) {
        RinkuCommand.broadcastCommandMessage(s, "Creating a backup with the name \"" + name + "\".");
        Ikari.getIkariDatabase().backup(name, callback -> {
            if(!callback) {
                RinkuCommand.broadcastCommandMessage(s, ChatColor.RED + "Failed to create the backup \"" + name + "\", maybe it already exists?");
            }else {
                RinkuCommand.broadcastCommandMessage(s, ChatColor.GREEN + "Successfully created a backup with the name \"" + name + "\".");
            }
        });
    }

}
