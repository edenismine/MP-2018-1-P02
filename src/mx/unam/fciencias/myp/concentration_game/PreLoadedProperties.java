package mx.unam.fciencias.myp.concentration_game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PreLoadedProperties {
    private static final Properties properties = new Properties();
    private static PreLoadedProperties instance = null;

    private PreLoadedProperties() {
        InputStream input = null;
        String filename = "src\\mx\\unam\\fciencias\\myp\\concentration_game\\settings.properties";
        try {
            input = new FileInputStream(filename);
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static PreLoadedProperties getInstance() {
        if (instance == null) {
            synchronized (PreLoadedProperties.class) {
                if (instance == null) {
                    instance = new PreLoadedProperties();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return this.getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
