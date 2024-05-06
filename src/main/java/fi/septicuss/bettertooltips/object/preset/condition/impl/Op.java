package fi.septicuss.bettertooltips.object.preset.condition.impl;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.validation.Validity;

public class Op implements Condition {

	@Override
	public boolean check(Player player, Arguments args) {
		return player.isOp();
	}

	@Override
	public Validity valid(Arguments args) {
		return Validity.TRUE;
	}

}
