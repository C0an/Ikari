package club.coan.ikari.faction.enums;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Relation {

    ENEMY("Enemy", ChatColor.RED),
    TEAM("Team", ChatColor.GREEN),
    ALLY("Ally", ChatColor.AQUA),
    CUSTOM("Custom", ChatColor.GOLD);

    @Getter
    private String displayName;
    @Getter private ChatColor chatColor;

    Relation(String name, ChatColor color) {
        this.displayName = name;
        this.chatColor = color;
    }

    public ChatColor toChatColour() {
        return this.getChatColor();
    }

}
