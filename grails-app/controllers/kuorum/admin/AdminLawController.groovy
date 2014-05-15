package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.Region
import kuorum.law.Law
import kuorum.web.commands.LawCommand

@Secured(['ROLE_ADMIN'])
class AdminLawController  extends  AdminController{

    def lawService

    def createLaw() {
        Region spain = Region.findByIso3166_2("EU-ES")
        [
                command:new LawCommand(),
                institutions:Institution.findAll()
        ]
    }

    def saveLaw(LawCommand command){
        if (command.hasErrors()){
            render view:'/adminLaw/createLaw', model:         [
                    command:command,
                    institutions:Institution.findAll()
            ]
        }

    }

    def editLaw(String hashtag){
        Law law = lawService.findLawByHashtag(params.hashtag.encodeAsHashtag())

        [law:law]
    }

    def updateLaw(){

    }

    def unpublishedLaws(){
        [laws:Law.findAllByPublished(false)]
    }
}
