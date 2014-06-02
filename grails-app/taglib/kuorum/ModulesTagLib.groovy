package kuorum

import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.law.LawVote
import kuorum.post.Post
import kuorum.users.KuorumUser

class ModulesTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    def lawService
    def springSecurityService
    def postService

    private static final Long NUM_RECOMMENDED_POST=3

    static namespace = "modulesUtil"

    def lawVotes={attrs ->
        Law law = attrs.law
        Boolean social = Boolean.parseBoolean(attrs.social?:"false")
        Boolean title = Boolean.parseBoolean(attrs.title?:"false")
        LawVote userVote
        if (springSecurityService.isLoggedIn()){
            log.info("User Logged IN")
            log.info("User Logged ID = ${springSecurityService.principal.id}")
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            log.info("User = ${user.name}")
            userVote = lawService.findLawVote(law,user)
            log.info("Vote = ${userVote.id}")
        }
        Integer necessaryVotesForKuorum = lawService.necessaryVotesForKuorum(law)
        out << render (template:"/law/lawVotesModule", model:[law:law,userVote:userVote,necessaryVotesForKuorum:necessaryVotesForKuorum, social:social, title:title])
    }

    def lawActivePeople={attrs ->
        Law law = attrs.law
        List<KuorumUser> activePeopleOnLaw = lawService.activePeopleOnLaw(law)
        if (activePeopleOnLaw)
            out << render (template:'/modules/activePeopleOnLaw', model: [users: activePeopleOnLaw])
    }

    def recommendedPosts={attrs ->
        KuorumUser user = null
        String specialCssClass = attrs.specialCssClass
        Law law = attrs.law //Not necessary
        Pagination pagination = attrs.numPost?new Pagination(max:Long.parseLong(attrs.numPost)):new Pagination(max:NUM_RECOMMENDED_POST)
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }
        List<Post> recommendedPost = postService.recommendedPosts(user, law, pagination)
        if (recommendedPost){
            String title = attrs.title?:message(code:"modules.recommendedPosts.title")
            out << render(template: '/modules/recommendedPosts', model:[recommendedPost:recommendedPost, title:title,specialCssClass:specialCssClass])
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
