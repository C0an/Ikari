package club.coan.ikari.database.types;

import club.coan.ikari.database.IkariDatabase;
import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.Callback;

import java.util.Set;
import java.util.UUID;

public class FlatfileDatabase extends IkariDatabase {

    @Override
    public Set<Faction> getFactionsInDatabase() {
        return null;
    }

    @Override
    public Faction getFaction(UUID uuid) {
        return null;
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
    public void startup() {

    }

    @Override
    public void shutdown() {

    }

}
