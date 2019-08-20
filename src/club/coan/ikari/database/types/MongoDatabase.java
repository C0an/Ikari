package club.coan.ikari.database.types;

import club.coan.ikari.config.IkariSettings;
import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.Callback;
import com.mongodb.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MongoDatabase extends IkariDatabase {

    @Getter private MongoClient mongoClient;
    @Getter private DB database;
    @Getter private DBCollection mapCollection;

    @Override
    public void getFactionsInDatabase(Callback<Set<UUID>> callback) {

    }

    @Override
    public void loadFaction(Callback<Faction> callback) {

    }

    @Override
    public void saveFaction(Callback<Boolean> callback) {

    }

    @Override
    public void deleteFaction(Callback<Boolean> callback) {

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
            mapCollection = database.getCollection("maps");
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
}
