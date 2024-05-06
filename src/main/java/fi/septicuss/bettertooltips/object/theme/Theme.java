package fi.septicuss.bettertooltips.object.theme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.configuration.ConfigurationSection;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.object.NamespacedPath;
import fi.septicuss.bettertooltips.object.validation.Validatable;

public class Theme implements Validatable {

	private String id;
	private NamespacedPath path;

	private int lines;
	private int height;
	private int padding;
	private int themeAscent;
	private int textStartAscent;
	private int textLineSpacing;

	// Calculated from texture
	private double width;

	// Validatable
	private boolean valid = false;

	public Theme(ConfigurationSection themeSection) {
		this.id = themeSection.getName();

		if (!themeSection.contains("path")) {
			Tooltips.warn(String.format("Theme \"%s\" doesn't define a path to a texture.", id));
			return;
		}

		if (!themeSection.contains("ascents")) {
			Tooltips.warn(String.format("Theme \"%s\" doesn't define the ascents.", id));
			return;
		}

		if (!themeSection.contains("lines")) {
			Tooltips.warn(String.format("Theme \"%s\" doesn't define line amount.", id));
			return;
		}

		this.path = new NamespacedPath(themeSection.getString("path"), "textures");
		this.height = themeSection.getInt("height");
		this.padding = themeSection.getInt("padding", 1);
		this.lines = themeSection.getInt("lines");

		ConfigurationSection ascentSection = themeSection.getConfigurationSection("ascents");
		this.themeAscent = ascentSection.getInt("theme-ascent");
		this.textStartAscent = ascentSection.getInt("text-start-ascent");
		this.textLineSpacing = ascentSection.getInt("text-line-spacing");

		final File texture = new File(Tooltips.getPackAssetsFolder(), path.getFullPath());

		if (!texture.exists()) {
			Tooltips.warn(String.format("Theme \"%s\" has an invalid texture \"%s\"", id, path.getNamespacedPath()));
			return;
		}

		try {
			final BufferedImage image = ImageIO.read(texture);
			final int imageWidth = image.getWidth();

			if (imageWidth % 3 != 0) {
				Tooltips.warn(String.format("Theme \"%s\" has invalid texture width (must be a multiple of 3)", id));
				return;
			}
			
			double definedHeight = height;
			double imageHeight = image.getHeight();

			double heightRatio = definedHeight / imageHeight;
			double partWidth = imageWidth / 3;

			double width = partWidth * heightRatio;
			
			this.width = Math.max(1D, (width));
			
		} catch (IOException e) {
			Tooltips.warn(String.format("Theme \"%s\" failed to load texture \"%s\". Error: %s", id,
					path.getNamespacedPath(), e.getMessage()));
			return;
		}

		valid = true;

	}

	public String getId() {
		return id;
	}

	public NamespacedPath getPath() {
		return path;
	}

	public int getHeight() {
		return height;
	}

	public int getLines() {
		return lines;
	}

	public int getThemeAscent() {
		return themeAscent;
	}

	public int getTextStartAscent() {
		return textStartAscent;
	}

	public int getTextLineSpacing() {
		return textLineSpacing;
	}
	
	public int getPadding() {
		return padding;
	}

	public double getWidth() {
		return width;
	}

	public String getFontName() {
		return path.getNamespace() + ":themes/" + id;
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

}
