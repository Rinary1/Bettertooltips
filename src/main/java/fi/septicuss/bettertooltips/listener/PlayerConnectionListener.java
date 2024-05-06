package fi.septicuss.bettertooltips.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fi.septicuss.bettertooltips.utils.cache.player.LookingAtCache;
import fi.septicuss.bettertooltips.utils.cache.tooltip.TooltipCache;

public class PlayerConnectionListener implements Listener {

	@EventHandler
	public void on(PlayerQuitEvent event) {
		TooltipCache.remove(event.getPlayer());
		LookingAtCache.remove(event.getPlayer());
	}
	
	
}
