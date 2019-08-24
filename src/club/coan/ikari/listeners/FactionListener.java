package club.coan.ikari.listeners;

import club.coan.ikari.faction.Faction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FactionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Faction f = Faction.getFactionOf(p);
        if(f != null) {
            f.sendInfo(p);
            f.sendMessage("&6Member Online: &2" + f.getRole(p.getUniqueId()).getAstrix() + p.getName());
        }else {
            p.sendMessage(ChatColor.GRAY + "You are not in a faction.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Faction f = Faction.getFactionOf(p);
        if(f != null) f.sendMessage("&6Member Offline: &c" + f.getRole(p.getUniqueId()) + p.getName());
    }


}
