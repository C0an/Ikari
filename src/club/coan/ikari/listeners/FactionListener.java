package club.coan.ikari.listeners;

import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.enums.Relation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();
            Faction pf = Faction.getFaction(p.getUniqueId());
            Faction df = Faction.getFaction(p.getUniqueId());
            if(pf == null || df == null) return;
            if(pf.getRelationTo(d.getUniqueId()) == Relation.ALLY || pf.getRelationTo(d.getUniqueId()) == Relation.TEAM) {
                e.setCancelled(true);
                d.sendMessage(ChatColor.YELLOW + "You cannot attack " + df.getRelationDisplay(d.getUniqueId(), pf, false) + p.getName() + ChatColor.YELLOW + "!");
            }
        }
    }


}
