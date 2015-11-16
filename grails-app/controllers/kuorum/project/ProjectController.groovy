package kuorum.project

import com.sun.swing.internal.plaf.basic.resources.basic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.CommissionType
import kuorum.core.model.project.ProjectBasicStats
import kuorum.core.model.project.ProjectRegionStats
import kuorum.core.model.VoteType
import kuorum.core.model.gamification.GamificationElement
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.solr.SolrProjectsGrouped
import kuorum.core.model.solr.SolrType
import kuorum.files.FileService
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.web.commands.ProjectCommand
import kuorum.web.commands.ProjectUpdateCommand
import kuorum.web.commands.profile.BasicPersonalDataCommand
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
    def kuorumUserService

    FileService fileService

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def create() {
        projectModel(new ProjectCommand(), null)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def save(ProjectCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

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
        projectService.assignFilesToCommandAndProject(command, project, user)
        project = projectService.saveAndCreateNewProject(project, user)
        fileService.deleteTemporalFiles(user)
        Boolean isDraft =params.isDraft? Boolean.parseBoolean(params.isDraft):false
        if (isDraft){
            flash.message=message(code:'admin.createProject.success', args: [project.hashtag])
        }else{
            projectService.publish(project)
            flash.message=message(code:'admin.publishProject.success', args: [project.hashtag])
        }
        redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def edit(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        ProjectCommand projectCommand = new ProjectCommand()
        projectCommand.commissions = project.commissions
        projectCommand.hashtag = project.hashtag
        projectCommand.photoId = project.image?.id
        projectCommand.videoPost = project.urlYoutube?.url
        projectCommand.pdfFileId = project.pdfFile?.id
        projectCommand.description = project.description
        projectCommand.deadline = project.deadline
        projectCommand.shortName = project.shortName

        projectModel(projectCommand, project)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def update(ProjectCommand command){
        Project project = projectService.findProjectByHashtag(params.hashtag?.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        command.hashtag = project.hashtag //El parametro de la URL es más importante que el del formulario y lo sobreescribe
        if(!command.validate()){
            render view:'/project/edit', model: projectModel(command, project)
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser owner = project.owner
        if (owner != user && !SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
        project.commissions = command.commissions
        project.description = command.description
        project.deadline = command.deadline
        project.shortName = command.shortName
        projectService.assignFilesToCommandAndProject(command, project, owner)
        projectService.updateProject(project)
        fileService.deleteTemporalFiles(user)
        Boolean isDraft =params.isDraft? Boolean.parseBoolean(params.isDraft):false
        if (isDraft){
            flash.message=message(code:'admin.createProject.success', args: [project.hashtag])
        }else{
            projectService.publish(project)
            flash.message=message(code:'admin.publishProject.success', args: [project.hashtag])
        }
        redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
    }

    private def projectModel(ProjectCommand command, Project project){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Region region = user?.professionalDetails?.region
        if (!region){
            region = project?.region
        }
        if (!region){
            region = Region.findByIso3166_2("EU-ES")
            log.warn("Usuario ${user} sin region de politico. Se pone españa")
        }
        [project:project, command:command, region:region]

    }

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
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

    @Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
    def addProjectUpdate(ProjectUpdateCommand projectUpdateCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (projectUpdateCommand.hasErrors()){
            render view:'/project/createProjectUpdate', model: [projectUpdateCommand: projectUpdateCommand, project: project]
            return
        }

        ProjectUpdate projectUpdate = new ProjectUpdate()
        projectUpdate.dateCreated = new Date()
        bindData(projectUpdate, projectUpdateCommand.properties)
        if(projectUpdateCommand.photoId){
            projectUpdate.image = KuorumFile.get(new ObjectId(projectUpdateCommand.photoId))
            fileService.convertTemporalToFinalFile(projectUpdate.image)
        }

        if(projectUpdateCommand.videoPost){
            KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(projectUpdateCommand.videoPost, user)
            projectUpdate.urlYoutube = urlYoutubeFile
        }

        Map result = projectService.addProjectUpdate(projectUpdate, project)
        fileService.deleteTemporalFiles(user)
        if(result.message == 'OK'){
            flash.message=message(code:'admin.createProjectUpdate.success', args: [project.hashtag])
            redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
        } else {
            render view:'/project/createProjectUpdate', model: [projectUpdateCommand: projectUpdateCommand, project: project]
        }
    }

    def index(){
        def paramsUrl = [type:SolrType.PROJECT]
        if (params.commission){
            CommissionType commissionType = recoverCommissionByTranslatedName(request.locale, params.commission)
            paramsUrl.put('commissionType', commissionType)
        }
        if (params.regionName){
            paramsUrl.put('regionName',params.regionName)
        }

        redirect(mapping:'searcherSearch',params: paramsUrl, permanent: true)
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
        List<Post> posts = postService.projectPosts(project,pagination)
        Long numPosts = postService.countProjectPosts(project)
        List<Post> victories = postService.projectVictories(project)
        Long numVictories = postService.countProjectVictories(project)
        List<Post> defends = postService.projectDefends(project)
        Long numDefends = postService.countProjectDefends(project)
        ProjectVote userVote = null;
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            userVote = projectService.findProjectVote(project,user)
        } else {
            if(!project.published){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            }
        }
        ProjectBasicStats projectStats = projectStatsService.calculateProjectStats(project)
        ProjectRegionStats regionStats = projectStatsService.calculateRegionStats(project)

        //Objeto usado para la creacion del formulario de datos básicos de una persona para que pueda votar
        BasicPersonalDataCommand basicPersonalDataCommand = new BasicPersonalDataCommand();
        if (flash?.basicPersonalDataCommand){
            basicPersonalDataCommand = flash?.basicPersonalDataCommand
        }

        [
                project:project,
                projectStats:projectStats,
                regionStats:regionStats,
                posts: posts,
                numPosts:numPosts,
                seeMorePosts:posts.size() < numPosts,
                victories:victories,
                numVictories:numVictories,
                seeMoreVictories:victories.size() < numVictories,
                defends:defends,
                numDefends:numDefends,
                seeMoreDefends:defends.size() < numDefends,
                userVote:userVote,
                basicPersonalDataCommand:basicPersonalDataCommand
        ]

    }

    def listClucksProject(Pagination pagination){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Post> posts = postService.projectPosts(project,pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<pagination.max}")
        render template: "/cluck/liPosts", model:[posts:posts]

    }

    def listClucksProjectDefends(Pagination pagination){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def posts = postService.projectDefends(project,pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<pagination.max}")
        render template: "/cluck/liPosts", model:[posts:posts]
    }

    def listClucksProjectVictories(Pagination pagination){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        def posts = postService.projectVictories(project,pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<pagination.max}")
        render template: "/cluck/liPosts", model:[posts:posts]
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN', 'ROLE_PREMIUM', 'ROLE_POLITICIAN'])
    def voteProject(String hashtag){
        VoteType voteType = VoteType.valueOf(params.voteType)
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project || !voteType){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        //Ñapilla para saber si el voto es nuevo
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

    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED', 'ROLE_USER'])
    def voteProjectAsNonCompleteUser(BasicPersonalDataCommand basicPersonalDataCommand){
        String hashtag = params.hashtag
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        if (!project){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        if (basicPersonalDataCommand.hasErrors()){
            flash.basicPersonalDataCommand = basicPersonalDataCommand
            redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.personalData.year=basicPersonalDataCommand.year
        user.personalData.country=basicPersonalDataCommand.country
        user.personalData.gender=basicPersonalDataCommand.gender
        user.personalData.province=basicPersonalDataCommand.province
        user.personalData.postalCode=basicPersonalDataCommand.postalCode
        kuorumUserService.updateUser(user)
        if (SpringSecurityUtils.ifAnyGranted("ROLE_INCOMPLETE_USER")){
            //El usuario no ha confirmado el email
            redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
        }else if (basicPersonalDataCommand.voteType){
            ProjectVote projectVote = projectService.voteProject(project, user, basicPersonalDataCommand.voteType)
            flash.message = g.message(code: "project.vote.success")
            redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
        }else{
            //NO hay voteProyect se ha clickado sobre la creacion de una propuesta
            redirect mapping:"postCreate", params:project.encodeAsLinkProperties()
        }
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
