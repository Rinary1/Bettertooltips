package fi.septicuss.bettertooltips.object.preset.condition.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.integrations.IntegratedPlugin;
import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.preset.condition.type.MultiString;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;
import io.lumine.mythic.bukkit.MythicBukkit;

public class LookingAtMythicMob implements Condition {

	// lookingatmythicmob{id=meow, meow1; dist=2}

	private static final String[] DISTANCE = { "distance", "dist", "d" };
	private static final String[] ID = { "id", "ids" };

	@Override
	public boolean check(Player player, Arguments args) {
		
		int distance = 3;

		if (args.has(DISTANCE))
			distance = args.get(DISTANCE).getAsInt();

		var rayTrace = Utils.getRayTraceResult(player, distance);
		
		if (rayTrace == null || rayTrace.getHitEntity() == null)
			return false;
		
		Entity entity = rayTrace.getHitEntity();
		MultiString ids = null;

		if (args.has(ID))
			ids = MultiString.of(args.get(ID).getAsString());
		
		var mob = MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId()).orElse(null);
		
		if (mob == null)
			return false;
		
		if (ids == null)
			return true;
		
		String type = String.valueOf(mob.getMobType());
		return ids.contains(type);
	}

	@Override
	public Validity valid(Arguments args) {
		if (!IntegratedPlugin.MYTHICMOBS.isEnabled())
			return Validity.of(false, "MythicMobs plugin is required for this condition.");
		
		return Validity.TRUE;
	}

}
