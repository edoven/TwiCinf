package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HomePathGetter {

    private final String CRAWNKER_HOME_PROPERTY_KEY = "CRAWNKER_HOME";
    private final String webappRootDir = this.getClass().getResource("/").getPath();
    private final String CONFIG_FILE_PATH = webappRootDir + "../../config/webapp.config";

    private static HomePathGetter instance = null;
    private String homePath;

    private HomePathGetter() {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        homePath = properties.getProperty(CRAWNKER_HOME_PROPERTY_KEY);
    }

    public String getHomePath() {

        return this.homePath;
    }

    public static HomePathGetter getInstance() {

        if (instance == null) {
            instance = new HomePathGetter();
        }
        return instance;
    }

}
