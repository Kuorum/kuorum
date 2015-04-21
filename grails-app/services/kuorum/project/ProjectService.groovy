package kuorum.project

import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.RegionService
import kuorum.ShortUrlService
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.VoteType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchPolitician
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.solr.SolrResults
import kuorum.files.FileService
import kuorum.notifications.NotificationService
import kuorum.post.Post
import kuorum.solr.IndexSolrService
import kuorum.solr.SearchSolrService
import kuorum.users.GamificationService
import kuorum.users.KuorumUser
import kuorum.util.Order
import kuorum.web.commands.ProjectCommand
import org.bson.types.ObjectId

@Transactional
class ProjectService {

    IndexSolrService indexSolrService
    SearchSolrService searchSolrService
    GamificationService gamificationService
    ShortUrlService shortUrlService
    FileService fileService
    RegionService regionService
    NotificationService notificationService

    def grailsApplication

    /**
     * Find the project associated to the #hashtag
     *
     * Return null if not found
     *
     * @param hashtag
     * @return Return null if not found
     */
    Project findProjectByHashtag(String hashtag) {
        if (!hashtag){
            return null;
        }
        Project.findByHashtag(hashtag)
    }


    ProjectVote findProjectVote(Project project, KuorumUser user){
        ProjectVote.findByProjectAndKuorumUser(project, user)
    }

    /**
     * An user votes a project and generates all associated events
     *
     * @param project
     * @param user
     * @param voteType
     * @return
     */

    ProjectVote voteProject(Project project, KuorumUser user, VoteType voteType){
//        if (!regionService.isRelevantRegionForUser(user, project.region)){
//            throw new KuorumException("Votando una ley que no es de su region (user: ${user.id}, region:${project.region})")
//        }
        ProjectVote projectVote = ProjectVote.findByKuorumUserAndProject(user, project)
        if (projectVote){
            projectVote = changeProjectVote(project, user, voteType, projectVote)
        }else if (!projectVote){
            projectVote = createProjectVote(project,user,voteType)
            gamificationService.projectVotedAward(user, project)
        }
        projectVote
    }

    private ProjectVote changeProjectVote(Project project, KuorumUser user, VoteType voteType, ProjectVote projectVote){
        VoteType orgVoteType = projectVote.voteType
        if (orgVoteType != voteType){
            projectVote.voteType = voteType
            projectVote.personalData = user.personalData
            if (!projectVote.save()){
                throw KuorumExceptionUtil.createExceptionFromValidatable(projectVote, "Error salvando el projectVote")
            }
            if (isUserVoteRelevant(user, project)){
                switch (orgVoteType){
                    case VoteType.POSITIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.yes':-1]]); break;
                    case VoteType.ABSTENTION:   Project.collection.update([_id:project.id],['$inc':['peopleVotes.abs':-1]]); break;
                    case VoteType.NEGATIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.no':-1]]); break;
                    default: break;
                }

                switch (voteType){
                    case VoteType.POSITIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.yes':1]]); break;
                    case VoteType.ABSTENTION:   Project.collection.update([_id:project.id],['$inc':['peopleVotes.abs':1]]); break;
                    case VoteType.NEGATIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.no':1]]); break;
                    default: break;
                }
                project.refresh()
            }
        }
        projectVote
    }
    private ProjectVote createProjectVote(Project project, KuorumUser user, VoteType voteType){
        ProjectVote projectVote = new ProjectVote()
        projectVote.project = project
        projectVote.kuorumUser = user
        projectVote.voteType = voteType
        projectVote.personalData = user.personalData
        if (!projectVote.save()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(projectVote, "Error salvando el projectVote")
        }
        if (isUserVoteRelevant(user, project)){
            switch (voteType){
                case VoteType.POSITIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.yes':1,'peopleVotes.total':1]]); break;
                case VoteType.ABSTENTION:   Project.collection.update([_id:project.id],['$inc':['peopleVotes.abs':1,'peopleVotes.total':1]]); break;
                case VoteType.NEGATIVE:     Project.collection.update([_id:project.id],['$inc':['peopleVotes.no':1,'peopleVotes.total':1]]); break;
                default: break;
            }
            project.refresh()
        }
        projectVote
    }

    Boolean isUserVoteRelevant(KuorumUser user, Project project){
        Region userRegion = regionService.findUserRegion(user)
        userRegion.iso3166_2.startsWith(project.region.iso3166_2)
    }

    Project saveAndCreateNewProject(Project project, KuorumUser user){
//        project.institution = user.institution
        project.region = user.politicianOnRegion
        project.owner = user

        project.shortUrl = shortUrlService.shortUrl(project)

        if(project.image){
            project.image.alt = project.hashtag
            project.image.save()
            fileService.convertTemporalToFinalFile(project.image)
        }

        if(project.pdfFile){
            fileService.convertTemporalToFinalFile(project.pdfFile)
        }

        project.availableStats = project.availableStats?:false
        calculateProjectRelevance(project)
        if (!project.save()){
           throw KuorumExceptionUtil.createExceptionFromValidatable(project, "Error salvando el proyecto")
        }
        project
    }

    Project updateProject(Project project){
        if (!project.shortUrl){
            //TODO: Quitar cuando todas las leyes de la antigua web hayan sido editadas.
            project.shortUrl = shortUrlService.shortUrl(project)
        }
        if (project.image){
            project.image.alt = project.hashtag
            project.image.save()
        }
        project.availableStats = project.availableStats?:Boolean.FALSE //Por si es nulo
        fileService.convertTemporalToFinalFile(project.image)

        calculateProjectRelevance(project)
        //Transaction only with atomic operation on mongo
        // If someone votes while someone saves the project it is possible to lose data for overwriting
        project.mongoUpdate()
        indexSolrService.index(project)
        project
    }

    Project addedNewPost(Project project, Post post){
        Project.collection.update([_id:project.id],['$inc':['peopleVotes.numPosts':1]]);
        project.refresh();
    }

    private void calculateProjectRelevance(Project project){
        switch (project.status){
            case ProjectStatusType.OPEN:
                project.relevance = 1
                break;
            case ProjectStatusType.APPROVED:
            case ProjectStatusType.REJECTED:
                project.relevance = 0
                break;
        }
    }

    Project publish(Project project){
//        Project.collection.update([_id:project.id], ['$set':[published:Boolean.TRUE, publishDate:new Date()]])
//        project.refresh()
        if (!project.published){
            project.published = true
            project.publishDate = new Date()
            project.save()
            indexSolrService.index(project)
            notificationService.sendProjectPublishNotification(project)
        }
        project
    }

    Project unpublish(Project project){
        Project.collection.update([_id:project.id], ['$set':[published:Boolean.FALSE]])
        project.refresh()
        project
    }

    Project closeProject(Project project){
        Project.collection.update([_id:project.id], ['$set':[open:Boolean.FALSE]])
        project.refresh()
        calculateProjectRelevance(project)
//        indexSolrService.delete(project)
    }

    Integer necessaryVotesForKuorum(Project project){
        Math.max(grailsApplication.config.kuorum.milestones.kuorum - project.peopleVotes.total, 0)
    }

    List<KuorumUser> activePeopleOnProject(Project project){
        Post.collection.distinct('owner',[project:project.id]).collect{KuorumUser.get(it)}
    }

    List<Project> recommendedProjects(Pagination pagination){ recommendedProjects(null,pagination)}
    List<Project> recommendedProjects(KuorumUser user = null, Pagination pagination = new Pagination()){
        //TODO: Improve
        Project.createCriteria().list(max:pagination.max, offset:pagination.offset){
            order("peopleVotes.total","asc")
        }
    }

    List<Project> relevantProjects( Pagination pagination){ relevantProjects(null, pagination) }
    List<Project> relevantProjects(KuorumUser user, Pagination pagination = new Pagination()){
        //TODO: Improve
        //TODO: THINK IF IS POSSIBLE TO GET RELEVANT PROJECTS WITHOUT COUNTRY
        relevantProjects(user, null, pagination)
    }
    List<Project> relevantProjects(KuorumUser user, Region region, Pagination pagination = new Pagination()){
        //TODO: Improve
//        def res = Project.createCriteria().list(max:pagination.max, offset:pagination.offset){
////            eq("status", ProjectStatusType.OPEN)
//            eq("published", Boolean.TRUE)
//            if (region) eq("region._id", region.id)
//            and{
//                order('relevance', 'desc')
//                order('id','desc')
//            }
//        }
//        res
        SearchProjects searchProjects = new SearchProjects();
        searchProjects.regionName = region?.name;
        searchProjects.max = pagination.max
        searchProjects.offset = pagination.offset
        SolrResults solrResults = searchSolrService.searchProjects(searchProjects);
        solrResults.elements.collect{Project.get(new ObjectId(it.id))}
    }

    /**
     * Check the status of a project by the giving date. If the deadline of the project is less than the giving date, the
     * status of the project change to CLOSE.
     * @param date The date to compare with the deadline.
     */
    void checkProjectsStatus(Date date){
        List<Project> projectList = Project.where{
            status == ProjectStatusType.OPEN
            deadline < date
        }.list()
        projectList.each{Project project ->
            project.status = ProjectStatusType.CLOSE
            calculateProjectRelevance(project)
            project.save()
            indexSolrService.index(project)
        }
    }

    List<Project> politicianProjects(KuorumUser politician, Pagination pagination = new Pagination()){
        def resultProjectSearch = search(politician, 'dateCreated', Order.DESC, true, pagination.offset, pagination.max)
        resultProjectSearch.projects?:[] //Not returns null
    }
    Long countPoliticianProjects(KuorumUser politician){
        Project.countByOwnerAndPublished(politician,true)
    }

    /*
    *
    * Search the projects of a user and order all of them
    * @param user The user whose projects will be ordered
    * @param sort The sort param to order the projects
    * @param order The order params to order the projects
    * @param projectPublished It can be null (All projects will be shown), true (Only published projects will be shown) or false (Only projects which aren't published will be shown)
    * @param offset It is necessary to pagination
    * @param max The max projects which will appear in the pagination
    *
    * @return The projects of a user ordered by the sort and order chosen.
    */
    Map search(KuorumUser user, String sort, Order orderProject, Boolean projectPublished = null, Long offset = 0, Integer max = 10){
        List <Project> projects = Project.createCriteria().list() {
            if (projectPublished != null)  eq 'published', projectPublished
            eq 'owner', user
            if(sort == 'dateCreated')
                order sort, orderProject.value
        }
        if(sort != 'dateCreated'){
            projects.sort{
                switch (sort){
                    case('peopleVotes'):
                        if(orderProject == Order.ASC){
                            it?.peopleVotes?.total
                        }else{
                            -it?.peopleVotes?.total
                        }
                        break
                    case('peopleVoteYes'):
                        if(orderProject == Order.ASC){
                            it?.peopleVotes?.total?(it?.peopleVotes?.yes*100)/it?.peopleVotes?.total:0
                        }else{
                            -(it?.peopleVotes?.total?(it?.peopleVotes?.yes*100)/it?.peopleVotes?.total:0)
                        }
                        break
                    case('numPosts'):
                        if(orderProject == Order.ASC){
                            it?.peopleVotes?.numPosts
                        }else{
                            -it?.peopleVotes?.numPosts
                        }
                        break
                    default:
                        break
                }
            }
        }
        if(projects.size() >= (offset + max)){
            [projects: projects[offset..<(offset + max)]]
        }else if(projects.size() > offset){
            [projects: projects[offset..-1]]
        }else{
            [projects: null]
        }
    }

    /**
     * Assign files (object KuorumFile) to a ProjectCommand and to a Project
     * @param command The command to update
     * @param project The project to update
     */
    void assignFilesToCommandAndProject(ProjectCommand command, Project project, KuorumUser user){
        if(command.photoId && (!command.fileType || command.fileType==FileType.IMAGE)){
            KuorumFile image = KuorumFile.get(new ObjectId(command.photoId))
            project.image = image
        }

        if(command.videoPost && command.fileType==FileType.YOUTUBE){
            //KuorumFile urlYoutube = KuorumFile.get(new ObjectId(command.urlYoutubeId))
            if (command.videoPost != project.urlYoutube?.url){
                KuorumFile urlYoutubeFile = fileService.createYoutubeKuorumFile(command.videoPost, user)
                project.urlYoutube = urlYoutubeFile
            }
        }
        if (command.fileType==FileType.YOUTUBE){
            fileService.convertFinalFileToTemporalFile(project.image)
            project.image = null
        }else{
            fileService.convertFinalFileToTemporalFile(project.urlYoutube)
            project.urlYoutube = null
        }

        if(command.pdfFileId){
            KuorumFile pdfFile = KuorumFile.get(new ObjectId(command.pdfFileId))
            project.pdfFile = pdfFile
        }
    }

    /**
     * Save a projectUpdate and add a update to a project.
     * @param projectUpdate The projectUpdate to save
     * @param project The project
     * @return A map containing the message (errors or OK if everything is OK) and the object of ProjectUpdate created
     */
    Map addProjectUpdate(ProjectUpdate projectUpdate, Project project){
        Map result = [message:'']
        if(projectUpdate.validate() && !projectUpdate.hasErrors()){
            project.updates.add(projectUpdate)
            project.save(failOnError: true)
            result.projectUpdate = projectUpdate
            result.message = 'OK'
        } else {
            result.message = projectUpdate.errors
        }

        result
    }
}
