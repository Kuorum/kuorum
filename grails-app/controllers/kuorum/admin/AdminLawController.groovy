package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
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
        [
                command:new LawCommand(),
                institutions:Institution.findAll()
        ]
    }

    def saveLaw(LawCommand command){
        if (Law.findByHashtag(command.hashtag)){
            command.errors.rejectValue("hashtag","notUnique")
        }
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
        LawCommand command = new LawCommand()
        command.properties.each {k,v ->
            if (k!="class" && law.hasProperty(k))
                command."$k" = law."$k"
        }
        command.photoId = law.image.id
        [
                law:law,
                command:command,
                institutions:Institution.findAll()
        ]
    }

    def updateLaw(LawCommand command){
        Law law = lawService.findLawByHashtag(params.hashtag.encodeAsHashtag())
        command.hashtag = command.hashtag.encodeAsHashtag()
        command.validate()
        if (command.hasErrors()){
            render view:'/adminLaw/editLaw', model:         [
                    law:law,
                    command:command,
                    institutions:Institution.findAll()
            ]
            return
        }
        command.properties.each {k,v -> if (k!="class" && law.hasProperty(k)) {law."$k" = command."$k"}}
        if (command.photoId != law.image.id){
            KuorumFile image = KuorumFile.get(new ObjectId(command.photoId))
            law.image = image
        }
        law.region = command.institution.region
        lawService.updateLaw(law)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        fileService.deleteTemporalFiles(user)
        flash.message=message(code: "law.update.success", args: [law.hashtag])
        redirect mapping:"lawShow", params:law.encodeAsLinkProperties()
    }

    def unpublishedLaws(){
        [laws:Law.findAllByPublished(false)]
    }
}
