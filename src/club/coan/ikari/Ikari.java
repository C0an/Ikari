package club.coan.ikari;

import club.coan.ikari.commands.faction.FactionCommands;
import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.database.types.FlatfileDatabase;
import club.coan.ikari.database.types.MongoDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.claim.Claim;
import club.coan.ikari.faction.flags.Flags;
import club.coan.ikari.utils.Config;
import club.coan.ikari.utils.command.CommandFramework;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Ikari extends JavaPlugin {

    @Getter private static Ikari instance;
    @Getter private static IkariDatabase ikariDatabase;
    @Getter private CommandFramework commandFramework;
    @Getter private Config langFile, settingsFile, factionFile;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        langFile = new Config("lang");
        settingsFile = new Config("settings");
        new IkariSettings();
        boolean flatFile = !getConfig().getString("database").equalsIgnoreCase("mongo");
        ikariDatabase = (flatFile ? new FlatfileDatabase() : new MongoDatabase());
        if(flatFile) factionFile = new Config("factions");
        ikariDatabase.startup(callback -> {
            if(!callback) {
                Bukkit.getPluginManager().disablePlugin(this);
            }
        });
        createSystemFactions();
        commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(new FactionCommands());
        commandFramework.registerHelp();


    }

    @Override
    public void onDisable() {
        ikariDatabase.shutdown();
        instance = null;
    }

    private void createSystemFactions() {
        Faction f = new Faction(UUID.randomUUID(), "Wilderness", null);
        f.setColor(ChatColor.GRAY);
        f.setFlags(new ArrayList<>(Collections.singleton(Flags.SYSTEM)));

        f = new Faction(UUID.randomUUID(), "Warzone", null);
        f.setColor(ChatColor.DARK_RED);
        f.setFlags(new ArrayList<>(Collections.singleton(Flags.SYSTEM)));
        f.setClaim(new Claim(new Location(Bukkit.getWorlds().get(0), -350, 0, 350), new Location(Bukkit.getWorlds().get(0), 350, 256, -350)));
    }
}
