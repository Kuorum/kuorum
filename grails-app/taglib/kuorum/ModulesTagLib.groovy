package kuorum

import kuorum.core.model.search.Pagination
import kuorum.project.Project
import kuorum.post.Post
import kuorum.project.ProjectVote
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.BasicPersonalDataCommand

class ModulesTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    def projectService
    def springSecurityService
    def postService

    private static final Long NUM_RECOMMENDED_POST=3

    static namespace = "modulesUtil"

    def projectVotes={attrs ->
        Project project = attrs.project
        Boolean social = Boolean.parseBoolean(attrs.social?:"false")
        Boolean title = Boolean.parseBoolean(attrs.title?:"false")
        BasicPersonalDataCommand basicPersonalDataCommand = attrs.basicPersonalDataCommand?:new BasicPersonalDataCommand()
        ProjectVote userVote = null
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            userVote = projectService.findProjectVote(project,user)
        }
        Integer necessaryVotesForKuorum = projectService.necessaryVotesForKuorum(project)
        out << render (template:"/project/projectVotesModule", model:[project:project,userVote:userVote,necessaryVotesForKuorum:necessaryVotesForKuorum, social:social, title:title, basicPersonalDataCommand:basicPersonalDataCommand])
    }

    def projectActivePeople={attrs ->
        Project project = attrs.project
        List<KuorumUser> activePeopleOnProject = projectService.activePeopleOnProject(project)
        if (activePeopleOnProject)
            out << render (template:'/modules/activePeopleOnProject', model: [users: activePeopleOnProject])
    }

    def recommendedPosts={attrs ->
        KuorumUser user = null
        String specialCssClass = attrs.specialCssClass
        Boolean showAsHome = attrs.showAsHome?:Boolean.FALSE;
        Project project = attrs.project //Not necessary
        Pagination pagination = attrs.numPost?new Pagination(max:Long.parseLong(attrs.numPost)):new Pagination(max:NUM_RECOMMENDED_POST)
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user, project, pagination)
        if (recommendedPost && ! showAsHome){
            String title = attrs.title?:message(code:"modules.recommendedPosts.title")
            out << render(template: '/modules/recommendedPosts', model:[recommendedPost:recommendedPost, title:title,specialCssClass:specialCssClass])
        }else if (recommendedPost && showAsHome){
            out << render(template: '/dashboard/landingPageModules/relevantPosts', model:[recommendedPost:recommendedPost])
        }
    }

    def lastCreatedPosts={attrs ->
        String specialCssClass = attrs.specialCssClass
        Project project = attrs.project //Not necessary
        Pagination pagination = attrs.numPost?new Pagination(max:Long.parseLong(attrs.numPost)):new Pagination(max:NUM_RECOMMENDED_POST)

        List<Post> lastCreatedPost = postService.lastCreatedPosts(pagination, project)
        if (lastCreatedPost){
            String title = attrs.title?:message(code:"modules.lastCreatedPost.title")
            out << render(template: '/modules/recommendedPosts', model:[recommendedPost:lastCreatedPost, title:title,specialCssClass:specialCssClass])
        }
    }

    def delayedModule={attrs ->

        def params = attrs.params
        def mapping = attrs.mapping
        def elementId = attrs.elementId

        def link = createLink(mapping:mapping, params: params)

        out << """
                <script>
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
                </script>
                <div id="${elementId}" class="hidden"></div>
        """
    }
}
