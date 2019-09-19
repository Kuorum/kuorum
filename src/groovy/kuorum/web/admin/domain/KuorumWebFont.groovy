package kuorum.web.admin.domain

enum KuorumWebFont {

    ARIAL("Arial"),
    GEORGIA("Georgia"),
    UBUNTU("Ubuntu");

    String fontName;

    KuorumWebFont(String fontName) {
        this.fontName = fontName
    }

    static KuorumWebFont build(String fontName){
        KuorumWebFont res = KuorumWebFont.values().find{it.fontName==fontName}
        if (!res)
            res = KuorumWebFont.GEORGIA;
        return res;
    }
}