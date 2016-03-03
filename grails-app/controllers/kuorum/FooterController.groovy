package kuorum

import org.springframework.context.i18n.LocaleContextHolder

class FooterController {

    def aboutUs() {}
    def vision() {}
    def team() {}
    def tech() {}
    def citizens() {}
    def impact() {}
    def politicians() {}
    def developers() {}
    def information() {
        Locale locale = LocaleContextHolder.getLocale();
        String langPressKit = "en"
        if (locale.getLanguage() == "es"){
            langPressKit = "es"
        }
        [
                langPressKit:langPressKit
        ]
    }
    def privacyPolicy(){}
    def termsUse(){}

}
