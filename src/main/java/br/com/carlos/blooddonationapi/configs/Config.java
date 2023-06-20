package br.com.carlos.blooddonationapi.configs;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String PROPERTIES = "application.properties";

    private Config() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Properties loadProperties(){

        Properties properties = null;

        try(InputStream inputStream = new FileInputStream(Config.PROPERTIES)) {

            properties = new Properties();

            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String getProperty(String property) {
        Properties properties = Config.loadProperties();

        if (properties != null) {
            return properties.getProperty(property);
        }

        return "";
    }
}
