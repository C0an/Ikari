package club.coan.ikari.database;

import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.Callback;

import java.util.Set;
import java.util.UUID;

public abstract class IkariDatabase {

    public abstract Set<Faction> getFactionsInDatabase();
    public abstract Faction getFaction(UUID uuid);
    public abstract void loadFaction(Callback<Faction> callback);
    public abstract void saveFaction(Callback<Boolean> callback);
    public abstract void deleteFaction(Callback<Boolean> callback);
    public abstract void startup();
    public abstract void shutdown();


}
