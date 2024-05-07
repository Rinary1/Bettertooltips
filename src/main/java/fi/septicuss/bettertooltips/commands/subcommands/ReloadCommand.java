package fi.septicuss.bettertooltips.commands.subcommands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.commands.TooltipsSubCommand;
import fi.septicuss.bettertooltips.utils.LanguageLoader;
import fi.septicuss.bettertooltips.utils.Colors;
import fi.septicuss.bettertooltips.utils.Messaging;

public class ReloadCommand implements TooltipsSubCommand {

	private Tooltips plugin;

	public ReloadCommand(Tooltips plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		Messaging.send(sender, Colors.PLUGIN + plugin.getConfig().getString("reloading_message", "Reloading plugin..."));
		plugin.reload();
		Messaging.send(sender, Colors.PLUGIN + plugin.getConfig().getString("reloaded_message", "Reloaded!"));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] relativeArgs) {
		return null;
	}

	@Override
	public String getPermission() {
		return "tooltips.command.reload";
	}

}
