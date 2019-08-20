package club.coan.ikari.faction.flags;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum Flags {

    NO_DEATHBAN("No Deathban", "Players will not recieve a deathban in a faction claim."),
    HIDE_FACTION_INFO("Hide Faction Info", "This faction will be hidden from faction information commands."),
    EVENT("Event", "This defines a faction claim as an event."),
    NO_PEARL("No Pearl", "Players will not be able to pearl inside the claim."),
    THIRTY_SEC_PEARL("30s Pearl Cooldown", "Players will recieve a 30 second enderpearl cooldown if they are in the claim."),
    NO_PVP("No PvP", "Players will be unable to pvp inside the claim."),
    SYSTEM("System", "This faction cannot lose DTR, cannot be joined and cannot be modified");

    private String name, description;

}
