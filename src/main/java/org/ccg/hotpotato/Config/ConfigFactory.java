package org.ccg.hotpotato.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class ConfigFactory {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PREFIX = "config_";
    private static final String DEFAULT_CONFIG_NAME = "default";

    public static boolean SaveConfig(Config config)  {
        try (FileWriter writer = new FileWriter(GetConfigFileName(config))) {
            gson.toJson(config, writer);
            Bukkit.getLogger().info("Config saved: " + config.get_configName());
            return true;
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error saving config: " + e.getMessage());
            return false;
        }
    }
    
    public static <T extends Config> T LoadConfig(Class<T> configClass, String configName) {
        try {
            try (FileReader reader = new FileReader(GetConfigFileName(configName, configClass))) {
                Bukkit.getLogger().info("Config loaded: " + configName);
                return gson.fromJson(reader, configClass);
            } catch (IOException e) {
                try(FileReader reader = new FileReader(GetConfigFileName(DEFAULT_CONFIG_NAME, configClass))) {
                    Bukkit.getLogger().info("Default config loaded");
                    return gson.fromJson(reader, configClass);
                } catch (IOException e2) {
                    T config = configClass.getDeclaredConstructor().newInstance();
                    config.SetConfigName(DEFAULT_CONFIG_NAME);
                    SaveConfig(config);
                    Bukkit.getLogger().info("Default config created");
                    return config;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T extends Config> T LoadConfig(Class<T> configClass) {
        return LoadConfig(configClass, DEFAULT_CONFIG_NAME);
    }
    
    private static String GetConfigFileName(Config config)
    {
        if (config.get_configName().isEmpty() || config.get_configName().isBlank())
            return GetConfigFileName(DEFAULT_CONFIG_NAME, config.getClass());
        return GetConfigFileName(config.get_configName(), config.getClass());
    }
    
    private static String GetConfigFileName(String configName, Class<? extends Config> configClass)
    {
        return CONFIG_PREFIX + configClass.getSimpleName() + "_" + configName + ".json";
    }

}
