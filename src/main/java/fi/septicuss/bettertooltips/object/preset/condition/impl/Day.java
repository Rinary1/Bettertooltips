package fi.septicuss.bettertooltips.object.preset.condition.impl;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;

public class Day implements Condition {

	@Override
	public boolean check(Player player, Arguments args) {
		long worldTime = player.getWorld().getTime();

		return (worldTime >= 0 && worldTime < 13000);
	}

	@Override
	public Validity valid(Arguments args) {
		return Validity.of(true);
	}

}
