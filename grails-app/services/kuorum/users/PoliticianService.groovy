package kuorum.users

import grails.transaction.Transactional
import kuorum.causes.CausesService
import kuorum.register.KuorumUserSession
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import org.kuorum.rest.model.tag.CauseRSDTO

@Transactional
class PoliticianService {

    IndexSolrService indexSolrService
    CausesService causesService
    KuorumUserService kuorumUserService

    void updateNews(KuorumUser politician, List<PoliticianRelevantEvent> relevantEvents){
        politician.relevantEvents = relevantEvents.findAll{it && it.validate()}
        kuorumUserService.updateUser(politician)
    }

    void updateUserCauses(KuorumUserSession loggedUser, List<String> causes){

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
