package club.coan.ikari.commands.faction;

import club.coan.ikari.utils.Locale;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.ikari.utils.command.Command;
import club.coan.ikari.utils.command.CommandArgs;
import club.coan.ikari.utils.command.Completer;
import org.apache.commons.lang.StringUtils;
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

    @Command(name = "f.who", minArg = 1)
    public void whoCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String[] args = c.getArgs();
        List<Faction> f = Faction.getFactions(args[0]);
        if(f == null || f.isEmpty()) {
            s.sendMessage("fac not found");
            return;
        }
        f.forEach(Faction::sendInfo);
        s.sendMessage(f.getName() + " - " + (f.isSystem() ? "System Faction" : "Leader: " + f.getLeader()));
    }

    @Command(name = "f.test", aliases = {"faction.test", "team.test", "t.test", "fac.test"}, fancyUsageMessage = true, minArg = 1, usage = "test <msg...>", description = "Send a test message")
    public void testCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String msg = StringUtils.join(c.getArgs(), ' ');
        s.sendMessage("§cThis is a test command, hello " + c.getFormattedDisplayName());
        s.sendMessage("§a" + msg);
        s.sendMessage(Locale.USAGEMESSAGE.toString().replaceAll("%command%", c.getCommand().getLabel()).replaceAll("%usage%", c.getCommand().getUsage()));
        s.sendMessage(Locale.USAGEMESSAGE.toString());
    }

    @Completer(name = "f.test", aliases = {"faction.test", "team.test", "t.test", "fac.  test"})
    public List<String> testCmpltr(CommandArgs c) {
        List<String> list = new ArrayList<>();
        if(c.getArgs().length == 0) {
            return null;
        }else {
            list.add("Hi, my name is " + c.getSender().getName());
            list.add("Oh, did you know that tab completion works?");
            list.add("Cool!");
        }
        return list;
    }

}
