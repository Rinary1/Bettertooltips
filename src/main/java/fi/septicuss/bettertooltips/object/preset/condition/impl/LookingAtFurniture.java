package fi.septicuss.bettertooltips.object.preset.condition.impl;

import java.util.function.Predicate;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.object.preset.condition.Condition;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Arguments;
import fi.septicuss.bettertooltips.object.preset.condition.type.MultiString;
import fi.septicuss.bettertooltips.object.validation.Validity;
import fi.septicuss.bettertooltips.utils.Utils;
import fi.septicuss.bettertooltips.utils.cache.player.LookingAtCache;

public class LookingAtFurniture implements Condition {

	private static final String[] DISTANCE = { "d", "distance" };
	private static final String[] ID = { "id" };


	@Override
	public boolean check(Player player, Arguments args) {
		final var provider = Tooltips.get().getFurnitureProvider();
		
		MultiString id = null;
		int distance = 3;

		if (args.has(DISTANCE))
			distance = args.get(DISTANCE).getAsInt();

		if (args.has(ID))
			id = MultiString.of(args.get(ID).getAsString());

		final MultiString finalizedId = id;
		
		Predicate<Block> blockPredicate = (block -> {
			if (block == null) return false;
			if (!provider.isFurniture(block)) return false;
			if (finalizedId == null) return true;
			return (finalizedId.contains(provider.getFurnitureId(block)));
		});
		
		Predicate<Entity> entityFilter = (entity -> {
			if (entity == null) return false;
			if (entity.equals(player)) return false;
			return Tooltips.FURNITURE_ENTITIES.contains(entity.getType());
		});
		
		var rayTrace = Utils.getRayTrace(player, distance, blockPredicate, entityFilter);

		if (rayTrace == null) {
			return false;
		}
		
		// Block
		if (rayTrace.getHitBlock() != null) {
			final Block block = rayTrace.getHitBlock();
			final String furnitureId = provider.getFurnitureId(block);
			
			cache(player, provider.getFurnitureId(block));
			
			if (finalizedId != null) { 
				return finalizedId.contains(furnitureId);
			}
			return true;
		}
		
		// Entity
		if (rayTrace.getHitEntity() != null) {
			final Entity entity = rayTrace.getHitEntity();
			if (entity != null && provider.isFurniture(entity)) {
				final String furnitureId = provider.getFurnitureId(entity);

				cache(player, furnitureId);
				
				if (finalizedId != null) {
					return finalizedId.contains(furnitureId);
				}
				
				return true;
			}
		}

		return false;
	}

	@Override
	public Validity valid(Arguments args) {

		var provider = Tooltips.get().getFurnitureProvider();
		
		if (provider == null) {
			return Validity.of(false, "No furniture plugin present");
		}

		if (args.has(DISTANCE) && !args.get(DISTANCE).isNumber()) {
			return Validity.of(false, "Distance must be a number");
		}
		
		return Validity.TRUE;
	}
	
	private void cache(Player player, String id) {
		LookingAtCache.put(player, id);
	}

}
