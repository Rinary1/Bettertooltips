package fi.septicuss.bettertooltips.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;

import fi.septicuss.bettertooltips.Tooltips;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

public class LanguageLoader {
    HashMap<String, String> translationMap;

    public LanguageLoader(Tooltips plugin){
        File languageDirectory = new File(plugin.getDataFolder(), "/");
        File defaultLanguageFile = new File(plugin.getDataFolder(), "/messages.yml");

        if (!languageDirectory.isDirectory()){
            languageDirectory.mkdir();
            try {
                InputStream stream = plugin.getResource("messages.yml");
                FileUtils.copyInputStreamToFile(stream, defaultLanguageFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
        for (String translation : translations.getKeys(false)) {
            translationMap.put(translation, translations.getString(translation));
        }
    }
    public String get(String path){
        return translationMap.get(path);
    }
}
