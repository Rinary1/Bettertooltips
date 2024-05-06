package fi.septicuss.bettertooltips.object.preset.actions.command.impl;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.actions.command.ActionCommand;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;
import fi.septicuss.bettertooltips.utils.placeholder.Placeholders;

public class MessageCommand implements ActionCommand {

	@Override
	public void run(Player player, Arguments arguments) {
		
		String message = appendFrom(convertArgumentsToArray(arguments), 0);
		message = Utils.color(Placeholders.replacePlaceholders(player, message));
		
		player.sendMessage(message);
		
	}

	@Override
	public Validity validity(Arguments arguments) {
		return Validity.TRUE;
	}

}
