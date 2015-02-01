package kuorum.project

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.project.ProjectRegionStats
import kuorum.core.model.VoteType
import kuorum.core.model.gamification.GamificationElement
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.solr.SolrProjectsGrouped
import kuorum.project.AcumulativeVotes
import kuorum.project.Project
import kuorum.project.ProjectVote
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants

import javax.servlet.http.HttpServletResponse

class ProjectController {

    def projectService
    def projectStatsService
    def postService
    def cluckService
    def springSecurityService
    def gamificationService
    def searchSolrService

    def index(){
        //TODO: Think pagination.This page is only for google and scrappers
        SearchProjects searchParams = new SearchProjects(max: 1000)
        searchParams.commissionType = recoverCommissionByTranslatedName(request.locale, params.commission)
        def groupProjects =[:]
        if (params.institutionName){
            searchParams.institutionName = params.institutionName.decodeKuorumUrl()
            List<SolrProjectsGrouped> projectsPerInstitution = searchSolrService.listProjects(searchParams)
            if (projectsPerInstitution){
                searchParams.institutionName = projectsPerInstitution.elements[0][0].institutionName
            }
            groupProjects.put(searchParams.institutionName  , projectsPerInstitution)
        }else{
            Institution.list().each {
                searchParams.institutionName = it.name
                List<SolrProjectsGrouped> projectsPerInstitution = searchSolrService.listProjects(searchParams)
                if (projectsPerInstitution)
                    groupProjects.put("${searchParams.institutionName}" , projectsPerInstitution)
            }
        }
        [groupProjects:groupProjects]
    }

    private CommissionType recoverCommissionByTranslatedName(Locale locale, String searchedCommission){
        //Funcion mega �apa para solucionar el par�metro de la url internacionalizado
        def commissionsTranslated = [:]
        CommissionType.values().each{commission ->
            String translatedCommission = message(code:"${CommissionType.class.name}.${commission}", locale: locale, default: 'XXXX')
            commissionsTranslated.put(translatedCommission.toLowerCase().encodeAsKuorumUrl(),commission)
        }
        commissionsTranslated."${searchedCommission?.toLowerCase()}"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showSecured(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
//        response.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY)
        redirect(mapping: 'projectShow', params:project.encodeAsLinkProperties(), permanent: true)
    }

    def show(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        Pagination pagination = new Pagination()
        def clucks = cluckService.projectClucks(project,pagination)
        List<Post> victories = postService.projectVictories(project)
        ProjectVote userVote = null;
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            userVote = projectService.findProjectVote(project,user)
        }
        ProjectBasicStats projectStats = projectStatsService.calculateProjectStats(project)
        [project:project, clucks: clucks,victories:victories, seeMore:clucks.size() == pagination.max, projectStats:projectStats, userVote:userVote]

    }

    def listClucksProject(Pagination pagination){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def clucks = cluckService.projectClucks(project,pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def voteProject(String hashtag){
        VoteType voteType = VoteType.valueOf(params.voteType)
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project || !voteType){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long numVotes = project.peopleVotes.total
        ProjectVote projectVote = projectService.voteProject(project, user, voteType)

        def newVote = false
        if (numVotes < project.peopleVotes.total){
            newVote = true
        }
        Integer necessaryVotesForKuorum = projectService.necessaryVotesForKuorum(project)
        def gamification = [
                title: "${message(code:'project.vote.gamification.title', args:[project.hashtag])}",
                text:"${message(code:'project.vote.gamification.motivationText', args:[project.hashtag])}",
                eggs:gamificationService.gamificationConfigVoteProject()[GamificationElement.EGG]?:0,
                plumes:gamificationService.gamificationConfigVoteProject()[GamificationElement.PLUME]?:0,
                corns:gamificationService.gamificationConfigVoteProject()[GamificationElement.CORN]?:0
        ]

        render ([
                newVote:newVote,
                necessaryVotesForKuorum:necessaryVotesForKuorum,
                voteType:projectVote.voteType.toString(),
                votes:project.peopleVotes,
                gamification:gamification
        ] as JSON)
    }

    def stats(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
//        Region region = Region.findByIso3166_2("EU-ES")
        Region region = project.region
        ProjectRegionStats stats = projectStatsService.calculateRegionStats(project, region)
        Integer necessaryVotesForKuorum = projectService.necessaryVotesForKuorum(project)
        [project:project, stats:stats, region:region, necessaryVotesForKuorum:necessaryVotesForKuorum]
    }

    private static final int MAP_YES = 1
    private static final int MAP_NO = 2
    private static final int MAP_ABS = 3
    def statsDataMap(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        Region spain = Region.findByIso3166_2("EU-ES")
        HashMap<String, AcumulativeVotes> statsPerProvince = projectStatsService.calculateProjectStatsPerSubRegions(project, spain)
        def map = [:]
        statsPerProvince.each {k,v ->
            def max = [v.yes,v.no, v.abs].max()
            if (v.abs == max)
                map.put(k,MAP_ABS)
            else if(v.no == v.yes)
                map.put(k, MAP_ABS)
            else if (v.no == max)
                map.put(k,MAP_NO)
            else if (v.yes == max)
                map.put(k,MAP_YES)
            else
                map.put(k,MAP_ABS)
        }
        render ([votation:[results:map]] as JSON)
    }

    def statsDataPieChart(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        Region region = Region.findByIso3166_2(params.regionIso3166)
        if (!project || !region){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        ProjectRegionStats stats = projectStatsService.calculateRegionStats(project, region)
        render stats as JSON
    }
}
