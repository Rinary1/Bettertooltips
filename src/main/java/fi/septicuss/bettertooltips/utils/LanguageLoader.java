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
        translationMap = new HashMap<>(); // init TranslateMap

        File languageDirectory = new File(plugin.getDataFolder(), "languages/");
        File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");

        if (!languageDirectory.isDirectory()){
            languageDirectory.mkdir();
            InputStream stream = plugin.getResource("en_US.yml");
            if (stream != null) {
                try {
                    FileUtils.copyInputStreamToFile(stream, defaultLanguageFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (plugin.getConfig().getString("locale") != null && plugin.getConfig().getString("locale").equals("en_US")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "languages/" + plugin.getConfig().getString("locale") + ".yml"));
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        }
        else{
            FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        }
    }
    public String get(String path){
        return translationMap.get(path);
    }
}
