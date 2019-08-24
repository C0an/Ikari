package club.coan.ikari.faction.flags;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum Flags {

    NO_DEATHBAN("No Deathban", "Players will not recieve a deathban in a faction claim.", true),
    HIDE_FACTION_INFO("Hide Faction Info", "This faction will be hidden from faction information commands.", true),
    EVENT("Event", "This defines a faction claim as an event.", true),
    NO_PEARL("No Pearl", "Players will not be able to pearl inside the claim.", true),
    THIRTY_SEC_PEARL("30s Pearl Cooldown", "Players will recieve a 30 second enderpearl cooldown if they are in the claim.", true),
    NO_PVP("No PvP", "Players will be unable to pvp inside the claim.", true),
    SYSTEM("System", "This faction cannot lose DTR, cannot be joined and cannot be modified.", false),
    NO_SAVE("No Save", "This faction will not save to the database.", false),
    OVERRIDE_NAME("Override Name", "This allows you to create a faction with the same name.", false);

    private String name, description;
    private boolean canApply;

}
