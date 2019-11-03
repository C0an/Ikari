package club.coan.ikari.commands.admin;

import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.utils.ServerType;
import club.coan.rinku.command.Command;
import club.coan.rinku.command.Param;
import club.coan.rinku.other.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ServerTypeCommands {


    @Command(names = "servertype list", description = "List all available server types", permission = "ikari.servertype")
    public static void sTypeListCmd(CommandSender s) {
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

    @Command(names = "servertype set", description = "Set the active server type", permission = "ikari.servertype")
    public static void sTypeSetCmd(CommandSender s, @Param(name = "servertype") ServerType serverType) {
        if(ServerType.getActiveServerType() == serverType) {
            s.sendMessage(ChatColor.RED + "This server type is already active.");
            return;
        }
        ServerType.setActiveServerType(serverType);
        ServerType.save();
        s.sendMessage(ChatColor.GREEN + "Successfully set the server type to \"" + serverType.getTypeName() + "\".");
    }

}
