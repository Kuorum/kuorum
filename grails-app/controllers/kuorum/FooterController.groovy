package kuorum

import org.springframework.context.i18n.LocaleContextHolder

class FooterController {

    def userGuides() {
        Locale locale = LocaleContextHolder.getLocale();
        String langGuides = "en"
        String imgFile1 = "guide1_"
        String imgFile2 = "guide2_"
        if (locale.getLanguage() == "es"){
            langGuides = "es"
        }
        [
                langGuides:langGuides,
                imgFile1:imgFile1+langGuides+".png",
                imgFile2:imgFile2+langGuides+".png"

        ]
    }
    def widget() {}

    def privacyPolicy(){}
    def termsUse(){}

    def footerUserGuides() {
        Locale locale = LocaleContextHolder.getLocale();
        String langGuides = "en"
        String imgFile1 = "guide1_"
        String imgFile2 = "guide2_"
        if (locale.getLanguage() == "es"){
            langGuides = "es"
        }
        [
                langGuides:langGuides,
                imgFile1:imgFile1+langGuides+".png",
                imgFile2:imgFile2+langGuides+".png"

        ]
    }
}
