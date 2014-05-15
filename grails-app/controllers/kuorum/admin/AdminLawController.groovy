package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.web.commands.LawCommand
import org.bson.types.ObjectId

@Secured(['ROLE_ADMIN'])
class AdminLawController  extends  AdminController{

    def lawService
    def fileService
    def springSecurityService

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
            return
        }
        Law law = new Law(command.properties)
        law.region = command.institution.region
        KuorumFile image = KuorumFile.get(new ObjectId(command.photoId))
        law.image = image
        law = lawService.saveAndCreateNewLaw(law)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        fileService.deleteTemporalFiles(user)
        flash.message=message(code:'admin.createLaw.success', args: [law.hashtag])
        redirect mapping:"lawShow", params:law.encodeAsLinkProperties()
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
