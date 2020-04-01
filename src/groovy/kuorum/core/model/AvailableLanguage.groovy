package kuorum.core.model

import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

/**
 * Available languages on the platform. This enum is used to model the data on de Mongo
 */
public enum AvailableLanguage{

    es_ES(new Locale("es","ES")),
    ca_ES(new Locale("ca","ES")),
    de_DE(new Locale("de","DE")),
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

    public static AvailableLanguage fromLocaleParam(LanguageRSDTO lang){
        AvailableLanguage res = null;
        try{
            res = AvailableLanguage.valueOf(lang.toString())
        }catch (Exception e){
            res = null;
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

    public boolean isSpanishLang(){
        return this.locale.getCountry() == "ES"
    }
}
