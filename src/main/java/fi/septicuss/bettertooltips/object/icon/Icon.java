package fi.septicuss.bettertooltips.object.icon;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.object.NamespacedPath;
import fi.septicuss.bettertooltips.object.validation.Validatable;
import fi.septicuss.bettertooltips.utils.Utils;

public class Icon implements Validatable {

	private String name;
	private NamespacedPath path;
	private int ascent;
	private int height;

	private char unicode = ' ';
	private boolean valid = false;

	public Icon(ConfigurationSection iconSection) {
		this.name = iconSection.getName();
		this.ascent = iconSection.getInt("ascent");
		this.height = iconSection.getInt("height");

		String pathStr = iconSection.getString("path");

		if (pathStr == null) {
			Tooltips.warn(String.format("Icon " + Utils.quote(name) + " does not define a path to the texture."));
			return;
		}

		this.path = new NamespacedPath(pathStr, "textures");

		var textureFile = new File(Tooltips.getPackAssetsFolder(), path.getFullPath());
		var fileExists = (textureFile.exists());

		if (!fileExists) {
			Tooltips.warn("Icon " + Utils.quote(name) + " uses an invalid texture " + Utils.quote(path.getNamespacedPath()));
			return;
		}

		this.valid = true;
	}

	public String getName() {
		return name;
	}

	public NamespacedPath getPath() {
		return path;
	}

	public int getAscent() {
		return ascent;
	}

	public int getHeight() {
		return height;
	}

	public char getUnicode() {
		return unicode;
	}

	public void setUnicode(char unicode) {
		this.unicode = unicode;
	}

	public boolean hasUnicode() {
		return (unicode != ' ');
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

}
