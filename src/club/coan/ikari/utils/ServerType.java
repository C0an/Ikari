package club.coan.ikari.utils;

import club.coan.ikari.Ikari;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class ServerType {

    @Getter public static Set<ServerType> serverTypes = new HashSet<>();
    @Getter @Setter private static ServerType activeServerType = null;

    private String typeName;
    private boolean deathban, naturalRegen, loseDTR, instantRespawn, playerDataOnSB;
    private int startingBalance;

    public ServerType(String typeName, boolean deathban, boolean naturalRegen, boolean loseDTR, boolean instantRespawn, boolean playerDataOnSB, int startingBalance) {
        this.typeName = typeName;
        this.deathban = deathban;
        this.naturalRegen = naturalRegen;
        this.loseDTR = loseDTR;
        this.instantRespawn = instantRespawn;
        this.playerDataOnSB = playerDataOnSB;
        this.startingBalance = startingBalance;
        serverTypes.add(this);
    }

    public ServerType() {
        FileConfiguration c = Ikari.getInstance().getSettingsFile();
        for(String s : c.getConfigurationSection("servertypes").getKeys(false)) {
            String key = "servertypes." + s + ".";
            ServerType sType = new ServerType(s, c.getBoolean(key + "deathban"), c.getBoolean(key + "naturalRegen"), c.getBoolean(key + "loseDTR"), c.getBoolean(key + "instantRespawn"), c.getBoolean(key + "playerDataOnSB"), c.getInt(key + "startingBalance"));
            if(c.getString("settings.servertype").equalsIgnoreCase(s)) {
                activeServerType = sType;
            }
        }
    }

    public static void save() {
        FileConfiguration c = Ikari.getInstance().getSettingsFile();
        c.set("settings.servertype", getActiveServerType().getTypeName());
        Ikari.getInstance().getSettingsFile().save();
    }

    public static ServerType getServerType(String name) {
        return getServerTypes().stream().filter(sType -> sType.getTypeName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
