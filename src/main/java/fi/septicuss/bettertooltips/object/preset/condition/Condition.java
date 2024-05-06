package fi.septicuss.bettertooltips.object.preset.condition;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;

public interface Condition {

	public boolean check(Player player, Arguments args);

	public Validity valid(Arguments args);

	default String quote(String message) {
		return Utils.quote(message);
	}
	
}
