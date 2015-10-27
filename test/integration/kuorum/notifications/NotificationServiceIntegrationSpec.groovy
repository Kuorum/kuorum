package kuorum.notifications

import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.RegionType
import kuorum.core.model.search.Pagination
import kuorum.helper.IntegrationHelper
import kuorum.project.Project
import kuorum.users.KuorumUser
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 25/03/14.
 */
class NotificationServiceIntegrationSpec extends Specification{

    def fixtureLoader
    def notificationService

    def setup(){
        Notification.collection.getDB().dropDatabase()
        fixtureLoader.load("testBasicData")
    }

    @Unroll
    @Ignore
    void "test find #numNotificationFound user notifications and notifications days are #rangeDate"(){
        given: "User and one notification per day"
        KuorumUser user = KuorumUser.findByEmail("patxi@example.com")
        Project project = Project.findByHashtag("#codigoPenal")

        rangeDate.each {
            ProjectClosedNotification notification = new ProjectClosedNotification(project:project)
            notification.kuorumUser = user
            notification.save(flush: true, failOnError: true)
            notification.dateCreated = (new Date() + it)
            notification.save(flush: true, failOnError: true)
        }
        Pagination pagination = new Pagination(max:numResults, offset:offset)
        when:
        notificationService.markUserNotificationsAsChecked(user)
        List<Notification> notifications = notificationService.findUserNotificationsNotChecked(user,pagination)
        then:
        notifications.size() == numNotificationFound
        if (notifications.size())
            notifications.first().dateCreated >= notifications.last().dateCreated
        where:
        rangeDate | numNotificationFound | numResults | offset
        (-5..1)   |   1                  | 5          | 0
        (-5..5)   |   5                  | 5          | 0
        (-5..10)  |   5                  | 5          | 5
        (-5..-1)  |   0                  | 5          | 0
        (-5..5)   |   0                  | 5          | 5
    }

    @Unroll
    @Ignore
    void "search related users to a project by the searchRelatedUserToUserCommisions"() {
        given:"users with different data"
        List <KuorumUser> listUsers=[]
        String provinceCode = Region.findByNameAndRegionType("�vila", RegionType.LOCAL).iso3166_2
        4.times{
            listUsers << IntegrationHelper.createDefaultUser("relatedUser${it}@example.com")
        }
        listUsers[0..2]*.relevantCommissions = [CommissionType.AGRICULTURE, CommissionType.ECONOMY, CommissionType.DEFENSE]
        listUsers[1..3]*.personalData*.provinceCode = provinceCode
        listUsers[3].relevantCommissions = [CommissionType.CONSTITUTIONAL]
        listUsers[0].personalData.provinceCode = Region.findByNameAndRegionType("Valladolid", RegionType.LOCAL).iso3166_2

        and:"a project to search the related users"
        Project project = IntegrationHelper.createDefaultProject("#relatedProyects")
        project.commissions = [CommissionType.AGRICULTURE]
        project.region.iso3166_2 = provinceCode

        project.save(flush: true)
        listUsers*.save(flush: true)

        when:"we call the search method"
        List result = notificationService.searchRelatedUserToProject(project)

        then:"the result will be 2 users, in position lisUsers[0] and lisUsers[1] because they have commisionType:AGRICULTURE and region:region, similar to the project"
        result
        result.size() == 2
        result.each {
            it in listUsers[1..2]
        }

        cleanup:
        listUsers*.delete(flush:true)
        Project.findById(project?.id)?.delete(flush:true)
    }

    @Unroll
    @Ignore
    void "search related users to a project but don't find any user"() {
        given:"users with different data"
        List <KuorumUser> listUsers=[]
        String provinceCode = Region.findByNameAndRegionType("�vila", RegionType.LOCAL).iso3166_2
        4.times{
            listUsers << IntegrationHelper.createDefaultUser("relatedUser${it}@example.com")
        }
        listUsers[0..3]*.relevantCommissions = [CommissionType.ECONOMY, CommissionType.DEFENSE]
        listUsers[0..3]*.personalData*.provinceCode = provinceCode
        and:"a project to search the related users"
        Project project = IntegrationHelper.createDefaultProject("#relatedProyects")
        project.commissions = [CommissionType.AGRICULTURE]
        project.region.iso3166_2 = provinceCode

        project.save(flush: true)
        listUsers*.save(flush: true)

        when:"we call the search method"
        List result = notificationService.searchRelatedUserToProject(project)

        then:
        !result

        cleanup:
        listUsers*.delete(flush:true)
        Project.findById(project?.id)?.delete(flush:true)
    }
}
