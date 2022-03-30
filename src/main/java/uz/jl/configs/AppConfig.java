package uz.jl.configs;

import uz.jl.enums.settings.Language;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Elmurodov Javohir, Wed 3:45 PM. 12/8/2021
 */
public class AppConfig {

    public static Language language;
    public static Properties p;

    static {
        load();
        language = Language.getByCode(get("system.language"));
    }

    public static String get(String key) {
        return p.getProperty(key);
    }

    private static void load() {
        p = new Properties();
        try (FileReader fileReader = new FileReader("src/main/resources/app.properties")) {
            p.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
