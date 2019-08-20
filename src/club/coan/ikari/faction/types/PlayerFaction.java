package club.coan.ikari.faction.types;

import club.coan.ikari.faction.Faction;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class PlayerFaction extends Faction {

    private Set<UUID> coleaders = new HashSet<>(), captains = new HashSet<>(), members = new HashSet<>(), allies = new HashSet<>(), invites = new HashSet<>();
    private double dtr = 1.01;
    private int dtrRegen = 0, balance = 0, kothCaptures = 0, lives = 0;

    public PlayerFaction(UUID uuid, String name, UUID leader) {
        super(uuid, name, leader);
    }

}
