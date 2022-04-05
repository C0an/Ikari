package club.coan.ikari.database.types;

import club.coan.ikari.Ikari;
import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.faction.flags.Flags;
import club.coan.rinku.config.Config;
import club.coan.rinku.other.Callback;
import com.mongodb.*;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MongoDatabase extends IkariDatabase {

    @Getter private MongoClient mongoClient;
    @Getter private DB database;
//    @Getter private DBCollection mapCollection;
    @Getter private DBCollection factionCollection;

    @Override
    public void getFactionsInDatabase(Callback<Set<UUID>> callback) {
        Set<UUID> factions = new HashSet<>();
        DBCursor cursor = factionCollection.find();
        cursor.forEach(dbObject -> factions.add(UUID.fromString(dbObject.get("uuid").toString())));
        callback.call(factions);
    }

    @Override
    public void loadFaction(UUID uuid, Callback<Faction> callback, boolean async) {
        if(async) {
            new Thread(() -> loadFaction(uuid, callback, false)).start();
            return;
        }
        DBObject query = new BasicDBObject("uuid", uuid.toString());
        DBCursor cursor = factionCollection.find(query);
        DBObject storedFaction = cursor.one();
        if (storedFaction == null) {
            callback.call(null);
            return;
        }

        String name = storedFaction.get("name").toString();
        UUID leader = (storedFaction.get("leader") == null ? null : UUID.fromString(storedFaction.get("leader").toString()));

        Faction faction = new Faction(uuid, name, leader);
        faction.setColor(ChatColor.valueOf(storedFaction.get("color").toString()));
        faction.setDeathban(Boolean.parseBoolean(storedFaction.get("deathban").toString()));
        faction.setPvp(Boolean.parseBoolean(storedFaction.get("pvp").toString()));

        ((List<String>)storedFaction.get("flags")).forEach(str-> faction.getFlags().add(Flags.valueOf(str)));

        ((List<String>)storedFaction.get("members")).forEach(id-> faction.getMembers().add(UUID.fromString(id)));

        ((List<String>)storedFaction.get("captains")).forEach(id-> faction.getCaptains().add(UUID.fromString(id)));

        ((List<String>)storedFaction.get("coleaders")).forEach(id-> faction.getColeaders().add(UUID.fromString(id)));

        ((List<String>)storedFaction.get("invites")).forEach(id-> faction.getInvites().add(UUID.fromString(id)));

        ((List<String>)storedFaction.get("allies")).forEach(id-> faction.getAllies().add(UUID.fromString(id)));

        faction.setDtr(Double.parseDouble(storedFaction.get("dtr").toString()));
        faction.setDtrRegen(Integer.parseInt(storedFaction.get("dtrRegen").toString()));
        faction.setBalance(Integer.parseInt(storedFaction.get("balance").toString()));
        faction.setKothCaptures(Integer.parseInt(storedFaction.get("kothCaptures").toString()));
        faction.setLives(Integer.parseInt(storedFaction.get("lives").toString()));

        callback.call(faction);
    }

    @Override
    public void saveFaction(Faction faction, Callback<Boolean> callback, boolean async) {
        if(async) {
            new Thread(() -> saveFaction(faction, callback, false)).start();
            return;
        }

        DBObject object = new BasicDBObjectBuilder()
                .add("uuid", faction.getUuid().toString())
                .add("name", faction.getName())
                .add("leader", (faction.getLeader() == null ? null : faction.getLeader().toString()))
                .add("color", faction.getColor().name())
                .add("deathban", faction.isDeathban())
                .add("pvp", faction.isPvp())
                .add("claim", null)
                .add("hq", null)
                .add("flags", faction.getFlags().stream().map(Flags::name).collect(Collectors.toList()))
                .add("coleaders", faction.getColeaders().stream().map(UUID::toString).collect(Collectors.toList()))
                .add("captains", faction.getCaptains().stream().map(UUID::toString).collect(Collectors.toList()))
                .add("members", faction.getMembers().stream().map(UUID::toString).collect(Collectors.toList()))
                .add("allies", faction.getAllies().stream().map(UUID::toString).collect(Collectors.toList()))
                .add("invites", faction.getInvites().stream().map(UUID::toString).collect(Collectors.toList()))
                .add("dtr", faction.getDtr())
                .add("dtrRegen", faction.getDtrRegen())
                .add("balance", faction.getBalance())
                .add("kothCaptures", faction.getKothCaptures())
                .add("lives", faction.getLives())
                .get();

        factionCollection.update(new BasicDBObject("uuid", faction.getUuid().toString()), object, new DBCollectionUpdateOptions().upsert(true));
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
        factionCollection.remove(new BasicDBObject("uuid", faction.getUuid().toString()));
        Faction.getFactions().remove(faction);
        callback.call(true);
    }

    @Override
    public void startup(Callback<Boolean> callback) {
        if (!IkariSettings.isMongoAuth()) {
            String link = "mongodb://" + IkariSettings.getMongoHost() + ":" + IkariSettings.getMongoPort();
            mongoClient = new MongoClient(new MongoClientURI(link));
        } else {
            List<ServerAddress> seeds = new ArrayList<>();
            seeds.add(new ServerAddress(IkariSettings.getMongoHost()));
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(
                    MongoCredential.createScramSha1Credential(
                            IkariSettings.getMongoUser(),
                            IkariSettings.getMongoAuthDatabase(),
                            IkariSettings.getMongoPassword().toCharArray()
                    )
            );
            mongoClient = new MongoClient( seeds, credentials );
        }

        try {
            database = mongoClient.getDB(IkariSettings.getMongoDatabase());
//            mapCollection = database.getCollection("map-info-" + IkariSettings.getMap());
            factionCollection = database.getCollection("factions-map-" + IkariSettings.getMap());
            callback.call(true);
        } catch (Exception ex) {
            System.out.println("[Ikari] Error whilst initialising the database.");
            System.out.println(ex.getClass().getSimpleName() + " - " + ex.getMessage());
            callback.call(false);
        }
    }


    @Override
    public void shutdown() {
        mongoClient.close();
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
        Faction.getFactions().stream().filter(faction -> !faction.getFlags().contains(Flags.NO_SAVE)).forEach(faction -> FlatfileDatabase.saveFaction(config, faction, callback1 -> {}, false));
        callback.call(true);
    }


}
