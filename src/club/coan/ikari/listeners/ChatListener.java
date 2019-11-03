package club.coan.ikari.listeners;

import club.coan.christian.bukkit.BukkitAPI;
import club.coan.ikari.faction.Faction;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class ChatListener implements Listener {

    private List<String> blockedWords = Arrays.asList("nigga", "nigger", "faggot", "kys", "killyourself", "kiss your sister", "kill yourself", "kill urself");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled() || e.getRecipients().size() == 0) return;
        Player p = e.getPlayer();
        String msg = e.getMessage();
        Faction f = Faction.getFactionOf(p);
        e.setCancelled(true);
        boolean blocked = false;
        for (String s : blockedWords) {
            if (msg.toLowerCase().contains(s)) {
                blocked = true;
                break;
            }
        }
        for (Player r : e.getRecipients()) {
            String chatFormat = (f == null ? "" : ChatColor.GOLD + "[" + f.getRelationDisplay(r.getUniqueId(), f, true) + ChatColor.GOLD + "]") + BukkitAPI.getPrefix(p) + BukkitAPI.getColor(p) + p.getName() + BukkitAPI.getSuffix(p) + "§r: " + e.getMessage();
            if(blocked) {
                if(r == p) {
                    r.sendMessage(chatFormat);
                } else {
                    if(r.hasPermission("ikari.chat.viewblocked")) {
                       r.sendMessage(ChatColor.RED + "[BLOCKED] " + chatFormat);
                    }
                }
            } else {
                r.sendMessage(chatFormat);
            }
        }
    }

}
