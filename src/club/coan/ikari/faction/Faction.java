package club.coan.ikari.faction;

import club.coan.ikari.Ikari;
import club.coan.ikari.faction.claim.Claim;
import club.coan.ikari.faction.enums.Relation;
import club.coan.ikari.faction.enums.Role;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.other.Callback;
import lombok.Data;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
public class Faction {

    @Getter private static ArrayList<Faction> factions = new ArrayList<>();

    private UUID uuid, leader;
    private String name;
    private ChatColor color;
    private boolean deathban, pvp;
    private Claim claim = null;
    private Location hq = null;
    private ArrayList<Flags> flags = new ArrayList<>();
    private Set<UUID> coleaders = new HashSet<>(), captains = new HashSet<>(), members = new HashSet<>(), allies = new HashSet<>(), invites = new HashSet<>();
    private double dtr = 1.01;
    private long dtrRegen = 0L;
    private int balance = 0, kothCaptures = 0, lives = 0;

    public Faction(UUID uuid, String name, UUID leader) {
        this.uuid = uuid;
        this.name = name;
        this.leader = leader;
        this.deathban = true;
        this.pvp = true;
        this.color = ChatColor.GRAY;
        factions.add(this);
    }

    public static void loadFactions() {
        Ikari.getIkariDatabase().getFactionsInDatabase(callback-> {
            if(callback == null) {
                System.out.println("[Ikari] Failed to load any factions - maybe there is none made?");
                return;
            }
            callback.forEach(uuid -> {
                Ikari.getIkariDatabase().loadFaction(uuid, callback1 -> {
                    if(callback1 == null) {
                        System.out.println("[Ikari] Failed to load Faction: " + uuid.toString());
                    }
                }, false);
            });
        });
    }

    public static void saveAll(Callback<String> callback) {
        AtomicInteger failed = new AtomicInteger();
        AtomicInteger saved = new AtomicInteger();
        int total = (int) Faction.getFactions().stream().filter(faction -> !faction.getFlags().contains(Flags.NO_SAVE)).count();
        for (Faction faction : Faction.getFactions()) {
            if(faction.getFlags().contains(Flags.NO_SAVE)) continue;
             faction.save(callback1 -> {
                if(callback1 == null || !callback1) {
                    failed.getAndIncrement();
                }else {
                    saved.getAndIncrement();
                }
            });
        }
        while ((failed.get() + saved.get()) < total) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
        }
        callback.call(ChatColor.GREEN + "Saved " + saved.get() + " factions, failed to save " + failed.get() + " factions.");
    }

    public boolean isSystem() {
        return flags.contains(Flags.SYSTEM);
    }

    public void disband() {
        sendMessage("&c&lThe faction has been disbanded.");
        Ikari.getIkariDatabase().deleteFaction(this, callback -> {
            if(callback) {
                System.out.println("[Ikari] Deleted the team " + getName() + " from the database.");
            }else {
                System.out.println("[Ikari] Failed to delete team " + getName() + " from the database.");
            }
        }, true);
    }


    public Role getRole(UUID uuid) {
        if(members.contains(uuid)) return Role.MEMBER;
        if(captains.contains(uuid)) return Role.CAPTAIN;
        if(coleaders.contains(uuid)) return Role.COLEADER;
        if(leader.equals(uuid)) return Role.LEADER;
        return Role.MEMBER;
    }

    public List<UUID> getAllMembers() {
        List<UUID> facMembers = new ArrayList<>();
        facMembers.add(leader);
        facMembers.addAll(coleaders);
        facMembers.addAll(captains);
        facMembers.addAll(members);
        return facMembers;
    }

    public void save(Callback<Boolean> callback1) {
        Ikari.getIkariDatabase().saveFaction(this, callback1, true);
    }

    public List<UUID> getOnlineMembers() {
        return getAllMembers().stream().filter(u -> Bukkit.getOfflinePlayer(u).isOnline()).collect(Collectors.toList());
    }

    public static Faction getFactionOf(UUID uuid) {
        return factions.stream().filter(faction -> faction.getAllMembers().contains(uuid)).findFirst().orElse(null);
    }

    public static Faction getFactionOf(Player p) {
        return getFactionOf(p.getUniqueId());
    }

    public static Faction getFaction(UUID uuid) {
        return factions.stream().filter(faction -> faction.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public static Faction getFaction(String name) {
        return factions.stream().filter(faction -> faction.getName().equalsIgnoreCase(name) && !faction.getFlags().contains(Flags.OVERRIDE_NAME)).findFirst().orElse(null);
    }

    public void sendMessage(String msg) {
        getOnlineMembers().forEach(on -> Bukkit.getPlayer(on).sendMessage(ChatColor.translateAlternateColorCodes('&', msg)));
    }

    /*/
        Credit to D0an
     */
    public static List<Faction> getFactions(String f, boolean hideUnwhoable) {
        List<Faction> facs = new ArrayList<>();
        factions.forEach(fac -> {
            for(UUID uid : fac.getAllMembers()){
                if(uid == null) continue;
                OfflinePlayer p = Bukkit.getOfflinePlayer(uid);
                if(p == null || p.getName() == null) continue;
                if(p.getName().equalsIgnoreCase(f)){
                    facs.add(fac);
                }
            }
        });
        Faction named = getFaction(f);
        if(named != null && !facs.contains(named) && (hideUnwhoable && !named.getFlags().contains(Flags.HIDE_FACTION_INFO))) facs.add(named);
        return facs;
    }

    public static List<Faction> getFactions(String f) {
        return getFactions(f, false);
    }

    public static Faction getFaction(Location loc, boolean player) {
        Faction in = null;
        for(Faction f : factions) {
            Claim c = f.getClaim();
            if(c == null) continue;
            if(c.isInside(loc, player)) {
                if(in == null) {
                    in = f;
                    continue;
                }
                if(Math.abs(in.getClaim().getX1()) > Math.abs(c.getX1()) || Math.abs(in.getClaim().getX2()) > Math.abs(c.getX2())){
                    in = f;
                }
            }
        }
        if(in != null) return in;
        return getFaction("Wilderness");
    }

    public void addClaim(Location p1, Location p2) {
        Claim c = new Claim(p1, p2);
        setClaim(c);
    }

    public void addClaim(Claim c) {
        setClaim(c);
    }

    public Relation getRelationTo(UUID player) {
        Faction pf = Faction.getFactionOf(player);
        if(pf != null) {

            if (this == pf) return Relation.TEAM;
            if (pf.getAllies().contains(getUuid())) return Relation.ALLY;

        }
        if (isSystem()) return Relation.CUSTOM;
        return Relation.ENEMY;
    }

    public String getOnlineName(UUID uuid) {
        if(getOnlineMembers().contains(uuid)) {
            return "§a" + Bukkit.getOfflinePlayer(uuid).getName();
        }else {
            return "§7" + Bukkit.getOfflinePlayer(uuid).getName();
        }
    }

    public String getRelationDisplay(UUID player, Faction faction, boolean factionName) {
        if (player == null) return "§e" + faction.getName();
        switch (faction.getRelationTo(player)) {
            case TEAM: {
                return "§2" + (factionName ? faction.getName() : "");
            }
            case ENEMY: {
                return "§e" + (factionName ? faction.getName() : "");
            }
            case ALLY: {
                return "§9" + (factionName ? faction.getName() : "");
            }
            default: {
                return faction.getColor()  + (factionName ? faction.getName() : "");
            }
        }
    }



    public String getOnlineCaptainFormatted() {
        HashSet<String> format = new HashSet<>();
        for(UUID u : getCaptains()) {
            format.add(getOnlineName(u) + ChatColor.YELLOW + "[" + ChatColor.GREEN + "0" + ChatColor.YELLOW + "]");
        }
        return StringUtils.join(format, ChatColor.GRAY + ", ");
    }

    public String getOnlineColeadersFormatted() {
        HashSet<String> format = new HashSet<>();
        for(UUID u : getColeaders()) {
            format.add(getOnlineName(u) + ChatColor.YELLOW + "[" + ChatColor.GREEN + "0" + ChatColor.YELLOW + "]");
        }
        return StringUtils.join(format, ChatColor.GRAY + ", ");
    }

    public String getOnlineMembersFormatted() {
        HashSet<String> format = new HashSet<>();
        for(UUID u : getOnlineMembers()) {
            format.add(getOnlineName(u) + ChatColor.YELLOW + "[" + ChatColor.GREEN + "0" + ChatColor.YELLOW + "]");
        }
        return StringUtils.join(format, ChatColor.GRAY + ", ");
    }

    public void sendInfo(CommandSender p) {
        p.sendMessage("§7§m-----------------------------------------");
        if(isSystem()) {
            p.sendMessage(getColor() + getName());
            p.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.WHITE + (getClaim() == null ? "None" : getClaim().getCenter().getBlockX() + ", " + getClaim().getCenter().getBlockZ()));
        }else {
            p.sendMessage(ChatColor.BLUE + getName() + ' ' + ChatColor.GRAY + "[" + getOnlineMembers().size() + "/" + getAllMembers().size() + "] " + ChatColor.DARK_AQUA + "-" + ChatColor.YELLOW + " HQ: " + ChatColor.WHITE + (getHq() == null ? "None" : getHq().getBlockX() + ", " + getHq().getBlockZ()));
            p.sendMessage(ChatColor.YELLOW + "Leader: " + getOnlineName(leader) + ChatColor.YELLOW + "[" + ChatColor.GREEN + "0" + ChatColor.YELLOW + "]");
            if(!coleaders.isEmpty()) p.sendMessage(ChatColor.YELLOW + "Co-Leaders: " + getOnlineColeadersFormatted());
            if(!captains.isEmpty()) p.sendMessage(ChatColor.YELLOW + "Captains: " + getOnlineCaptainFormatted());
            if(!members.isEmpty()) p.sendMessage(ChatColor.YELLOW + "Members: " + getOnlineMembersFormatted());
            p.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.BLUE + "$" + getBalance());
            p.sendMessage(ChatColor.YELLOW + "Deaths until Raidable: " + ChatColor.GREEN + getDtr());
            if(dtrRegen != 0) p.sendMessage(ChatColor.YELLOW + "Time Until Regen: " + ChatColor.GREEN + getDtrRegen());
        }
        p.sendMessage("§7§m-----------------------------------------");

    }
}
