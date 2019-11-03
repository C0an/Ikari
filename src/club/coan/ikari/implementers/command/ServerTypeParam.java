package club.coan.ikari.implementers.command;

import club.coan.ikari.utils.ServerType;
import club.coan.rinku.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerTypeParam implements ParameterType<ServerType> {

    @Override
    public ServerType transform(CommandSender commandSender, String s) {
        ServerType serverType = ServerType.getServerType(s);
        if(serverType == null) commandSender.sendMessage(ChatColor.RED + "There is no such server type with the name \"" + s + "\".");

        return serverType;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (ServerType serverType : ServerType.getServerTypes()) {
            if (StringUtils.startsWithIgnoreCase(serverType.getTypeName(), source)) {
                completions.add(serverType.getTypeName());
            }
        }
        return completions;
    }
}
