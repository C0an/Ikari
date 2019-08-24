package club.coan.ikari.utils.scoreboard;

import club.coan.ikari.Ikari;
import club.coan.ikari.utils.scoreboard.events.AssembleBoardCreateEvent;
import club.coan.ikari.utils.scoreboard.events.AssembleBoardDestroyEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class AssembleListener implements Listener {

	private Assemble assemble;

	public AssembleListener(Assemble assemble) {
		this.assemble = assemble;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(e.getPlayer());

		Bukkit.getPluginManager().callEvent(createEvent);
		if (createEvent.isCancelled()) {
			return;
		}
		Bukkit.getScheduler().runTask(assemble.getPlugin(), () -> getAssemble().getBoards().put(e.getPlayer().getUniqueId(), new AssembleBoard(e.getPlayer(), getAssemble())));

	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(destroyEvent);
		if (destroyEvent.isCancelled()) {
			return;
		}

		getAssemble().getBoards().remove(event.getPlayer().getUniqueId());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

}
