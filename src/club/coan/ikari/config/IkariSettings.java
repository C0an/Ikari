package club.coan.ikari.config;

import club.coan.ikari.Ikari;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;


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
    }

    @Getter public static String mongoHost, mongoDatabase, mongoUser, mongoAuthDatabase, mongoPassword;
    @Getter public static int mongoPort;
    @Getter public static boolean mongoAuth;

}
