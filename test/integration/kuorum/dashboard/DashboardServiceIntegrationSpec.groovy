package kuorum.dashboard

import grails.plugin.fixtures.FixtureLoader
import grails.test.spock.IntegrationSpec
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.RegionType
import kuorum.helper.IntegrationHelper
import kuorum.notifications.Notice
import kuorum.users.KuorumUser
import spock.lang.Shared
import spock.lang.Unroll

class DashboardServiceIntegrationSpec extends IntegrationSpec{

    @Shared
    DashboardService dashboardService


    @Shared
    FixtureLoader fixtureLoader

    @Shared
    KuorumUser politician, politicianToFollow, salendaRegularUser

    @Shared
    String regionCodeName

    def setupSpec(){
        Region.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")

        politician = KuorumUser.findByEmail('politician@example.com')
        politicianToFollow = KuorumUser.findByEmail('politicianinactive@example.com')
        regionCodeName = politician.politicianOnRegion.iso3166_2
        salendaRegularUser = IntegrationHelper.createDefaultUser('salendaRegularUser@example.com' )
        salendaRegularUser.personalData.provinceCode = regionCodeName
        salendaRegularUser.save(flush: true)
    }

    @Unroll
    void "test to get all the incomplete user's notices "(){
        given:"a user who has incomplete his profile"
        KuorumUser user = KuorumUser.findByEmail(email)
        user.personalData.province = province
        user.personalData.gender = gender
        user.personalData.birthday = birthday
        user.personalData.provinceCode = regionUserName
        if(following){
            user.following << following
        }else{
            user.following = []
        }
        user.personalData.telephone = telephone
        user.notice = new Notice(reloadDashboard: counterDashboard, timesInMonth:showNoticeMonth)
        user.save(flush:true)

        when:"call the method to get the notices"
        Map priorityNotice = dashboardService.showNotice(user, Locale.ENGLISH)

        then:"get the most critical notice"
        if(priorityNotice){
            priorityNotice.errors == errors
            priorityNotice.notice = notice
        }

        where:
        email                    | gender      | birthday   | province                                                    | regionUserName | following             | telephone    | counterDashboard | showNoticeMonth || notice                                 || errors
        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 0                | 0               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 0                | 1               || 'No hay políticos a seguir en tu país' || false
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | regionCodeName | politician.id         | null         | 0                | 1               || 'Rellena la provincia'                 || false
        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 0                | 1               || 'Rellena tu edad y sexo'               || false
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 20               | 1               || 'Rellena tu edad y sexo'               || false
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 20               | 0               || null | true


        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 0                | 1               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | null         | 0                | 1               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | null           | null                  | null         | 0                | 2               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | null       | null                                                        | null           | null                  | null         | 20               | 1               || 'Sigue a un político'                  || false
        salendaRegularUser.email | null        | new Date() | null                                                        | null           | null                  | null         | 20               | 1               || 'Sigue a un político'                  || false
        salendaRegularUser.email | null        | null       | null                                                        | null           | null                  | null         | 20               | 2               || 'Sigue a un político'                  || false

        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | regionCodeName | politician.id         | null         | 0                | 1               || 'Rellena la provincia'                 || false
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | null           | politician.id         | null         | 20               | 1               || 'Rellena la provincia'                 || false
        salendaRegularUser.email | Gender.MALE | null       | null                                                        | null           | politician.id         | null         | 20               | 1               || 'Rellena la provincia'                 || false
        salendaRegularUser.email | null        | new Date() | null                                                        | null           | politician.id         | null         | 40               | 1               || 'Rellena la provincia'                 || false

        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 0                | 1               || 'Rellena tu edad y sexo'               || false
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 40               | 1               || 'Rellena tu edad y sexo'               || false

        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 40               | 1               || 'Rellena tu edad y sexo'               || false
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 40               | 1               || 'Rellena tu edad y sexo'               || false

        politician.email         | Gender.MALE | null       | null                                                        | null           | null                  | null         | 0                | 1               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | null       | null                                                        | regionCodeName | null                  | null         | 0                | 1               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 0                | 1               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | new Date() | null                                                        | regionCodeName | null                  | null         | 20               | 0               || 'Rellena tu número de teléfono'        || false
        politician.email         | null        | null       | null                                                        | regionCodeName | null                  | null         | 20               | 0               || 'Rellena tu número de teléfono'        || false

        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | '6565656565' | 40               | 1               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | '6565656565' | 40               | 1               || 'Sigue a un político'                  || false
        politician.email         | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | '6565656565' | 40               | 2               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | new Date() | null                                                        | null           | null                  | '6565656565' | 40               | 0               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | null       | null                                                        | null           | null                  | '6565656565' | 40               | 0               || 'Sigue a un político'                  || false
        politician.email         | null        | null       | null                                                        | null           | null                  | '6565656565' | 40               | 0               || 'Sigue a un político'                  || false

        politician.email         | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politicianToFollow.id | '6565656565' | 60               | 1               || 'Rellena tu edad y sexo'               || false
        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politicianToFollow.id | '6565656565' | 60               | 1               || 'No hay políticos a seguir en tu país' || false
        politician.email         | Gender.MALE | new Date() | null                                                        | null           | politicianToFollow.id | '6565656565' | 0                | 1               || 'Rellena la provincia'                 || false
        politician.email         | Gender.MALE | null       | null                                                        | null           | politicianToFollow.id | '6565656565' | 60               | 1               || 'Rellena la provincia'                 || false
        politician.email         | null        | new Date() | null                                                        | null           | politicianToFollow.id | '6565656565' | 60               | 1               || 'Rellena la provincia'                 || false

        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | '6565656565' | 0                | 0               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | '6565656565' | 0                | 0               || 'Sigue a un político'                  || false
        politician.email         | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politicianToFollow.id | '6565656565' | 0                | 0               || 'Rellena tu edad y sexo' | false

        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 4                | 7               || 'Sigue a un político' | false
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 4                | 0               || null                                   || true
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | regionCodeName | politician.id         | null         | 0                | 9               || null                                   || true
        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 5                | 0               || null                                   || true
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 5                | 5               || null                                   || true
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 13               | 6               || null                                   || true
        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 0                | 2               || null                                   || true


        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | null           | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false
        salendaRegularUser.email | Gender.MALE | null       | null                                                        | null           | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false
        salendaRegularUser.email | null        | new Date() | null                                                        | null           | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false
        salendaRegularUser.email | null        | null       | null                                                        | null           | null                  | null         | 1                | 5               || 'Sigue a un político'                  || false

        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | regionCodeName | politician.id         | null         | 4                | 1               || null                                   || true
        salendaRegularUser.email | Gender.MALE | new Date() | null                                                        | null           | politician.id         | null         | 5                | 2               || null                                   || true
        salendaRegularUser.email | Gender.MALE | null       | null                                                        | null           | politician.id         | null         | 6                | 0               || null                                   || true
        salendaRegularUser.email | null        | new Date() | null                                                        | null           | politician.id         | null         | 7                | 6               || null                                   || true

        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 0                | 5               || null                                   || true
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | politician.id         | null         | 20               | 9               || null                                   || true

        salendaRegularUser.email | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 5                | 0               || null                                   || true
        salendaRegularUser.email | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politician.id         | null         | 3                | 3               || null                                   || true

        politician.email         | Gender.MALE | null       | null                                                        | null           | null                  | null         | 5                | 5               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | null       | null                                                        | regionCodeName | null                  | null         | 5                | 2               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | null         | 5                | 2               || 'Rellena tu número de teléfono'        || false
        politician.email         | Gender.MALE | new Date() | null                                                        | regionCodeName | null                  | null         | 2                | 5               || 'Rellena tu número de teléfono'        || false
        politician.email         | null        | null       | null                                                        | regionCodeName | null                  | null         | 2                | 4               || 'Rellena tu número de teléfono'        || false

        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | regionCodeName | null                  | '6565656565' | 0                | 5               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | '6565656565' | 5                | 3               || 'Sigue a un político'                  || false
        politician.email         | null        | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | null                  | '6565656565' | 4                | 3               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | new Date() | null                                                        | null           | null                  | '6565656565' | 10               | 6               || 'Sigue a un político'                  || false
        politician.email         | Gender.MALE | null       | null                                                        | null           | null                  | '6565656565' | 1                | 5               || 'Sigue a un político'                  || false
        politician.email         | null        | null       | null                                                        | null           | null                  | '6565656565' | 6                | 7               || 'Sigue a un político'                  || false

        politician.email         | Gender.MALE | null       | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politicianToFollow.id | '6565656565' | 5                | 2               || null                                   || true
        politician.email         | Gender.MALE | new Date() | Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) | null           | politicianToFollow.id | '6565656565' | 5                | 0               || null                                   || true
        politician.email         | Gender.MALE | new Date() | null                                                        | null           | politicianToFollow.id | '6565656565' | 12               | 4               || null                                   || true
        politician.email         | Gender.MALE | null       | null                                                        | null           | politicianToFollow.id | '6565656565' | 2                | 5               || null                                   || true
        politician.email         | null        | new Date() | null                                                        | null           | politicianToFollow.id | '6565656565' | 6                | 6               || null                                   || true


        politician.email         | Gender.MALE | new Date() | null                                                        | regionCodeName | politicianToFollow.id | '6565656565' | 0                | 8               || null                                   || true
        politician.email         | Gender.MALE | null       | null                                                        | regionCodeName | politicianToFollow.id | '6565656565' | 8                | 3               || null                                   || true
        politician.email         | null        | new Date() | null                                                        | regionCodeName | politicianToFollow.id | '6565656565' | 8                | 1               || null                                   || true
    }

    def cleanupSpec(){
        salendaRegularUser.delete(flush: true)
        politician.politicianOnRegion.iso3166_2 = Region.findByNameAndRegionType('España', RegionType.NATION).iso3166_2
    }
}
