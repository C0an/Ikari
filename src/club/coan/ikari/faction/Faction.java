package club.coan.ikari.faction;

import club.coan.ikari.faction.claim.Claim;
import club.coan.ikari.faction.flags.Flags;
import lombok.Data;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

@Data
public abstract class Faction {

    @Getter private static ArrayList<Faction> factions = new ArrayList<>();

    private UUID uuid, leader;
    private String name, displayName;
    private ChatColor color;
    private boolean deathban, pvp;
    private Claim claim;
    private Location hq;
    private ArrayList<Flags> flags = new ArrayList<>();

    public Faction(UUID uuid, String name, UUID leader) {
        this.uuid = uuid;
        this.name = name;
        this.leader = leader;
        this.deathban = true;
        this.pvp = true;
        this.color = ChatColor.GRAY;
        factions.add(this);
    }

}
