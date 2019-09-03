package club.coan.ikari.listeners;

import club.coan.ikari.utils.ServerType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoreListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.GREEN + "The active server type is: " + ServerType.getActiveServerType().getTypeName());
    }

}
