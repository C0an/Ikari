package club.coan.ikari.listeners;

import club.coan.ikari.Ikari;
import club.coan.ikari.utils.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        ServerType serverType = ServerType.getActiveServerType();
        p.sendMessage(serverType.isDeathban() ? "You would be deathbanned due to server type." : "You wouldn't of been banned because of the server type.");
        if(serverType.isInstantRespawn()) Bukkit.getScheduler().runTaskLater(Ikari.getInstance(), () -> p.spigot().respawn(), 1);
    }


}
