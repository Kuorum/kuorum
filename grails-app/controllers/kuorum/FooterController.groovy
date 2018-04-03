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

    def footerAboutUs(){}

    def footerContactUs(){}

    def footerOurTeam(){}

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

    def footerHistory(){}

    def footerPress(){
        Locale locale = LocaleContextHolder.getLocale();
        String lang = "en"
        if (locale.getLanguage() == "es"){
            lang = "es"
        }
        [lang:lang]
    }

    def footerBlog(){}
    def footerBlog001(){render (view: "/footer/blogArticles/footerBlog001", model:[blogId:'001', suggestedBlogs:[]])}
    def footerBlog002(){render (view: "/footer/blogArticles/footerBlog002", model:[blogId:'002', suggestedBlogs:[]])}
    def footerBlog003(){render (view: "/footer/blogArticles/footerBlog003", model:[blogId:'003', suggestedBlogs:[]])}
    def footerBlog004(){render (view: "/footer/blogArticles/footerBlog004", model:[blogId:'004', suggestedBlogs:[]])}
    def footerBlog005(){render (view: "/footer/blogArticles/footerBlog005", model:[blogId:'005', suggestedBlogs:[]])}
}
