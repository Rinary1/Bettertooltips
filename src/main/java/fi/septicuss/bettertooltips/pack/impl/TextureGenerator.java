package fi.septicuss.bettertooltips.pack.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import fi.septicuss.bettertooltips.object.NamespacedPath;
import fi.septicuss.bettertooltips.pack.Generator;
import fi.septicuss.bettertooltips.pack.PackData;
import fi.septicuss.bettertooltips.utils.FileUtils;

public class TextureGenerator implements Generator {

	@Override
	public void generate(PackData packData, File assetsDirectory, File targetDirectory) {

		for (NamespacedPath namespacedPath : packData.getUsedTextures()) {
			String filePath = namespacedPath.getFullPath();

			Path from = Path.of(assetsDirectory.getPath(), filePath);
			Path to = Path.of(targetDirectory.getPath(), filePath);
			
			try {
				FileUtils.createIfNotExists(to.toFile());
				Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			
		}
	}

	@Override
	public String getName() {
		return "Texture Generator";
	}

}
