package fi.septicuss.bettertooltips.commands.subcommands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.commands.TooltipsSubCommand;
import fi.septicuss.bettertooltips.utils.Colors;
import fi.septicuss.bettertooltips.utils.Messaging;

public class EvalCommand implements TooltipsSubCommand {

	private Tooltips plugin;

	public EvalCommand(Tooltips plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Messaging.send(sender, Colors.WARN + "[!] Must be a player to use this command.");
			return;
		}
		
		if (args.length < 2) {
			Messaging.send(sender, Colors.WARN + "[!] Missing condition");
			return;
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 1; i < args.length; i++) {
			builder.append(args[i] + " ");
		}

		String conditionStr = builder.toString();
		var parser = plugin.getConditionManager().getStatementParser();
		var statement = parser.parse("eval", conditionStr);

		if (statement == null) {
			Messaging.send(sender, Colors.WARN + "[!] An error occured while trying to parse condition");
			return;
		}

		Player player = (Player) sender;

		boolean result = statement.getCondition().check(player);

		Messaging.send(sender,
				"Condition result: " + (result ? Colors.PLUGIN : Colors.WARN) + result);

		if (statement.hasOutcome())
			Messaging.send(sender,
					" Outcome: " + (statement.getOutcome().asBoolean() ? Colors.PLUGIN : Colors.WARN)
							+ statement.getOutcome().toString());

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] relativeArgs) {
		return Lists.newArrayList();
	}

	@Override
	public String getPermission() {
		return "tooltips.command.eval";
	}

}
