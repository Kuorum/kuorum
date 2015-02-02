package kuorum

import kuorum.project.ProjectService

class CheckProjectsStatusJob {
    static triggers = {
      simple repeatInterval: 60000l // execute job once in 60 seconds
    }

    ProjectService projectService

    def execute() {
        projectService.checkProjectsStatus(new Date())
    }
}
