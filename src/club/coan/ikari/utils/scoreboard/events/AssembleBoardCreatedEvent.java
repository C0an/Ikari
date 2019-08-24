package club.coan.ikari.utils.scoreboard.events;

import club.coan.ikari.utils.scoreboard.AssembleBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class AssembleBoardCreatedEvent extends Event {

    @Getter public static HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;
    private final AssembleBoard board;
    private Player player;

    public AssembleBoardCreatedEvent(AssembleBoard board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
