package kuorum.Law

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.Region
import kuorum.law.Law
import kuorum.web.commands.LawCommand

import javax.servlet.http.HttpServletResponse

class LawController {

    def lawService
    def cluckService

    static scaffold = true

    def index(){
        [lawInstanceList:Law.findAll()]
    }

    def show(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def clucks = cluckService.lawClucks(law)

        [lawInstance:law, clucks: clucks]

    }

    @Secured(['ROLE_ADMIN'])
    def create(){
        [   lawInstance:new LawCommand(),
            regions:Region.findAll(),
            institutions:Institution.findAll()
        ]
    }

    @Secured(['ROLE_ADMIN'])
    def save(LawCommand command){
        if (command.hasErrors()){
            def regions = Region.findAll()
            def institutions = Institution.findAll()
            render view: "create", model:[
                    lawInstance:command,
                    regions:Region.findAll(),
                    institutions:Institution.findAll()
            ]
            return
        }

        Law law = new Law(command.properties)
        lawService.saveLaw(law)
        redirect mapping:"lawShow", params:[hashtag:law.hashtag.decodeHashtag()]
    }

    @Secured(['ROLE_ADMIN'])
    def edit(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        LawCommand lawCommand = new LawCommand()
        lawCommand.properties.each {k,v -> lawCommand."$k" = law."$k"}
        [lawInstance:lawCommand,regions:Region.findAll()]
    }


    @Secured(['ROLE_ADMIN'])
    def update(LawCommand command){
        if (command.hasErrors()){
            render view: "edit", model:[lawInstance:command,regions:Region.findAll()]
        }
        Law law = lawService.findLawByHashtag(command.hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }

    }

    @Secured(['ROLE_ADMIN'])
    def publish(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        law = lawService.publish(law)
        render "publish"
    }

    @Secured(['ROLE_ADMIN'])
    def unpublish(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        law = lawService.unpublish(law)
        render "unpublish"
    }

}
