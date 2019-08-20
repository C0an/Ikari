package club.coan.ikari.utils;

import club.coan.ikari.Ikari;
import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum Locale {

    NOPERMISSION("command.nopermission"),
    INGAMEONLY("command.ingameonly"),
    INCONSOLEONLY("command.inconsoleonly"),
    USAGEMESSAGE("command.usage.message"),
    USAGEDESCRIPTION("command.usage.description"),
    INCORRECTSETUP("command.incorrectsetup");

    private String path;

    Locale(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', Ikari.getInstance().getLangFile().getString(path));
    }
}
