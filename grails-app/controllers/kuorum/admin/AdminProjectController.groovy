package kuorum.admin

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.web.commands.ProjectCommand
import org.bson.types.ObjectId

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class AdminProjectController  extends  AdminController{

    def projectService
    def fileService
    def springSecurityService

    private def projectModel(ProjectCommand command, Project project){
        def model = [:]
        if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            model = [
                    institutions:[user.institution],
                    regions:[user?.politicianOnRegion]
            ]
            command.region = model.regions[0]
            command.owner = user
            command.institution = user.institution
        }
        model << [project:project, command: command]
        model
    }

    def editProject(String hashtag){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        ProjectCommand command = new ProjectCommand()
        command.properties.each {k,v ->
            if (k!="class" && project.hasProperty(k))
                command."$k" = project."$k"
        }
        command.photoId = project.image.id
        projectModel(command, project)
    }

    def updateProject(ProjectCommand command){
        Project project = projectService.findProjectByHashtag(params.hashtag.encodeAsHashtag())
        command.hashtag = command.hashtag.encodeAsHashtag()
        command.validate()
        if (command.hasErrors()){
            render view:'/adminProject/editProject', model:projectModel(command, project)
            return
        }
        command.properties.each {k,v -> if (k!="class" && project.hasProperty(k)) {project."$k" = command."$k"}}
        if (command.photoId != project.image.id){
            KuorumFile image = KuorumFile.get(new ObjectId(command.photoId))
            project.image = image
        }
        project.region = command.institution.region
        projectService.updateProject(project)
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        fileService.deleteTemporalFiles(user)
        flash.message=message(code: "project.update.success", args: [project.hashtag])
        redirect mapping:"projectShow", params:project.encodeAsLinkProperties()
    }

    def unpublishedProjects(){
        if (SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN')){
            return [projects:Project.findAllByPublished(false)]
        }else if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
            KuorumUser user = springSecurityService.currentUser
            return [projects: Project.findAllByPublishedAndRegion(false, user.personalData.province)]
        }
    }

    def publishProject(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        projectService.publish(project)
        flash.message=message(code: "admin.editProject.publish.success", args: [project.hashtag])
        redirect mapping:"adminEditProject", params:project.encodeAsLinkProperties()
    }

    def unPublishProject(String hashtag){
        Project project = projectService.findProjectByHashtag(hashtag.encodeAsHashtag())
        projectService.unpublish(project)
        flash.message=message(code: "admin.editProject.unPublish.success", args: [project.hashtag])
        redirect mapping:"adminEditProject", params:project.encodeAsLinkProperties()
    }
}
