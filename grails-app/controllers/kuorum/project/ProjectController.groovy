package kuorum.project

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.project.ProjectRegionStats
import kuorum.core.model.VoteType
import kuorum.core.model.gamification.GamificationElement
import kuorum.core.model.project.ProjectUpdate
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.solr.SolrProjectsGrouped
import kuorum.files.FileService
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.util.Order
import kuorum.web.commands.ProjectCommand
import kuorum.web.commands.ProjectUpdateCommand
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

import javax.servlet.http.HttpServletResponse

class ProjectController {
    def kuorumMailService
    def projectService
    def projectStatsService
    def postService
    def cluckService
    def springSecurityService
    def gamificationService
    def searchSolrService

    FileService fileService

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def create() {
        projectModel(new ProjectCommand(), null)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def save(ProjectCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        projectService.assignUserRegionAndInstitutionToCommand(command, user)

        if(!command.validate()){
            render view:'/project/create', model: projectModel(command, null)
            return
        }

        if (Project.findByHashtag(command.hashtag)){
            command.errors.rejectValue("hashtag","notUnique")
        }
        if (command.hasErrors()){
            render view:'/project/create', model: projectModel(command, null)
            return
        }
        Project project = new Project(command.properties)
        projectService.assignFilesToCommandAndProject(command, project)
        project = projectService.saveAndCreateNewProject(project, false)
        fileService.deleteTemporalFiles(user)
        flash.message=message(code:'admin.createProject.success', args: [project.hashtag])
        redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def publish(ProjectCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        projectService.assignUserRegionAndInstitutionToCommand(command, user)

        if(!command.validate()){
            render view:'/project/create', model: projectModel(command, null)
            return
        }

        if (Project.findByHashtag(command.hashtag)){
            command.errors.rejectValue("hashtag","notUnique")
        }
        if (command.hasErrors()){
            render view:'/project/create', model: projectModel(command, null)
            return
        }
        Project project = new Project(command.properties)
        projectService.assignFilesToCommandAndProject(command, project)
        project = projectService.saveAndCreateNewProject(project, true)
        fileService.deleteTemporalFiles(user)
        flash.message=message(code:'admin.createProject.success', args: [project.hashtag])

//        List <KuorumUser> relatedUsers = projectService.searchRelatedUserToUserCommisions(project)
//        kuorumMailService.sendSavedProjectToRelatedUsers(relatedUsers,project)
        redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
    }

    private def projectModel(ProjectCommand command, Project project){
        def model = [:]
        if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            model = [
                    institutions:[user.institution],
                    regions:[user?.politicianOnRegion]
            ]
            command.region = model.regions[0]
//            command.owner = user
//            command.institution = user.institution
        }
        model << [project:project, command: command]
        model
    }

    def createProjectUpdate(String hashtag){
        if(hashtag){
            Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
            if(project){
                [projectUpdateCommand: new ProjectUpdateCommand(), project: project]
            } else {
                flash.message=message(code:'admin.createProjectUpdate.project.not.found', args: [hashtag])
                redirect mapping:"home"
            }
        } else {
            flash.message=message(code:'admin.createProjectUpdate.project.not.found', args: [hashtag])
            redirect mapping:"home"
        }
    }

    def addProjectUpdate(ProjectUpdateCommand projectUpdateCommand){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (projectUpdateCommand.hasErrors()){
            render view:'/project/createProjectUpdate', model: projectUpdateCommand
            return
        }

        ProjectUpdate projectUpdate = new ProjectUpdate()
        bindData(projectUpdate, projectUpdateCommand.properties)
        if(projectUpdateCommand.photoId){
            projectUpdate.image = KuorumFile.get(new ObjectId(projectUpdateCommand.photoId))

        }

        if(projectUpdateCommand.urlYoutubeId){
            projectUpdate.urlYoutube = KuorumFile.get(new ObjectId(projectUpdateCommand.urlYoutubeId))
        }

        Map result = projectService.addProjectUpdate(projectUpdate, project)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        fileService.deleteTemporalFiles(user)
        if(result.message == 'OK'){
            flash.message=message(code:'admin.createProjectUpdate.success', args: [project.hashtag])
            redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
        } else {
            render view:'/project/createProjectUpdate', model: projectUpdate
        }
    }

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
        ProjectRegionStats regionStats = projectStatsService.calculateRegionStats(project)
        [project:project, clucks: clucks,victories:victories, seeMore:clucks.size() == pagination.max, projectStats:projectStats, regionStats:regionStats, userVote:userVote]

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

    @Secured(['ROLE_POLITICIAN'])
    def ajaxShowProjectListOfUsers(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map projectsOrderListOfUser = projectService.search(user, params.sort, Order.findByValue(params.order), params.published as Boolean, params.offset as Integer, params.max as Integer)
        render template: "projects", model: [projects: projectsOrderListOfUser.projects, order: params.order, sort: params.sort, published: params.published, max: params.max, offset: params.offset]
    }

    @Secured(['ROLE_POLITICIAN'])
    def list(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String sort = '', order = ''
        Integer offset = 0
        Integer max = 10
        Map projectsOrderListOfUser = projectService.search(user, sort, Order.findByValue(order) , null, offset, max)

        [projects: projectsOrderListOfUser.projects, order: order, sort: sort, published: '', max: max, offset: offset]
    }
}
