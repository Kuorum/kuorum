package kuorum.Law

import kuorum.law.Law

import javax.servlet.http.HttpServletResponse

class LawController {

    def lawService

    static scaffold = true

    def show(String hashtag){
        Law law = lawService.findLawByHashtag("#${hashtag}")
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        [lawInstance:law]

    }
}
