package club.coan.ikari.database.types;

import club.coan.ikari.Ikari;
import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.config.Config;
import club.coan.rinku.other.Callback;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlatfileDatabase extends IkariDatabase {
    

    @Override
    public void getFactionsInDatabase(Callback<Set<UUID>> callback) {
        Set<UUID> factions = new HashSet<>();
        if(!Ikari.getInstance().getFactionFile().isConfigurationSection("factions") || Ikari.getInstance().getFactionFile().getConfigurationSection("factions").getKeys(false).isEmpty()) {
            callback.call(null);
            return;
        }
        Ikari.getInstance().getFactionFile().getConfigurationSection("factions").getKeys(false).forEach(str -> factions.add(UUID.fromString(str)));
        callback.call(factions);
    }

    @Override
    public void loadFaction(UUID uuid, Callback<Faction> callback, boolean async) {
        if(async) {
            new Thread(() -> loadFaction(uuid, callback, false)).start();
            return;
        }
        if(!Ikari.getInstance().getFactionFile().getConfigurationSection("factions").getKeys(false).contains(uuid.toString())) {
            callback.call(null);
            return;
        }
        String name = Ikari.getInstance().getFactionFile().getString("factions." + uuid.toString() + ".name");
        UUID leader = (Ikari.getInstance().getFactionFile().getString("factions." + uuid.toString() + ".leader") == null ? null : UUID.fromString(Ikari.getInstance().getFactionFile().getString("factions." + uuid + ".leader")));

        Faction faction = new Faction(uuid, name, leader);
        faction.setColor(ChatColor.valueOf(Ikari.getInstance().getFactionFile().getString("factions." + uuid.toString() + ".color")));
        faction.setDeathban(Boolean.parseBoolean(Ikari.getInstance().getFactionFile().getString("factions." + uuid.toString() + ".deathban")));
        faction.setPvp(Boolean.parseBoolean(Ikari.getInstance().getFactionFile().getString("factions." + uuid.toString() + ".pvp")));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".flags").forEach(str-> faction.getFlags().add(Flags.valueOf(str)));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".members").forEach(id-> faction.getMembers().add(UUID.fromString(id)));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".captains").forEach(id-> faction.getCaptains().add(UUID.fromString(id)));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".coleaders").forEach(id-> faction.getColeaders().add(UUID.fromString(id)));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".invites").forEach(id-> faction.getInvites().add(UUID.fromString(id)));

        Ikari.getInstance().getFactionFile().getStringList("factions." + uuid.toString() + ".allies").forEach(id-> faction.getAllies().add(UUID.fromString(id)));

        faction.setDtr(Ikari.getInstance().getFactionFile().getDouble("factions." + uuid.toString() + ".dtr"));
        faction.setDtrRegen(Ikari.getInstance().getFactionFile().getLong("factions." + uuid.toString() + ".dtrRegen"));
        faction.setBalance(Ikari.getInstance().getFactionFile().getInt("factions." + uuid.toString() + ".balance"));
        faction.setKothCaptures(Ikari.getInstance().getFactionFile().getInt("factions." + uuid.toString() + ".kothCaptures"));
        faction.setLives(Ikari.getInstance().getFactionFile().getInt("factions." + uuid.toString() + ".lives"));

        callback.call(faction);
    }

    @Override
    public void saveFaction(Faction faction, Callback<Boolean> callback, boolean async) {
        saveFaction(Ikari.getInstance().getFactionFile(), faction, callback, async);
    }

    public static void saveFaction(Config config, Faction faction, Callback<Boolean> callback, boolean async) {
        if(async) {
            new Thread(() -> saveFaction(config, faction, callback, false)).start();
            return;
        }

        String key = "factions." + faction.getUuid().toString() + ".";
        config.set(key + "name", faction.getName());
        config.set(key + "leader", (faction.getLeader() == null ? null : faction.getLeader().toString()));
        config.set(key + "color", faction.getColor().name());
        config.set(key + "deathban", faction.isDeathban());
        config.set(key + "pvp", faction.isPvp());
        config.set(key + "claim", null);
        config.set(key + "hq", null);
        config.set(key + "flags", faction.getFlags().stream().map(Flags::name).collect(Collectors.toList()));
        config.set(key + "coleaders", faction.getColeaders().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(key + "captains", faction.getCaptains().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(key + "members", faction.getMembers().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(key + "allies", faction.getAllies().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(key + "invites", faction.getInvites().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(key + "dtr", faction.getDtr());
        config.set(key + "dtrRegen", faction.getDtrRegen());
        config.set(key + "balance", faction.getBalance());
        config.set(key + "kothCaptures", faction.getKothCaptures());
        config.set(key + "lives", faction.getLives());
        config.save();
        callback.call(true);
    }

    @Override
    public void deleteFaction(Faction faction, Callback<Boolean> callback, boolean async) {
        if(async) {
            new Thread(() -> deleteFaction(faction, callback, false)).start();
            return;
        }
        if(faction == null) {
            callback.call(false);
            return;
        }
        Ikari.getInstance().getFactionFile().set("factions." + faction.getUuid().toString(), null);
        Ikari.getInstance().getFactionFile().save();
        Faction.getFactions().remove(faction);
        callback.call(true);
    }

    @Override
    public void startup(Callback<Boolean> callback) {
        callback.call(true);
    }


    @Override
    public void shutdown() {

    }


    @Override
    public void backup(String fileName, Callback<Boolean> callback) {
        File folder = Ikari.getInstance().getDataFolder();

        try {
            File file = new File(folder, fileName + ".yml");
            if (file.exists()) {
                callback.call(false);
                return;
            }
        }catch (Exception e) {
            callback.call(false);
            e.printStackTrace();
            return;
        }

        Config config = new Config(Ikari.getInstance(), fileName);
        Faction.getFactions().stream().filter(faction -> !faction.getFlags().contains(Flags.NO_SAVE)).forEach(faction -> saveFaction(config, faction, callback::call, false));
        callback.call(true);
    }

}
