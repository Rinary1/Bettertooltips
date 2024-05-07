package fi.septicuss.bettertooltips.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import fi.septicuss.bettertooltips.Tooltips;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageLoader  {

    public static void setupLanguage(Tooltips plugin) {

        try {
            LanguageLoader(plugin);
        } catch (IOException e) {
            Tooltips.warn("Failed to set up messages.yml");
            e.printStackTrace();
        }

    }

    public static void LanguageLoader(Tooltips plugin) throws IOException {

        final String internalPath = "default/config/messages.yml";
        final String targetPath = "messages.yml";

        final File dataFolder = plugin.getDataFolder();
        final File existingConfigFile = new File(dataFolder, targetPath);

        if (!existingConfigFile.exists()) {
            copyFromJar(plugin, internalPath, existingConfigFile);
        }

        // Update existing config if needed
        final InputStreamReader reader = new InputStreamReader(plugin.getResource(internalPath));

        final FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(existingConfigFile);
        final FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(reader);

        for (String key : internalConfig.getKeys(true)) {
            if (!existingConfig.contains(key)) {
                existingConfig.set(key, internalConfig.get(key));
                existingConfig.setComments(key, internalConfig.getComments(key));
            }
        }

        existingConfig.save(existingConfigFile);


    }

    public static void copyFromJar(Tooltips plugin, String internalPath, File targetFile) throws IOException {
        final InputStream stream = plugin.getResource(internalPath);
        if (stream == null)
            return;

        targetFile.getParentFile().mkdirs();

        Files.copy(stream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
