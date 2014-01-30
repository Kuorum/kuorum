package kuorum.core.model

/**
 * Available languages on the platform. This enum is used to model the data on de Mongo
 */
public enum AvailableLanguage{

    es_ES(new Locale("es","ES")),
    en_EN(new Locale("en","EN"))

    Locale locale
    AvailableLanguage(Locale locale){
        this.locale = locale
    }
}
