package kuorum.users

import grails.transaction.Transactional
import kuorum.RegionService
import kuorum.causes.CausesService
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.OfficeDetails
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
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
    KuorumUserAuditService kuorumUserAuditService

    KuorumUser updatePoliticianRelevantEvents(KuorumUser politician, List<PoliticianRelevantEvent> relevantEvents){
        politician.relevantEvents = relevantEvents.findAll{it && it.validate()}
        kuorumUserService.updateUser(politician)
    }

    KuorumUser updatePoliticianProfessionalDetails(KuorumUser politician, ProfessionalDetailsCommand command){
        if (!politician.professionalDetails ){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.constituency = command.constituency
        politician.professionalDetails.institution = command.institution
        politician.professionalDetails.region= command.region
        politician.careerDetails= command.careerDetails
        kuorumUserService.updateUser(politician)
    }

    KuorumUser updatePoliticianQuickNotes(KuorumUser politician, PoliticianExtraInfo politicianExtraInfo, OfficeDetails institutionalOffice, OfficeDetails politicalOffice){
        politicianExtraInfo.ipdbId = politician?.politicianExtraInfo?.ipdbId
        politician.politicianExtraInfo = politicianExtraInfo
        politician.institutionalOffice= institutionalOffice
        politician.politicalOffice= politicalOffice
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
        if(audit){
            // If politician.save() will override the ideologic field that is defined on service, but not on kuorum.
            kuorumUserAuditService.auditEditUser(politician)
        }
    }
}
