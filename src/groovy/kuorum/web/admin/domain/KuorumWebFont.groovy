package kuorum.web.admin.domain

enum KuorumWebFont {

    ARIAL_HELVETICA("Arial", "\"Helvetica Neue\", Helvetica,Arial,sans-serif"),
    ARIAL_ARIAL("Arial", "Arial"),
    GEORGIA_HELVETICA("Georgia","\"Helvetica Neue\", Helvetica,Arial,sans-serif"),
    UBUNTU_HELVETICA("Ubuntu","\"Helvetica Neue\", Helvetica,Arial,sans-serif"),
    MERRIWHEADER_OPENSANS("'Merriweather', serif", "'Open Sans', sans-serif"),
    LATO_MERRIWHEADER("'Lato', sans-serif", "'Merriweather', serif");

    String titleFontName;
    String textFontName;

    KuorumWebFont(String titleFontName,textFontName) {
        this.titleFontName = titleFontName
        this.textFontName = textFontName
    }

    static KuorumWebFont build(String fontCombinationName){
        KuorumWebFont res = KuorumWebFont.values().find{it.name()==fontCombinationName}
        if (!res)
            res = KuorumWebFont.GEORGIA_HELVETICA;
        return res;
    }
}