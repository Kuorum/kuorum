package kuorum.web.admin.domain

enum KuorumWebFont {

    HELVETICA_ARIAL("\"Helvetica Neue\", Helvetica,Arial,sans-serif","Arial" ),
    ARIAL_ARIAL("Arial", "Arial"),
    HELVETICA_GEORGIA("\"Helvetica Neue\", Helvetica,Arial,sans-serif", "Georgia"),
    UBUNTU_HELVETICA("Ubuntu, Helvetica","\"Helvetica Neue\", Helvetica, sans-serif"),
    OPENSANS_MERRIWHEADER("'Open Sans', sans-serif","'Merriweather', serif"),
    MERRIWHEADER_LATO("'Merriweather', serif","'Lato', sans-serif");

    String titleFontName;
    String textFontName;

    KuorumWebFont(String titleFontName,textFontName) {
        this.titleFontName = titleFontName
        this.textFontName = textFontName
    }

    static KuorumWebFont build(String fontCombinationName){
        KuorumWebFont res = KuorumWebFont.values().find{it.name()==fontCombinationName}
        if (!res)
            res = KuorumWebFont.HELVETICA_GEORGIA;
        return res;
    }
}