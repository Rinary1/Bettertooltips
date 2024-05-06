package fi.septicuss.bettertooltips.object.preset.condition.impl;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.preset.condition.type.MultiString;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.placeholder.Placeholders;

public class Permission implements Condition {

	private static final String[] PERMISSION = { "p", "perm", "permission" };

	@Override
	public boolean check(Player player, Arguments args) {
		MultiString permissions = MultiString.of(args.get(PERMISSION).getAsString());
		for (var permission : permissions.getStrings())
			if (player.hasPermission(Placeholders.replacePlaceholders(player, permission)))
				return true;
		return false;
	}

	@Override
	public Validity valid(Arguments args) {
		if (!args.has(PERMISSION)) {
			return Validity.of(false, "Permission argument is required");
		}

		return Validity.TRUE;
	}

}
