package kuorum

import kuorum.core.model.search.Pagination
import kuorum.project.Project
import kuorum.post.Post
import kuorum.project.ProjectVote
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.BasicPersonalDataCommand
import org.bson.types.ObjectId

class ModulesTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    def projectService
    def springSecurityService
    def postService
    def kuorumUserService

    private static final Long NUM_RECOMMENDED_POST = 3

    static namespace = "modulesUtil"

    def projectVotes = { attrs ->
        Project project = attrs.project
        Boolean social = Boolean.parseBoolean(attrs.social ?: "false")
        Boolean title = Boolean.parseBoolean(attrs.title ?: "false")
        BasicPersonalDataCommand basicPersonalDataCommand = attrs.basicPersonalDataCommand ?: new BasicPersonalDataCommand()
        ProjectVote userVote = null
        if (springSecurityService.isLoggedIn()) {
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            userVote = projectService.findProjectVote(project, user)
        }
        Integer necessaryVotesForKuorum = projectService.necessaryVotesForKuorum(project)
        out << render(template: "/project/projectVotesModule", model: [project: project, userVote: userVote, necessaryVotesForKuorum: necessaryVotesForKuorum, social: social, title: title, basicPersonalDataCommand: basicPersonalDataCommand])
    }

    def projectActivePeople = { attrs ->
        Project project = attrs.project
        List<KuorumUser> activePeopleOnProject = projectService.activePeopleOnProject(project)
        if (activePeopleOnProject)
            out << render(template: '/modules/activePeopleOnProject', model: [users: activePeopleOnProject])
    }

    def lastCreatedPosts = { attrs ->
        String specialCssClass = attrs.specialCssClass
        Project project = attrs.project //Not necessary
        Pagination pagination = attrs.numPost ? new Pagination(max: Long.parseLong(attrs.numPost)) : new Pagination(max: NUM_RECOMMENDED_POST)

        List<Post> lastCreatedPost = postService.lastCreatedPosts(pagination, project)
        if (lastCreatedPost) {
            String title = attrs.title ?: message(code: "modules.lastCreatedPost.title")
            out << render(template: '/modules/recommendedPosts', model: [recommendedPost: lastCreatedPost, title: title, specialCssClass: specialCssClass])
        }
    }

    def recommendedUsersList = { attrs ->
        Integer numUsers = attrs.numUsers as Integer
        KuorumUser user = attrs.user
        List<KuorumUser> recommendedUsers = kuorumUserService.recommendedUsers(user, new Pagination(max: numUsers))
        out << render(template: '/modules/users/recommendedUsersAsList', model: [users: recommendedUsers, showDeleteRecommendation: 'true'])
    }

    def delayedModule = { attrs ->

        def params = attrs.params
        def mapping = attrs.mapping
        def elementId = attrs.elementId

        def link = createLink(mapping: mapping, params: params)

        def delayScript =  """
                    \$(function(){
                        \$.ajax({
                            url: "${link}",
                            context: document.body
                        }).done(function(data) {
                            \$("#${elementId}" ).html( data);
                            \$("#${elementId}").hide()
                            \$("#${elementId}").removeClass('hidden')
                            \$("#${elementId}").show('slow')
                        });
                    });
        """
        out << "<div id=\"${elementId}\" class=\"hidden\"></div>"
        r.script( [:],tagInputScript)
    }

    private static final Integer MAX_LENGTH_TEXT = 250
    def shortText = { attrs ->
        Integer maxLength = attrs.maxLength!=null?Integer.parseInt(attrs.maxLength):MAX_LENGTH_TEXT
        String orgText = attrs.text.encodeAsRemovingHtmlTags()
        String text = orgText.substring(0, Math.min(orgText.length(), maxLength))


        if (text && text.length() < orgText.length()){
            text += " ..."
        }
        out << text

    }
}
