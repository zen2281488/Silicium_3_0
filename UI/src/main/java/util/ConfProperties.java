package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfProperties {
    private static final Properties properties;

    static {
        properties = loadProperties("src/test/resources/conf.properties");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    private static Properties loadProperties(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from file: " + filePath, e);
        }
    }
}