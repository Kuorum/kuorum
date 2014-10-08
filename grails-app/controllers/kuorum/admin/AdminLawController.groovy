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
        [
                command:new LawCommand(),
                institutions:Institution.findAll(),
                regions:Region.findAll()
        ]
    }

    def saveLaw(LawCommand command){
        if (Law.findByHashtag(command.hashtag)){
            command.errors.rejectValue("hashtag","notUnique")
        }
        if (command.hasErrors()){
            render view:'/adminLaw/createLaw', model:         [
                    command:command,
                    institutions:Institution.findAll(),
                    regions:Region.findAll()
            ]
            return
        }
        Law law = new Law(command.properties)
        law.region = command.region
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
                institutions:Institution.findAll(),
                regions:Region.findAll()
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
                    institutions:Institution.findAll(),
                    regions:Region.findAll()
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

    def publishLaw(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        lawService.publish(law)
        flash.message=message(code: "admin.editLaw.publish.success", args: [law.hashtag])
        redirect mapping:"adminEditLaw", params:law.encodeAsLinkProperties()
    }

    def unPublishLaw(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        lawService.unpublish(law)
        flash.message=message(code: "admin.editLaw.unPublish.success", args: [law.hashtag])
        redirect mapping:"adminEditLaw", params:law.encodeAsLinkProperties()
    }
}
