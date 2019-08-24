package club.coan.ikari.database.types;

import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.Callback;

import java.util.Set;
import java.util.UUID;

public class FlatfileDatabase extends IkariDatabase {

    @Override
    public void getFactionsInDatabase(Callback<Set<UUID>> callback) {

    }

    @Override
    public void loadFaction(UUID uuid, Callback<Faction> callback, boolean async) {

    }

    @Override
    public void saveFaction(Faction faction, Callback<Boolean> callback, boolean async) {

    }

    @Override
    public void deleteFaction(Faction faction, Callback<Boolean> callback, boolean async) {

    }

    @Override
    public void startup(Callback<Boolean> callback) {

    }


    @Override
    public void shutdown() {

    }

}
