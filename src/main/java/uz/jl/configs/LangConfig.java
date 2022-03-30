package uz.jl.configs;

import uz.jl.enums.settings.Language;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Elmurodov Javohir, Thu 3:15 PM. 12/9/2021
 */
public class LangConfig {
    public static Properties uz;
    public static Properties ru;
    public static Properties en;
    public static String pathPre = "src/main/resources/i18n/";

    static {load();}
    private static void load() {
        try (FileReader uzFileReader = new FileReader(pathPre + "uz.properties");
             FileReader ruFileReader = new FileReader(pathPre + "ru.properties");
             FileReader enFileReader = new FileReader(pathPre + "en.properties")) {
            uz = new Properties();
            ru = new Properties();
            en = new Properties();
            uz.load(uzFileReader);
            ru.load(ruFileReader);
            en.load(enFileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        Language language = Session.getInstance().getUser().getLanguage();
        return get(key,language);
    }

    public static String get(String key, Language language) {
        String langCode = language.getCode();
        if (langCode.equalsIgnoreCase("uz"))
            return uz.getProperty(key);
        if (langCode.equalsIgnoreCase("ru"))
            return ru.getProperty(key);
        return en.getProperty(key);
    }
}
