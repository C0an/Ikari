package club.coan.ikari;

import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.database.types.FlatfileDatabase;
import club.coan.ikari.database.types.MongoDatabase;
import club.coan.ikari.utils.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Ikari extends JavaPlugin {

    @Getter private static Ikari instance;
    @Getter private static IkariDatabase ikariDatabase;
    @Getter private Config langFile, settingsFile, factionFile;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        langFile = new Config("lang");
        settingsFile = new Config("settings");
        boolean flatFile = !getConfig().getString("database").equalsIgnoreCase("mongo");
        ikariDatabase = (flatFile ? new FlatfileDatabase() : new MongoDatabase());
        if(flatFile) factionFile = new Config("factions");
        ikariDatabase.startup();
    }

    @Override
    public void onDisable() {
        ikariDatabase.shutdown();
        instance = null;
    }
}
