package kuorum.dashboard

import grails.test.spock.IntegrationSpec
import kuorum.Region
import kuorum.core.model.RegionType
import kuorum.helper.IntegrationHelper
import kuorum.users.KuorumUser
import spock.lang.Shared

class DashboardServiceIntegrationSpec extends IntegrationSpec{

    @Shared
    DashboardService dashboardService

    @Shared
    KuorumUser politician, userToFollow, salendaRegularUser

    @Shared
    String regionCodeName

    def setupSpec(){
        Region.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")

        politician = KuorumUser.findByEmail('politician@example.com')
        userToFollow = KuorumUser.findByEmail('patxi@example.com')
        regionCodeName = politician.professionalDetails.region.iso3166_2
        salendaRegularUser = IntegrationHelper.createDefaultUser('salendaRegularUser@example.com' )
        salendaRegularUser.personalData.provinceCode = regionCodeName
        salendaRegularUser.save(flush: true)
    }

    def cleanupSpec(){
        salendaRegularUser.delete(flush: true)
        politician.professionalDetails.region.iso3166_2 = Region.findByNameAndRegionType('España', RegionType.NATION).iso3166_2
    }
}
