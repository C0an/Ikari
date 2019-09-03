package club.coan.ikari.faction.events;

import club.coan.christian.bukkit.utils.BaseEvent;
import club.coan.ikari.faction.Faction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class FactionCreateEvent extends BaseEvent {

    private Player player;
    private Faction faction;

}
