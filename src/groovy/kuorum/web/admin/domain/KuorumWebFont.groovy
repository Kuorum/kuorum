package kuorum.web.admin.domain

enum KuorumWebFont {

//    MONTSERRAT_MONTSERRATBOLD("Montserrat, sans-serif","Montserrat, sans-serif", "700"),
//    ROBOTO_ROBOTOBOLD("Roboto, sans-serif","Roboto, sans-serif", "700"),
    HELVETICA_ARIAL("\"Helvetica Neue\", Helvetica,Arial,sans-serif","Arial" ),
    ARIAL_ARIAL("Arial", "Arial", ),
//    ARIAL_ARIALBOLD("Arial", "Arial", "bold"),
    HELVETICA_GEORGIA("\"Helvetica Neue\", Helvetica,Arial,sans-serif", "Georgia"),
    UBUNTU_HELVETICA("Ubuntu, Helvetica","\"Helvetica Neue\", Helvetica, sans-serif"),
    MERRIWHEADER_OPENSANS("'Merriweather', serif","'Open Sans', sans-serif"),
    LATO_MERRIWHEADER("'Lato', sans-serif","'Merriweather', serif");

    String titleFontName;
    String titleFontWeight;
    String textFontName;

    KuorumWebFont(String titleFontName,String textFontName) {
        this(titleFontName, textFontName, "normal")
    }
    KuorumWebFont(String titleFontName, String textFontName, String titleWeight) {
        this.titleFontName = titleFontName
        this.textFontName = textFontName
        this.titleFontWeight = titleWeight
    }

    static KuorumWebFont build(String fontCombinationName){
        KuorumWebFont res = KuorumWebFont.values().find{it.name()==fontCombinationName}
        if (!res)
            res = KuorumWebFont.HELVETICA_GEORGIA;
        return res;
    }
}