package com.jukisawa.discordBot.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService{
    public static String loadConfig(String key) {
        Properties props = new Properties();
        try (InputStream input = ConfigService.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties nicht gefunden!");
            }
            props.load(input);
            String value = props.getProperty(key);
            if (value == null || value.isEmpty()) {
                throw new RuntimeException(key + " nicht in config.properties gefunden!");
            }
            return value;
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden von config.properties", e);
        }
    }

}
