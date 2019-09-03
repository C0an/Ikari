package club.coan.ikari.commands.admin;

import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.utils.ServerType;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.CommandArgs;
import club.coan.rinku.other.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ServerTypeCommands {

    @Command(name = "servertype", usage = "<servertype>", description = "Set the active server type", fancyUsageMessage = true, permission = "ikari.servertype")
    public void sTypeCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        s.sendMessage(BukkitUtils.CHAT_STRAIGHT_LINE);
        s.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "ServerType Commands");
        s.sendMessage("");
        s.sendMessage(ChatColor.YELLOW + c.getCommand().getLabel() + " list " + ChatColor.GRAY + "-" + ChatColor.WHITE + " List all available server types.");
        s.sendMessage(ChatColor.YELLOW + c.getCommand().getLabel() + " set <servertype> " + ChatColor.GRAY + "-" + ChatColor.WHITE + " Set the active server type");
        s.sendMessage(BukkitUtils.CHAT_STRAIGHT_LINE);
    }

    @Command(name = "servertype.list", description = "List all available server types", permission = "ikari.servertype")
    public void sTypeListCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        s.sendMessage(BukkitUtils.CHAT_STRAIGHT_LINE);
        s.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "ServerTypes " + ChatColor.GRAY + "(" + ServerType.getServerTypes().size() + ")");
        s.sendMessage("");
        ServerType.getServerTypes().forEach(serverType -> {
            s.sendMessage(ChatColor.YELLOW + serverType.getTypeName());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Deathban: " + (serverType.isDeathban() ? ChatColor.GREEN : ChatColor.RED) + serverType.isDeathban());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Natural Regen: " + (serverType.isNaturalRegen() ? ChatColor.GREEN : ChatColor.RED) + serverType.isNaturalRegen());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Lose DTR: " + (serverType.isLoseDTR() ? ChatColor.GREEN : ChatColor.RED) + serverType.isLoseDTR());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Instant Respawn: " + (serverType.isInstantRespawn() ? ChatColor.GREEN : ChatColor.RED) + serverType.isInstantRespawn());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Player Data on SB: " + (serverType.isPlayerDataOnSB() ? ChatColor.GREEN : ChatColor.RED) + serverType.isPlayerDataOnSB());
            s.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "Starting Balance: " + ChatColor.WHITE + IkariSettings.getServerCurrency() + serverType.getStartingBalance());
        });
        s.sendMessage(BukkitUtils.CHAT_STRAIGHT_LINE);
    }

    @Command(name = "servertype.set", description = "Set the active server type", usage = "<servertype>", minArg = 1, fancyUsageMessage = true, permission = "ikari.servertype")
    public void sTypeSetCmd(CommandArgs c) {
        CommandSender s = c.getSender();
        String[] args = c.getArgs();
        ServerType serverType = ServerType.getServerType(args[0]);
        if(serverType == null) {
            s.sendMessage(ChatColor.RED + "There is no such server type with the name \"" + args[0] + "\".");
            return;
        }
        if(ServerType.getActiveServerType() == serverType) {
            s.sendMessage(ChatColor.RED + "This server type is already active.");
            return;
        }
        ServerType.setActiveServerType(serverType);
        ServerType.save();
        s.sendMessage(ChatColor.GREEN + "Successfully set the server type to \"" + serverType.getTypeName() + "\".");
    }

}
