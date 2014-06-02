package kuorum.Law

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.Law.LawRegionStats
import kuorum.core.model.VoteType
import kuorum.core.model.gamification.GamificationElement
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchLaws
import kuorum.core.model.solr.SolrLawsGrouped
import kuorum.law.AcumulativeVotes
import kuorum.law.Law
import kuorum.law.LawVote
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants

import javax.servlet.http.HttpServletResponse

class LawController {

    def lawService
    def lawStatsService
    def postService
    def cluckService
    def springSecurityService
    def gamificationService
    def searchSolrService

    def index(){
        //TODO: Think pagination.This page is only for google and scrappers
        SearchLaws searchParams = new SearchLaws(max: 1000)
        searchParams.commissionType = recoverCommissionByTranslatedName(request.locale, params.commission)
        def groupLaws =[:]
        if (params.institutionName){
            searchParams.institutionName = params.institutionName.decodeKuorumUrl()
            List<SolrLawsGrouped> lawsPerInstitution = searchSolrService.listLaws(searchParams)
            if (lawsPerInstitution){
                searchParams.institutionName = lawsPerInstitution.elements[0][0].institutionName
            }
            groupLaws.put(searchParams.institutionName  , lawsPerInstitution)
        }else{
            Institution.list().each {
                searchParams.institutionName = it.name
                List<SolrLawsGrouped> lawsPerInstitution = searchSolrService.listLaws(searchParams)
                if (lawsPerInstitution)
                    groupLaws.put("${searchParams.institutionName}" , lawsPerInstitution)
            }
        }
        [groupLaws:groupLaws]
    }

    private CommissionType recoverCommissionByTranslatedName(Locale locale, String searchedCommission){
        //Funcion mega ñapa para solucionar el parámetro de la url internacionalizado
        def commissionsTranslated = [:]
        CommissionType.values().each{commission ->
            String translatedCommission = message(code:"${CommissionType.class.name}.${commission}", locale: locale, default: 'XXXX')
            commissionsTranslated.put(translatedCommission.toLowerCase().encodeAsKuorumUrl(),commission)
        }
        commissionsTranslated."${searchedCommission?.toLowerCase()}"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showSecured(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
//        response.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY)
        redirect(mapping: 'lawShow', params:law.encodeAsLinkProperties(), permanent: true)
    }

    def show(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        Pagination pagination = new Pagination()
        def clucks = cluckService.lawClucks(law,pagination)
        List<Post> victories = postService.lawVictories(law)
        [law:law, clucks: clucks,victories:victories, seeMore:clucks.size() == pagination.max]

    }

    def listClucksLaw(Pagination pagination){
        Law law = lawService.findLawByHashtag(params.hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def clucks = cluckService.lawClucks(law,pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def voteLaw(String hashtag){
        VoteType voteType = VoteType.valueOf(params.voteType)
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law || !voteType){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Long numVotes = law.peopleVotes.total
        LawVote lawVote = lawService.voteLaw(law, user, voteType)

        def newVote = false
        if (numVotes < law.peopleVotes.total){
            newVote = true
        }
        Integer necessaryVotesForKuorum = lawService.necessaryVotesForKuorum(law)
        def gamification = [
                title: "${message(code:'law.vote.gamification.title', args:[law.hashtag])}",
                text:"${message(code:'law.vote.gamification.motivationText', args:[law.hashtag])}",
                eggs:gamificationService.gamificationConfigVoteLaw()[GamificationElement.EGG]?:0,
                plumes:gamificationService.gamificationConfigVoteLaw()[GamificationElement.PLUME]?:0,
                corns:gamificationService.gamificationConfigVoteLaw()[GamificationElement.CORN]?:0
        ]

        render ([
                newVote:newVote,
                necessaryVotesForKuorum:necessaryVotesForKuorum,
                voteType:lawVote.voteType.toString(),
                votes:law.peopleVotes,
                gamification:gamification
        ] as JSON)
    }

    def stats(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        Region spain = Region.findByIso3166_2("EU-ES")
        LawRegionStats stats = lawStatsService.calculateRegionStats(law, spain)
        [law:law, stats:stats, region:spain]
    }

    def statsDataMap(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        if (!law){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        Region spain = Region.findByIso3166_2("EU-ES")
        HashMap<String, AcumulativeVotes> statsPerProvince = lawStatsService.calculateLawStatsPerSubRegions(law, spain)
        def map = [:]
        statsPerProvince.each {k,v ->
            def max = [v.yes,v.no, v.abs].max()
            if (v.no == max)
                map.put(k,2)
            else if (v.yes == max)
                map.put(k,1)
            else
                map.put(k,3)
        }
        render ([votation:[results:map]] as JSON)
    }

    def statsDataPieChart(String hashtag){
        Law law = lawService.findLawByHashtag(hashtag.encodeAsHashtag())
        Region region = Region.findByIso3166_2(params.regionIso3166)
        if (!law || !region){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        LawRegionStats stats = lawStatsService.calculateRegionStats(law, region)
        render stats as JSON
    }
}
