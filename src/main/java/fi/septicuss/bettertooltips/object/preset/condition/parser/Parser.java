package fi.septicuss.bettertooltips.object.preset.condition.parser;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.utils.Utils;

public interface Parser<T> {

	public T parse(String presetName, String from);

	default void warn(String message) {
		Tooltips.warn(message);
	}

	default String quote(String message) {
		return Utils.quote(message);
	}

}
