package fi.septicuss.bettertooltips.tooltip.runnable;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolManager;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.object.preset.Preset;
import fi.septicuss.bettertooltips.object.preset.PresetManager;
import fi.septicuss.bettertooltips.object.preset.actions.ActionProperties.TooltipAction;
import fi.septicuss.bettertooltips.object.preset.condition.StatementHolder;
import fi.septicuss.bettertooltips.tooltip.TooltipManager;

public class TooltipRunnableManager {

	private TooltipManager tooltipManager;
	private ProtocolManager protocolManager;

	private Map<String, Preset> presets = new HashMap<>();
	private Map<String, StatementHolder> holders = new HashMap<>();
	
	private TooltipRunnable runnable;

	// MANAGER
	
	public TooltipRunnableManager(Tooltips plugin) {
		this.tooltipManager = plugin.getTooltipManager();
		this.protocolManager = plugin.getProtocolManager();
		loadNecessaryData(plugin.getPresetManager());
	}
	
	private void loadNecessaryData(PresetManager presetManager) {
		for (var entry : presetManager.getConditionalPresets().entrySet()) {
			String id = entry.getKey();
			Preset preset = entry.getValue();
			
			presets.put(id, preset);
			holders.put(id, preset.getStatementHolder());
		}
	}
	
	public void runActions(TooltipAction action, Player player) {
		if (runnable != null)
			runnable.runActions(action, player);
	}
	
	public void run(JavaPlugin plugin, int checkFrequency) {
		runnable = new TooltipRunnable(tooltipManager, protocolManager, presets, holders, checkFrequency);
		runnable.runTaskTimer(plugin, 0L, checkFrequency);
	}
	
	public void stop() {
		runnable.clearData();
		runnable.cancel();
		runnable = null;
	}
	
}
