package utils.properties;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class ConfProperties {
    private static final Properties commonProperties;
    private static final Logger logger = LogManager.getLogger(ConfProperties.class);

    static {
        commonProperties = loadProperties("src/test/resources/conf.properties");
    }

    public static String string(String key) {
        return commonProperties.getProperty(key);
    }

    public static boolean bool(String key) {
        return Boolean.parseBoolean(commonProperties.getProperty(key));
    }

    public static int integer(String key) {
        return Integer.parseInt(commonProperties.getProperty(key));
    }

    private static Properties loadProperties(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            logger.error("Ошибка чтения файла конфигурации", e);
            throw new RuntimeException("Не удалось загрузить properties-файл: " + filePath, e);
        }
    }
}
