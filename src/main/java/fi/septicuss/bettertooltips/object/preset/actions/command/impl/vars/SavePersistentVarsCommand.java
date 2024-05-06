package fi.septicuss.bettertooltips.object.preset.actions.command.impl.vars;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.actions.command.ActionCommand;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.variable.Variables;

public class SavePersistentVarsCommand extends VarCommand implements ActionCommand {

	@Override
	public void run(Player player, Arguments arguments) {
		Variables.PERSISTENT.save();
	}

	@Override
	public Validity validity(Arguments arguments) {
		return Validity.TRUE;
	}

}
