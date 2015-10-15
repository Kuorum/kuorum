package kuorum.users

import com.mongodb.DBCursor
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity
import kuorum.users.extendedPoliticianData.PoliticalActivityActionType
import kuorum.users.extendedPoliticianData.PoliticalActivityOutcomeType
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianLeaning
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.PoliticianTimeLine
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import org.bson.types.ObjectId

@Transactional
class PoliticianService {

    FileService fileService;

    KuorumUser createPoliticianFromCSV(def line) {

        KuorumUser politician = findOrRecoverPolitician(line.id)
        populateBasicData(politician, line)
        populateLeaning(politician, line)
        populateProfessionalDetails(politician, line)
        populateSocialLinks(politician, line)
        populateExternalPoliticianActivity(politician, line)
        populateRelevantEvents(politician, line)
        populateExtraInfo(politician, line)

        politician.save(failOnError: true)
    }

    private void populateRelevantEvents(KuorumUser politician, def line) {
        List<PoliticianRelevantEvent> relevantEvents = []
        String prefixTitle = "known_for"
        String prefixLink = "known_for_link"
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
        politician.name = line."name"
        String avatarUrl = line."picture"
        if (avatarUrl.startsWith("http")){
            KuorumFile avatar = fileService.createExternalFile(politician, avatarUrl,FileGroup.USER_AVATAR, FileType.IMAGE)
            politician.avatar = avatar
        }
        politician.bio = line."bio"
        politician.userType = UserType.POLITICIAN
        politician.email = line."email"?:politician.email?:"info+${line.id}-${politician.name.replaceAll(" ","_")}@kuorum.org"
        politician.password = "CSV ${Math.random()}"
        politician.verified = true
        politician.personalData = politician.personalData ?: new PersonData()
        politician.personalData.gender = line."gender"?Gender.valueOf(line."gender"):Gender.MALE
        politician.personalData.userType = politician.userType

    }

    private void populateSocialLinks(KuorumUser politician, def line){
        if (!politician.socialLinks){
            politician.socialLinks = new SocialLinks()
        }
        politician.socialLinks.blog = line."blog"
        politician.socialLinks.facebook = line."facebook"
        politician.socialLinks.twitter = line."twitter"
        politician.socialLinks.linkedIn = line."linkedin"
        politician.socialLinks.googlePlus= line."googlePlus"
        politician.socialLinks.instagram = line."instagram"
        politician.socialLinks.youtube= line."youtubeChannel"
    }

    private void populateExternalPoliticianActivity(KuorumUser politician, def line){
        if (!politician.timeLine){

        }
        List<ExternalPoliticianActivity> timeLines = [];
        String prefix = "lastActivity"
        (1..5).each{i->
            ExternalPoliticianActivity epa = new ExternalPoliticianActivity()
            String title = line."${prefix}${i}"
            if (title){
                epa.date = Date.parse("dd/MM/yyyy HH:mm",line."${prefix}${i}Date")
                epa.title =title
                epa.link ="#"
                epa.actionType =line."${prefix}${i}Action"=="Participate"?PoliticalActivityActionType.VOTED_FOR:PoliticalActivityActionType.VOTED_AGAINST
                epa.outcomeType =
                        line."${prefix}${i}Outcome"=="Became Law"?PoliticalActivityOutcomeType.APPROVED:
                        line."${prefix}${i}Outcome"=="Became Private Law"?PoliticalActivityOutcomeType.APPROVED:
                        line."${prefix}${i}Outcome"=="Passed House"?PoliticalActivityOutcomeType.APPROVED:
                        line."${prefix}${i}Outcome"=="Passed Senate"?PoliticalActivityOutcomeType.APPROVED:
                        line."${prefix}${i}Outcome"=="Failed House"?PoliticalActivityOutcomeType.DENIED:
                        line."${prefix}${i}Outcome"=="Failed House"?PoliticalActivityOutcomeType.DENIED:
                        line."${prefix}${i}Outcome"=="Introduced"?PoliticalActivityOutcomeType.PENDING:
                        PoliticalActivityOutcomeType.PENDING
                timeLines << epa
            }
        }
        timeLines.sort{a,b-> b.date<=>a.date}
        politician.externalPoliticianActivities = timeLines
    }

    private void populateLeaning(KuorumUser politician, def line){
        if (!politician.politicianLeaning){
            politician.politicianLeaning = new PoliticianLeaning()
        }
        Double dli = Double.parseDouble(line."political_leaning_index")
        politician.politicianLeaning.liberalIndex = Math.round(dli * 100)
    }

    private void populateExtraInfo(KuorumUser politician, def line){
        if (!politician.politicianExtraInfo){
            throw RuntimeException("Este politico no tiene el id de la BBDD")
        }
        politician.politicianExtraInfo.webSite = line."officialWebsite"
        politician.politicianExtraInfo.university = line."university"
        politician.politicianExtraInfo.school = line."school"
        politician.politicianExtraInfo.completeName = line."completeName"?:line."name"
        String dateOfBirth = line."dateOfBirth"
        politician.politicianExtraInfo.birthDate = dateOfBirth?Date.parse("dd/MM/yyyy",dateOfBirth):""
        politician.politicianExtraInfo.birthPlace = line."placeOfBirth"
    }

    private void populateProfessionalDetails(KuorumUser politician, def line){
        if (!politician.professionalDetails){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.politicalParty = line."politicalParty"
        politician.professionalDetails.profession = line."profession"
        politician.professionalDetails.institution = line."institution"
        politician.professionalDetails.constituency = findConstituency(line)
        politician.professionalDetails.region =findRegion(line)

    }

    private Region findRegion(def line){
        def regionFields = ["region_code_alliance","region_code_nation", "region_code_state", "region_code_county", "region_code_city"]
        findRegionCombiningRegionCodes(regionFields, line)
    }

    private Region findConstituency(def line){
        String constituencyCode = line."constituency"
        Region constituency
        if (constituencyCode){
            constituency = Region.findByIso3166_2(constituencyCode)
        }
        if (!constituency){
            def consituencyFields = ["constituency_code_alliance","constituency_code_nation", "constituency_code_state","constituency_code_county","constituency_code_city"]
            constituency = findRegionCombiningRegionCodes(consituencyFields, line)
        }
        constituency
    }
    
    private Region findRegionCombiningRegionCodes(List<String> fieldNamesInOrder, line){
        String separator = ""
        String regionCode = "";
        for (String fieldName : fieldNamesInOrder){
            if (!line."${fieldName}"){
                break
            }
            regionCode += separator+line."${fieldName}"
            separator = "-"
        }
        Region.findByIso3166_2(regionCode)
    }

}
