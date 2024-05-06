package fi.septicuss.bettertooltips.object.preset.condition.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.preset.condition.type.EnumOptions;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;

public class LookingAtEntity implements Condition {

	private static final String[] DISTANCE = {"d", "dist", "distance"};
	private static final String[] TYPE = {"entity", "type", "t"};
	
	@Override
	public boolean check(Player player, Arguments args) {
		
		int distance = 3;
		EnumOptions<EntityType> entities = null;

		if (args.has(DISTANCE))
			distance = args.get(DISTANCE).getAsInt();
		
		if (args.has(TYPE))
			entities = args.get(TYPE).getAsEnumOptions(EntityType.class);
		
		var rayTrace = Utils.getRayTraceResult(player, distance);
		
		if (rayTrace == null || rayTrace.getHitEntity() == null) 
			return false;
		
		if (entities == null)
			return true;

		Entity entity = rayTrace.getHitEntity();
		
		if (entity == null) return false;
		return entities.contains(entity.getType());
	}

	@Override
	public Validity valid(Arguments args) {
		
		if (args.has(DISTANCE) && !args.isNumber(DISTANCE)) {
			return Validity.of(false, "Distance integer must be present");
		}
		
		if (args.has(TYPE)) {
			String type = args.get(TYPE).getAsString();
			Validity typeValidity = EnumOptions.validity(EntityType.class, type);
			
			if (!typeValidity.isValid())
				return typeValidity;
		}
		
		return Validity.TRUE;
	}

}
