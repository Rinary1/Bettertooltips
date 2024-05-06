package fi.septicuss.bettertooltips.utils.cache.tooltip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fi.septicuss.bettertooltips.integrations.papi.PAPI;
import fi.septicuss.bettertooltips.object.title.TitleBuilder;

public class TooltipCache {

	// TODO Clear
	// Player -> Hash Code -> Tooltip
	private static final Map<UUID, Map<Integer, TitleBuilder>> PLAYER_TOOLTIP_CACHE = new ConcurrentHashMap<>();

	// TODO: Cache unprocessedText hashes
	
	public static void clear() {
		PLAYER_TOOLTIP_CACHE.clear();
	}

	public static void cache(Player player, List<String> unprocessedText, TitleBuilder builder) {
		UUID uuid = player.getUniqueId();
		int code = getHashCode(player, unprocessedText);

		Map<Integer, TitleBuilder> map = PLAYER_TOOLTIP_CACHE.getOrDefault(uuid, new HashMap<>());
		map.put(code, builder);
		PLAYER_TOOLTIP_CACHE.put(uuid, map);
	}
	
	public static void remove(Player player) {
		PLAYER_TOOLTIP_CACHE.remove(player.getUniqueId());
	}

	public static boolean contains(Player player, List<String> unprocessedText) {
		UUID uuid = player.getUniqueId();

		Map<Integer, TitleBuilder> map = PLAYER_TOOLTIP_CACHE.getOrDefault(uuid, new HashMap<>());
		if (map.isEmpty())
			return false;

		int code = getHashCode(player, unprocessedText);
		return map.containsKey(code);
	}

	public static TitleBuilder get(Player player, List<String> unprocessedText) {
		UUID uuid = player.getUniqueId();

		Map<Integer, TitleBuilder> map = PLAYER_TOOLTIP_CACHE.getOrDefault(uuid, new HashMap<>());
		if (map.isEmpty())
			return null;

		int code = getHashCode(player, unprocessedText);
		return map.get(code);
	}

	private static int getHashCode(Player player, List<String> unprocessedText) {
		List<String> processed = PAPI.replacePlaceholders(player, unprocessedText);
		return processed.hashCode();
	}

}
