package club.coan.ikari.database;

import club.coan.ikari.faction.Faction;
import club.coan.ikari.utils.Callback;

import java.util.Set;
import java.util.UUID;

public abstract class IkariDatabase {

    public abstract void getFactionsInDatabase(Callback<Set<UUID>> callback);
    public abstract void loadFaction(UUID uuid, Callback<Faction> callback, boolean async);
    public abstract void saveFaction(Faction faction, Callback<Boolean> callback, boolean async);
    public abstract void deleteFaction(Faction faction, Callback<Boolean> callback, boolean async);
    public abstract void startup(Callback<Boolean> callback);
    public abstract void shutdown();


}
