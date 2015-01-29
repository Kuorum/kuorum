package kuorum.project

import kuorum.Institution
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.model.CommissionType
import spock.lang.Specification

/**
 * Created by iduetxe on 4/03/14.
 */
class ProjectServiceIntegrationSpec extends Specification{

    def projectService
    def fixtureLoader

    def setup(){
        Region.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    void "test update a project"() {
        given: "A project"
        Project project = Project.findByHashtag("#leyAborto")
        when: "Updating the project"
        Region region = Region.findByIso3166_2("EU-ES")
        Institution institution = Institution.findAllByRegion(region).first()
        PoliticalParty politicalParty = PoliticalParty.list().find{it.institution == institution}
        String realName = "realName"
        String shortName = "shortName"
        def commissions = [CommissionType.AGRICULTURE, CommissionType.BUDGETS, CommissionType.CONSTITUTIONAL]

        project.shortName = shortName
        project.region = region
        project.institution = institution
        project.politicalParty = politicalParty
        project.realName = realName
        project.commissions = commissions
        Project projectSaved = projectService.updateProject(project)
        then: "Project is update properly"

        projectSaved.shortName ==shortName
        projectSaved.region == region
        projectSaved.region.iso3166_2 == region.iso3166_2
        projectSaved.region.id == region.id
        projectSaved.realName == realName
        projectSaved.commissions == commissions
        projectSaved.politicalParty == politicalParty
        projectSaved.institution == institution
        projectSaved.institution.name == institution.name
        Project.withNewSession {
            Project projectRecovered = Project.findByHashtag("#leyAborto")
            projectRecovered.shortName ==shortName
            projectRecovered.region == region
            projectRecovered.region.iso3166_2 == region.iso3166_2
            projectRecovered.region.id == region.id
            projectRecovered.realName == realName
            projectRecovered.commissions == commissions
            projectRecovered.institution == institution
            projectRecovered.institution.name == institution.name
//            projectRecovered.politicalParty.name == politicalParty.name
//            projectRecovered.politicalParty == politicalParty
        }

    }

}
