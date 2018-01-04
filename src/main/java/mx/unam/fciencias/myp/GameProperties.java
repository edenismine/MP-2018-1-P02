package mx.unam.fciencias.myp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GameProperties {
    private static final Properties properties = new Properties();
    private static GameProperties instance = null;

    private GameProperties() {
        InputStream input = null;
        String filename = "settings.properties";
        try {
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }
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

    static GameProperties getInstance() {
        if (instance == null) {
            synchronized (GameProperties.class) {
                if (instance == null) {
                    instance = new GameProperties();
                }
            }
        }
        return instance;
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property
     * list, and its defaults, recursively, are then checked.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     * The method returns null if the property is not found.
     */
    String getProperty(String key) {
        return this.getProperty(key, null);
    }

    /**
     * Searches for the property with the specified key in this property list. If the key is not found in this property list, the default property list, and its defaults, recursively, are then checked. The method returns the default value argument if the property is not found.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     */
    String getProperty(String key, String defaultValue) {
        properties.getProperty("");
        return properties.getProperty(key, defaultValue);
    }
}
