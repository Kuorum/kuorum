package kuorum.users

import com.mongodb.DBCursor
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianLeaning
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import org.bson.types.ObjectId

@Transactional
class PoliticianService {

    FileService fileService;

    KuorumUser createPoliticianFromCSV(def line) {

        KuorumUser politician = findOrRecoverPolitician(line.Id)
        populateBasicData(politician, line)
        populateLeaning(politician, line)
        populateProfessionalDetails(politician, line)
        populateSocialLinks(politician, line)
        populateLastActivity(politician, line)
        populateRelevantEvents(politician, line)
        populateExtraInfo(politician, line)

        politician.save(failOnError: true)
    }

    private void populateRelevantEvents(KuorumUser politician, def line) {
        List<PoliticianRelevantEvent> relevantEvents = []
        String prefixTitle = "Known for"
        String prefixLink = "Known for link"
        (1..5).each{i->
            if (line."${prefixLink}${i}"){
                PoliticianRelevantEvent relevantEvent = new PoliticianRelevantEvent();
                relevantEvent.title = line."${prefixTitle}${i}"
                relevantEvent.url = line."${prefixLink}${i}"
                relevantEvents.add(relevantEvent)
            }
        }
        politician.relevantEvents = relevantEvents
    }

    private KuorumUser findOrRecoverPolitician(String ipdbId){
        findOrRecoverPolitician(Long.parseLong(ipdbId))
    }
    private KuorumUser findOrRecoverPolitician(Long ipdbId){
        DBCursor result = KuorumUser.collection.find(['politicianExtraInfo.ipdbId':ipdbId],[_id:1])
        KuorumUser politician;
        if (result.hasNext()){
            ObjectId userId = result.next()._id
            politician = KuorumUser.findById(userId)
            log.debug("Updating ${politician.name} with ipdbId: ${ipdbId}")
        }else{
            politician = new KuorumUser()
            politician.setPoliticianExtraInfo(new PoliticianExtraInfo(ipdbId: ipdbId))
            log.debug("Creating politician with ipdbId ${ipdbId}")
        }
        return politician
    }

    private void populateBasicData(KuorumUser politician, def line){
        politician.name = line."Name"
        String avatarUrl = line."Picture"
        if (avatarUrl.startsWith("http")){
            KuorumFile avatar = fileService.createExternalFile(politician, avatarUrl,FileGroup.USER_AVATAR, FileType.IMAGE)
            politician.avatar = avatar
        }
        politician.bio = line."Bio"
        politician.userType = UserType.POLITICIAN
        politician.email = line."Email"?:politician.email?:"info+${politician.name.replaceAll(" ","_")}@kuorum.org"
        politician.password = "CSV ${Math.random()}"
        politician.verified = true
        politician.personalData = politician.personalData ?: new PersonData()
        politician.personalData.gender = Gender.MALE //TODO: GENDER??
        politician.personalData.userType = politician.userType

    }

    private void populateSocialLinks(KuorumUser politician, def line){
        if (!politician.socialLinks){
            politician.socialLinks = new SocialLinks()
        }
        politician.socialLinks.blog = line."Blog"
        politician.socialLinks.facebook = line."Facebook"
        politician.socialLinks.twitter = line."Twitter"
        politician.socialLinks.linkedIn = line."Linkedin"
        politician.socialLinks.googlePlus= line."Googleplus"
        politician.socialLinks.instagram = line."Instagram"
        politician.socialLinks.youtube= line."Youtubechannel"
    }

    private void populateLastActivity(KuorumUser politician, def line){
        if (!politician.timeLine){

        }
    }

    private void populateLeaning(KuorumUser politician, def line){
        if (!politician.politicianLeaning){
            politician.politicianLeaning = new PoliticianLeaning()
        }
        Double dli = Double.parseDouble(line."Political leaning index")
        politician.politicianLeaning.liberalIndex = Math.round(dli * 100)
    }

    private void populateExtraInfo(KuorumUser politician, def line){
        if (!politician.politicianExtraInfo){
            throw RuntimeException("Este politico no tiene el id de la BBDD")
        }
        politician.politicianExtraInfo.webSite = line."Officialwebsite"
        politician.politicianExtraInfo.webSite = line."Officialwebsite"
        politician.politicianExtraInfo.university = line."University"
        politician.politicianExtraInfo.school = line."School"
        politician.politicianExtraInfo.completeName = line."Completename"?:line."Name"
    }

    private void populateProfessionalDetails(KuorumUser politician, def line){
        if (!politician.professionalDetails){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.politicalParty = line."Politicalparty"
        politician.professionalDetails.profession = line."Profession"
    }

}
