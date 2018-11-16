package kuorum.users

import grails.transaction.Transactional
import kuorum.RegionService
import kuorum.causes.CausesService
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.tag.CauseRSDTO

@Transactional
class PoliticianService {

    FileService fileService
    KuorumMailService kuorumMailService
    LinkGenerator grailsLinkGenerator
    IndexSolrService indexSolrService
    CausesService causesService
    NotificationService notificationService
    RegionService regionService
    KuorumUserService kuorumUserService

    KuorumUser updateNews(KuorumUser politician, List<PoliticianRelevantEvent> relevantEvents){
        politician.relevantEvents = relevantEvents.findAll{it && it.validate()}
        kuorumUserService.updateUser(politician)
    }

    KuorumUser updateUserCauses(KuorumUserSession loggedUser, List<String> causes){

        List<CauseRSDTO> oldCauses = causesService.findSupportedCauses(loggedUser)
        oldCauses.each {cause ->
            causesService.unsupportCause(loggedUser, cause.name)
        }
        causes.findAll({it?.trim()}).collect({it.decodeHashtag()}).each {cause ->
            causesService.supportCause(loggedUser, cause)
        }
        indexSolrService.deltaIndex()
    }
}
