package uz.jl.enums.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Elmurodov Javohir, Wed 4:17 PM. 12/8/2021
 */

@Getter
@AllArgsConstructor
public enum Language {

    UZ("uz", "Uzbek"),
    RU("ru", "Russian"),
    EN("en", "English");

    private final String code;
    private final String name;

    public static Language getByCode(String lang) {
        for (Language language : values()) {
            if (language.getCode().equalsIgnoreCase(lang)) return language;
        }
        return null;
    }
}
