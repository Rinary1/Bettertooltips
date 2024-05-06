package fi.septicuss.bettertooltips.object.preset.actions.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.DelayCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.MessageCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.SoundCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars.ClearVarCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars.ClearVarsCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars.SavePersistentVarsCommand;
import fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars.SetVarCommand;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Argument;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;

public class ActionCommands {

	private static final Map<String, ActionCommand> COMMANDS;

	static {
		COMMANDS = new HashMap<>();
		COMMANDS.put("sound", new SoundCommand());
		COMMANDS.put("message", new MessageCommand());
		COMMANDS.put("delay", new DelayCommand());
		
		// Variables
		
		//  Persistent
		COMMANDS.put("setpersistentvar", new SetVarCommand(true));
		COMMANDS.put("clearpersistentvar", new ClearVarCommand(true));
		COMMANDS.put("clearpersistentvars", new ClearVarsCommand(true));
		COMMANDS.put("savepersistentvars", new SavePersistentVarsCommand());
		
		//  Non-persistent
		COMMANDS.put("setvar", new SetVarCommand(false));
		COMMANDS.put("clearvar", new ClearVarCommand(false));
		COMMANDS.put("clearvars", new ClearVarsCommand(false));
	}

	public static boolean isValidCommand(String fullCommand) {
		String first = getFirstArgument(fullCommand);

		for (var commandName : COMMANDS.keySet())
			if (first.equalsIgnoreCase(commandName))
				return true;

		return false;
	}
	
	public static Set<Map.Entry<String, ActionCommand>> getEntries() {
		return Collections.unmodifiableSet(COMMANDS.entrySet());
	}

	public static Validity runCommand(Player player, String presetId, String fullCommand) {
		
		if (player != null) {
			fullCommand = fullCommand.replace("%player%", player.getName());
		}
		
		String[] split = fullCommand.split(" ");
		String first = split[0].toLowerCase();

		ActionCommand command = COMMANDS.get(first);

		if (command == null)
			return Validity.of(false, "Command not found");

		Arguments arguments = new Arguments();

		for (int i = 1; i < split.length; i++) {
			arguments.add(String.valueOf(i), new Argument(split[i]));
		}

		Validity validity = command.validity(arguments);

		if (validity == null || !command.validity(arguments).isValid()) {
			Tooltips.warn(
					"Failed to run action command " + Utils.quote(first) + " in preset " + Utils.quote(presetId) + ":");
			if (validity.hasReason())
				Tooltips.warn("  -> " + validity.getReason());
			return validity;
		}

		command.run(player, arguments);
		return Validity.TRUE;
	}

	private static String getFirstArgument(String fullCommand) {
		String[] split = fullCommand.split(" ");
		return split[0];
	}

}
