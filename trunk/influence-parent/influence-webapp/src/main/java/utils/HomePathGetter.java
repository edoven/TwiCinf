package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HomePathGetter {

    private final String CRAWNKER_HOME_PROPERTY_KEY = "CRAWNKER_HOME";

    private final String webappRootDir;

    private final String CONFIG_FILE_PATH;

    private static HomePathGetter instance = null;
    private String homePath;

    private HomePathGetter() {

        this.webappRootDir = this.getClass().getResource("/").getPath();
        this.CONFIG_FILE_PATH = this.webappRootDir + "../../config/webapp.properties";

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE_PATH));
        } catch (FileNotFoundException e) {
            String emsg = "cant find " + CONFIG_FILE_PATH + " file";
            throw new RuntimeException(emsg, e);
        } catch (IOException e) {
            String emsg = "cant read " + CONFIG_FILE_PATH + " file";
            throw new RuntimeException(emsg, e);
        }
        this.homePath = properties.getProperty(CRAWNKER_HOME_PROPERTY_KEY);
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
