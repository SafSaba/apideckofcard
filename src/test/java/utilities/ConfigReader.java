package utilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    private static Properties configFile;

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("configuration.properties");
            configFile = new Properties();
            configFile.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            logger.info(":: An Exception has occurred " + e);
        }
    }

    public static String getProperty(String key) {
        return configFile.getProperty(key);
    }

}
