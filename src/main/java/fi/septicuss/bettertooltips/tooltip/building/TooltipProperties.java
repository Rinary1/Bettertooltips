package fi.septicuss.bettertooltips.tooltip.building;

import fi.septicuss.bettertooltips.object.preset.Preset;
import fi.septicuss.bettertooltips.object.theme.Theme;

public class TooltipProperties {

	private Theme theme;
	private String color;
	private int horizontalShift;

	protected TooltipProperties() {

	}

	protected TooltipProperties(Theme theme, String color, int horizontalShift) {
		this.theme = theme;
		this.color = color;
		this.horizontalShift = horizontalShift;
	}

	public Theme getTheme() {
		return theme;
	}

	public String getColor() {
		return color;
	}

	public int getHorizontalShift() {
		return horizontalShift;
	}

	public static TooltipProperties from(Preset preset) {
		return new TooltipProperties(preset.getTheme(), preset.getColor(), preset.getHorizontalShift());
	}

	public static TooltipProperties from(Theme theme) {
		return new TooltipProperties(theme, "#ffffff", 0);
	}

}
