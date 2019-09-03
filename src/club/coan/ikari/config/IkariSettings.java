package club.coan.ikari.config;

import club.coan.ikari.Ikari;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;


public class IkariSettings {

    public IkariSettings() {
        FileConfiguration c = Ikari.getInstance().getConfig();
        mongoHost = c.getString("mongo.host", "127.0.0.1");
        mongoPort = c.getInt("mongo.port", 27017);
        mongoDatabase = c.getString("mongo.database", "Ikari");
        mongoAuth = c.getBoolean("mongo.auth.enabled", false);
        mongoUser = c.getString("mongo.auth.username", "user");
        mongoPassword = c.getString("mongo.auth.password", "password");
        mongoAuthDatabase = c.getString("mongo.auth.auth", "admin");

        c = Ikari.getInstance().getSettingsFile();
        serverName = c.getString("settings.server_name");
        map = c.getString("settings.map");
        serverCurrency = c.getString("settings.serverCurrency");

        factionNameMin = c.getInt("factions.name.min");
        factionNameMax = c.getInt("factions.name.max");
        blockifcontain = c.getBoolean("factions.name.blocked.blockifcontain");
        blockNameList = c.getStringList("factions.name.blocked.list");
        factionSize = c.getInt("factions.size");
        maxdtr = c.getDouble("factions.maxdtr");

    }

    @Getter public static String mongoHost, mongoDatabase, mongoUser, mongoAuthDatabase, mongoPassword;
    @Getter public static int mongoPort;
    @Getter public static boolean mongoAuth;

    @Getter public static String serverName, map, serverCurrency;

    @Getter public static int factionNameMin, factionNameMax, factionSize;
    @Getter public static boolean blockifcontain;
    @Getter public static List<String> blockNameList;
    @Getter public static double maxdtr;

}
