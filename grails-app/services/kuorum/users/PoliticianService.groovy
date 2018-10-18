package kuorum.users

import grails.transaction.Transactional
import kuorum.RegionService
import kuorum.causes.CausesService
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.kuorum.rest.model.tag.CauseRSDTO

@Transactional
class PoliticianService {

    FileService fileService;
    KuorumMailService kuorumMailService
    LinkGenerator grailsLinkGenerator
    IndexSolrService indexSolrService
    CausesService causesService
    NotificationService notificationService
    RegionService regionService
    KuorumUserService kuorumUserService

    KuorumUser updatePoliticianRelevantEvents(KuorumUser politician, List<PoliticianRelevantEvent> relevantEvents){
        politician.relevantEvents = relevantEvents.findAll{it && it.validate()}
        kuorumUserService.updateUser(politician)
    }

    KuorumUser updatePoliticianCauses(KuorumUser politician, List<String> causes, Boolean audit=true){

        List<CauseRSDTO> oldCauses = causesService.findSupportedCauses(politician);
        oldCauses.each {cause ->
            causesService.unsupportCause(politician, cause.name)
        }
        causes.findAll({it?.trim()}).collect({it.decodeHashtag()}).each {cause ->
            causesService.supportCause(politician, cause)
        }
        indexSolrService.deltaIndex()
    }
}
