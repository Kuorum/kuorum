package kuorum.core.model

/**
 * Available languages on the platform. This enum is used to model the data on de Mongo
 */
public enum AvailableLanguage{

    es_ES(new Locale("es","ES")),
    en_EN(new Locale("en","EN"));

    Locale locale
    AvailableLanguage(Locale locale){
        this.locale = locale
    }

    /**
     * Returns the available language depending on language
     * @param lang
     * @return
     */
    public static AvailableLanguage fromLocaleParam(String lang){
        AvailableLanguage res = null;
        AvailableLanguage.values().each {availableLanguage ->
            if (availableLanguage.locale.language ==lang){
                res = availableLanguage
            }
        }
        return res;
    }

    public static AvailableLanguage fromLocale(Locale locale){
        AvailableLanguage res = null;
        AvailableLanguage.values().each {availableLanguage ->
            if (availableLanguage.locale.language == locale.getLanguage()){
                res = availableLanguage
            }
        }
        return res;
    }
}
