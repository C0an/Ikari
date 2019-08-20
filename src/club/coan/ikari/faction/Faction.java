package club.coan.ikari.faction;

import club.coan.ikari.faction.claim.Claim;
import club.coan.ikari.faction.enums.Relation;
import club.coan.ikari.faction.flags.Flags;
import lombok.Data;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
    private int dtrRegen = 0, balance = 0, kothCaptures = 0, lives = 0;

    public Faction(UUID uuid, String name, UUID leader) {
        this.uuid = uuid;
        this.name = name;
        this.leader = leader;
        this.deathban = true;
        this.pvp = true;
        this.color = ChatColor.GRAY;
        factions.add(this);
    }

    public boolean isSystem() {
        return flags.contains(Flags.SYSTEM);
    }

    public Set<UUID> getAllMembers() {
        Set<UUID> facMembers = new HashSet<>();
        facMembers.add(leader);
        facMembers.addAll(coleaders);
        facMembers.addAll(captains);
        facMembers.addAll(members);
        return facMembers;
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
        return factions.stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /*/
        Credit to D0an
     */
    public static List<Faction> getFactions(String f) {
        List<Faction> facs = new ArrayList<>();
        factions.forEach(fac ->
        {
            for(UUID uid : fac.getAllMembers()){
                OfflinePlayer p = Bukkit.getOfflinePlayer(uid);
                if(p.getName() == null) continue;
                if(p.getName().equalsIgnoreCase(f)){
                    facs.add(fac);
                }
            }
        });
        Faction named = getFaction(f);
        if(named != null && !facs.contains(named)) facs.add(named);
        return facs;
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

    public String getRelationDisplay(UUID player, Faction faction) {
        if (player == null) return "§e" + faction.getName();
        switch (faction.getRelationTo(player)) {
            case TEAM: {
                return "§2" + faction.getName();
            }
            case ENEMY: {
                return "§e" + faction.getName();
            }
            case ALLY: {
                return "§9" + faction.getName();
            }
            default: {
                return faction.getColor() + faction.getName();
            }
        }

    }

    public void sendInfo(Player p) {
        p.sendMessage("§7§m-----------------------------------------");
        p.sendMessage(getRelationDisplay(p.getUniqueId(), this));
        p.sendMessage("§7§m-----------------------------------------");
        p.sendMessage("§7§m-----------------------------------------");
    }
}
