package kuorum.users

import com.mongodb.DBCursor
import grails.plugin.mail.MailService
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianLeaning
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.PoliticianTimeLine
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

@Transactional
class PoliticianService {

    FileService fileService;
    KuorumMailService kuorumMailService
    LinkGenerator grailsLinkGenerator

    void asyncUploadPoliticianCSV(KuorumUser executorUser, InputStream data){
        byte[] buffer = new byte[data.available()];
        data.read(buffer)
        File csv = File.createTempFile("temp-file-name-${new Date().time}", ".csv");
        OutputStream outStream = new FileOutputStream(csv);
        outStream.write(buffer);
        outStream.close()
        Thread.start {
            InputStream csvStream = new FileInputStream(csv);
            Reader reader = new InputStreamReader(csvStream)
            def lines = com.xlson.groovycsv.CsvParser.parseCsv(reader)
            String politiciansOk = "<h1>Politician OK</h1><UL>"
            String politiciansWrong = "<h1>Politician Wrong</h1><UL>"
            for(line in lines) {
                try {
                    KuorumUser user = createPoliticianFromCSV(line)
                    log.info("Uploaded ${user.name}")
                    politiciansOk += "<li> <a href='${grailsLinkGenerator.link(mapping: 'userShow', params:user.encodeAsLinkProperties())}'> $user.name </a></li>"
                }catch (Exception e){
                    log.warn("Error parseando el político ${line.name}", e)
                    politiciansWrong +=  "<li>${line.name} (${line.id}): <i>${e.getMessage()}</i></li>"
                }
            }
            politiciansOk += "</UL>"
            politiciansWrong += "</UL>"
            log.info("Enviando mail de fin de procesado de email a ${executorUser.name} (${executorUser.email})")
            kuorumMailService.sendBatchMail(executorUser, politiciansWrong + politiciansOk, "CSV Loaded")
            csv.delete()
        }

    }

    KuorumUser createPoliticianFromCSV(def line) {

        KuorumUser politician = findOrRecoverPolitician(line.email, line.id)
        populateBasicData(politician, line)
        populateLeaning(politician, line)
        populateProfessionalDetails(politician, line)
        populateSocialLinks(politician, line)
        populateExternalPoliticianActivity(politician, line)
        populateTimeLine(politician, line)
        populateTags(politician, line)
        populateRelevantEvents(politician, line)
        populateExtraInfo(politician, line)

        politician.save(failOnError: true)
    }

    private void populateTags(KuorumUser politician, def line) {
        String prefix = "cause"
        List<String> tags = []
        (1..5).each{i->
            String tag = line."${prefix}${i}"
            if (tag){
                tags << tag
            }
        }
        politician.tags= tags
    }

    private void populateTimeLine(KuorumUser politician, def line) {
        List<PoliticianTimeLine> timeLine = []
        String prefix = "political_experience"
        (1..5).each{i->
            if (line."${prefix}${i}"){
                PoliticianTimeLine event = new PoliticianTimeLine();
                event.title = line."${prefix}${i}"
                event.text = line."${prefix}${i}_content"
                event.date = Date.parse("dd/MM/yyyy HH:mm",line."${prefix}${i}_date")
                event.important = false
                if (!timeLine.find{it.title == event.title}){
                    timeLine.add(event)
                }
            }
        }
        politician.timeLine = timeLine.sort{a,b-> b.date<=>a.date}
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

    private KuorumUser findOrRecoverPolitician(String email, String ipdbId){
        findOrRecoverPolitician(email, ipdbId?Long.parseLong(ipdbId):null)
    }
    private KuorumUser findOrRecoverPolitician(String email, Long ipdbId){

        //Search politician by email
        KuorumUser politician;
        if (email){
            politician = KuorumUser.findByEmail(email);
            if (politician){
                if (!politician.politicianExtraInfo){
                    politician.setPoliticianExtraInfo(new PoliticianExtraInfo(ipdbId: ipdbId))
                }
                return politician;
            }
        }

        // Search politician by IPDB id
        DBCursor result = KuorumUser.collection.find(['politicianExtraInfo.ipdbId':ipdbId],[_id:1])
        if (result.hasNext()){
            ObjectId userId = result.next()._id
            politician = KuorumUser.findById(userId)
            log.debug("Updating ${politician.name} with ipdbId: ${ipdbId}")
        }else{
            politician = new KuorumUser()
            politician.enabled = false
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
        String profileUrl = line."politicalPartyImage"
        if (profileUrl.startsWith("http")){
            KuorumFile imageProfile = fileService.createExternalFile(politician, profileUrl,FileGroup.USER_PROFILE, FileType.IMAGE)
            politician.imageProfile= imageProfile
        }
        politician.bio = line."bio"
        politician.userType = UserType.POLITICIAN
        politician.email = line."email"?:politician.email?:"info+${line.id}-${politician.name.encodeAsMD5()}@kuorum.org"
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
        List<ExternalPoliticianActivity> externalPoliticianActivities = [];
        String prefix = "lastActivity"
        (1..5).each{i->
            ExternalPoliticianActivity epa = new ExternalPoliticianActivity()
            String title = line."${prefix}${i}"
            if (title){
                epa.date = Date.parse("dd/MM/yyyy HH:mm",line."${prefix}${i}Date")
                epa.title =title
                epa.link =line."${prefix}${i}Link"
                epa.actionType =line."${prefix}${i}Action"
                epa.outcomeType =line."${prefix}${i}Outcome"
                externalPoliticianActivities << epa
            }
        }
        externalPoliticianActivities.sort{a,b-> b.date<=>a.date}
        politician.externalPoliticianActivities = externalPoliticianActivities
    }

    private void populateLeaning(KuorumUser politician, def line){
        if (!politician.politicianLeaning){
            politician.politicianLeaning = new PoliticianLeaning()
        }
        String leanindIndex = line."political_leaning_index";
        Double dli = 0.5
        if (leanindIndex){
            dli = Double.parseDouble(leanindIndex)
        }
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
        politician.politicianExtraInfo.birthDate = dateOfBirth?Date.parse("dd/MM/yyyy",dateOfBirth):null
        politician.politicianExtraInfo.birthPlace = line."placeOfBirth"
        politician.politicianExtraInfo.family = line."family"
    }

    private void populateProfessionalDetails(KuorumUser politician, def line){
        if (!politician.professionalDetails){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.politicalParty = line."politicalParty"
        politician.professionalDetails.profession = line."profession"
        politician.professionalDetails.position = line."position"
        politician.professionalDetails.institution = line."institution"
        politician.professionalDetails.constituency = findConstituency(line)
        politician.professionalDetails.region =findRegion(line)
        politician.professionalDetails.cvLink =line."cvLink"
        politician.professionalDetails.declarationLink =line."declarationLink"
        politician.professionalDetails.sourceWebsite =line."sourceWebsite"

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
