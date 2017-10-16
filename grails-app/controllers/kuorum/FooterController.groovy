package kuorum

import org.springframework.context.i18n.LocaleContextHolder

class FooterController {

    def aboutUs() {}
    def vision() {}
    def team() {}
    def tech() {}
    def citizens() {}
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
    def leaders() {}
    def government() {}
    def widget() {}
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


    def footerAboutUs(){
        render(view: "footerAboutUs")
    }

    def footerContactUs(){
        render(view: "footerContactUs")
    }

    def footerOurTeam(){
        render(view: "footerOurTeam")
    }

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

    def footerHistory(){
        render(view: "footerHistory")
    }

    def footerPress(){
        render(view: "footerPress")
    }

    def customBlog(){
        render(view: "customBlog")
    }
}
