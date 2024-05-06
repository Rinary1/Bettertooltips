package fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.actions.command.ActionCommand;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;
import fi.septicuss.bettertooltips.utils.placeholder.Placeholders;
import fi.septicuss.bettertooltips.utils.variable.Variables;
import fi.septicuss.bettertooltips.utils.variable.Variables.VariableProvider;

public class ClearVarCommand extends VarCommand implements ActionCommand {

	private boolean persistent = false;
	private String commandName = "clearvar";

	public ClearVarCommand(boolean persistent) {
		this.persistent = persistent;
		this.commandName = persistent ? "clearpersistentvar" : "clearvar";
	}

	@Override
	public void run(Player player, Arguments arguments) {
		String[] args = convertArgumentsToArray(arguments);

		// Determine scope & name
		final String scopeArgument = args[0];

		final boolean global = isGlobal(scopeArgument);
		Player target = getTarget(scopeArgument);

		if (target == null)
			target = player;

		final String variableName = Placeholders.replacePlaceholders(target, args[1]);

		// Clearing the variable
		VariableProvider provider = (persistent ? Variables.PERSISTENT : Variables.LOCAL);

		if (global) {
			provider.clearVar(variableName);
			return;
		}

		provider.clearVar(target, variableName);
	}

	@Override
	public Validity validity(Arguments arguments) {
		String[] args = convertArgumentsToArray(arguments);

		if (args.length < 2)
			return Validity.of(false, "Not enough arguments; " + commandName + " (player/global) name");

		String target = args[0];
		Player targetPlayer = Bukkit.getPlayerExact(target);
		
		if (!target.equalsIgnoreCase("global") && !target.equalsIgnoreCase("player") && targetPlayer == null)
			return Validity.of(false,
					"Scope must either be a players name, 'player' or 'global'. Now was " + Utils.quote(args[0]));
		
		return Validity.TRUE;
	}

}
