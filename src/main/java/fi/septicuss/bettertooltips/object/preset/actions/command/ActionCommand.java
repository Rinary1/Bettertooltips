package fi.septicuss.bettertooltips.object.preset.actions.command;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;

public interface ActionCommand {

	public void run(Player player, Arguments arguments);
	
	public Validity validity(Arguments arguments);
	
	public default String getArgument(int index) {
		return String.valueOf(index);
	}
	
	public default String[] convertArgumentsToArray(Arguments arguments) {
		String[] args = new String[arguments.size()];
		for (int i = 1; i <= arguments.size(); i++) {
			args[i - 1] = arguments.get(String.valueOf(i)).getAsString();
		}
		return args;
	}

	public default String appendFrom(String[] args, int startIndex) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < args.length; i++)
			builder.append(args[i] + " ");
		return builder.toString().strip();
	}
}
