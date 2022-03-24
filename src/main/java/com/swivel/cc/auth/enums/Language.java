package com.swivel.cc.auth.enums;


import com.swivel.cc.auth.exception.AuthServiceException;

/**
 * Application supported languages are listed here
 */
public enum Language {

    ENGLISH,
    BAHASA;

    /**
     * This method validates a given language.
     *
     * @param language language
     * @return true/ false
     */
    public static boolean isValidLanguage(String language) {
        if (language != null) {
            for (Language l : Language.values()) {
                if (l.toString().equalsIgnoreCase(language.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method returns relevant language.
     *
     * @param language language
     * @return Language
     */
    public static Language getLanguage(String language) {
        if (language != null) {
            for (Language l : Language.values()) {
                if (l.toString().equalsIgnoreCase(language.trim())) {
                    return l;
                }
            }
        }
        throw new AuthServiceException("Invalid language");
    }

}
