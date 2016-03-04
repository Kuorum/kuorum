package kuorum.users

import com.mongodb.DBCursor
import grails.plugin.mail.MailService
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.Region
import kuorum.RegionService
import kuorum.causes.CausesService
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity
import kuorum.users.extendedPoliticianData.OfficeDetails
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianLeaning
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.PoliticianTimeLine
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import org.apache.commons.lang.WordUtils
import org.bson.types.ObjectId
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

    private static final String IPDB_DATE_FORMAT = "dd/MM/yyyy HH:mm"

    KuorumUser updatePoliticianExternalActivity(KuorumUser politician, List<ExternalPoliticianActivity> externalPoliticianActivities){
        politician.externalPoliticianActivities = externalPoliticianActivities.findAll{it && it.validate()}
        sortExternalPoliticianActivity(politician)
        politician.save()
    }

    KuorumUser updatePoliticianRelevantEvents(KuorumUser politician, List<PoliticianRelevantEvent> relevantEvents){
        politician.relevantEvents = relevantEvents.findAll{it && it.validate()}
        politician.save()
    }

    KuorumUser updatePoliticianProfessionalDetails(KuorumUser politician, ProfessionalDetailsCommand command){
        if (!politician.professionalDetails ){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.constituency = command.constituency
        politician.professionalDetails.institution = command.institution
        politician.professionalDetails.region= command.region
        politician.careerDetails= command.careerDetails
        politician.save()
    }

    KuorumUser updatePoliticianExperience(KuorumUser politician, List<PoliticianTimeLine> timeLine){
        politician.timeLine = timeLine.findAll{it && it.validate()}
        sortPoliticalExperience(politician)
        politician.save()
    }

    KuorumUser updatePoliticianQuickNotes(KuorumUser politician, PoliticianExtraInfo politicianExtraInfo, OfficeDetails institutionalOffice, OfficeDetails politicalOffice){
        politicianExtraInfo.ipdbId = politician?.politicianExtraInfo?.ipdbId
        politician.politicianExtraInfo = politicianExtraInfo
        politician.institutionalOffice= institutionalOffice
        politician.politicalOffice= politicalOffice
        politician.save()
    }

    KuorumUser updatePoliticianCauses(KuorumUser politician, List<String> causes){

        List<CauseRSDTO> oldCauses = causesService.findDefendedCauses(politician);
        oldCauses.each {cause ->
            causesService.withdrawCause(politician, cause.name)
        }
        causes.findAll({it?.trim()}).collect({it.decodeHashtag()}).each {cause ->
            causesService.defendCause(politician, cause)
        }
        indexSolrService.index(politician)
        politician
    }

    KuorumUser requestABetaTesterAccount(KuorumUser user){
        if (user.userType!= UserType.POLITICIAN){
            throw new KuorumException("This user is not a politician")
        }
        user.requestedPoliticianBetaTester = true
        user.save()
        notificationService.sendBetaTesterPurchaseNotification(user)
        user
    }

    /**
     * Read a csv and create politicians asynchronously
     * @param executorUser
     * @param data
     */
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
                    politiciansOk += "<li> <a href='${grailsLinkGenerator.link(mapping: 'userShow', params:user.encodeAsLinkProperties(), absolute: true)}'> $user.name </a></li>"
                }catch (Exception e){
                    log.warn("Error parseando el politico ${line.name}", e)
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

        KuorumUser politician = findOrRecoverPolitician(line)
        populateBasicData(politician, line)
        populateImages(politician, line)
        populateLeaning(politician, line)
        populateProfessionalDetails(politician, line)
        populateCareerDetails(politician, line)
        populateSocialLinks(politician, line)
        populateExternalPoliticianActivity(politician, line)
        populateTimeLine(politician, line)
        populateTags(politician, line)
        populateRelevantEvents(politician, line)
        populateExtraInfo(politician, line)
        populateAddress(politician, line)

        politician.save(failOnError: true)
    }

    private void populateTags(KuorumUser politician, def line) {
        String prefix = "cause"
        List<String> tags = []
        (1..6).each{i->
            String tag = line."${prefix}${i}"
            if (tag){
                tags << tag
            }
        }
        updatePoliticianCauses(politician, tags)
    }

    private void populateTimeLine(KuorumUser politician, def line) {
        List<PoliticianTimeLine> timeLine = []
        String prefix = "political_experience"
        (1..5).each{i->
            if (line."${prefix}${i}"){
                PoliticianTimeLine event = new PoliticianTimeLine();
                event.title = line."${prefix}${i}"
                event.text = line."${prefix}${i}_content"
                event.date = parseDate(line."${prefix}${i}_date", IPDB_DATE_FORMAT)
                event.important = false
                if (!timeLine.find{it.title == event.title}){
                    timeLine.add(event)
                }
            }
        }
        politician.timeLine = timeLine
        sortPoliticalExperience(politician)
    }

    private void populateRelevantEvents(KuorumUser politician, def line) {
        List<PoliticianRelevantEvent> relevantEvents = []
        String prefixTitle = "known_for"
        String prefixLink = "known_for_link"
        (1..5).each{i->
            if (line."${prefixTitle}${i}"){
                PoliticianRelevantEvent relevantEvent = new PoliticianRelevantEvent();
                relevantEvent.title = line."${prefixTitle}${i}"
                relevantEvent.url = line."${prefixLink}${i}"
                relevantEvents.add(relevantEvent)
            }
        }
        politician.relevantEvents = relevantEvents
    }

    private void sortExternalPoliticianActivity(KuorumUser politician){
        politician.externalPoliticianActivities = politician.externalPoliticianActivities.sort{a,b-> !a.date?-1:!b.date?1:b.date<=>a.date}
    }
    private void sortPoliticalExperience(KuorumUser politician){
        politician.timeLine = politician.timeLine.sort{a,b-> b.date<=>a.date}
    }

    private KuorumUser findOrRecoverPolitician(def line){
        String email = line.email?:generateEmail(null, line)
        String externalId = line.id?:null
        //Search politician by email
        KuorumUser politician;
        if (email){
            politician = KuorumUser.findByEmail(email);
            if (politician){
                if (!politician.politicianExtraInfo){
                    politician.setPoliticianExtraInfo(new PoliticianExtraInfo(externalId: externalId))
                }
                politician.politicianExtraInfo.externalId = externalId;
                log.info("Updating ${politician.name} with externalId: ${externalId}")
                return politician;
            }
        }

        // Search politician by externalId id
        DBCursor result = KuorumUser.collection.find(['politicianExtraInfo.externalId':externalId],[_id:1])
        if (result.hasNext()){
            ObjectId userId = result.next()._id
            politician = KuorumUser.findById(userId)
            log.info("Updating ${politician.name} with externalId: ${externalId}")
        }else{
            politician = new KuorumUser()
            politician.enabled = false
            politician.setPoliticianExtraInfo(new PoliticianExtraInfo(externalId: externalId))
            log.info("Creating politician with ipdbId ${externalId}")
        }
        return politician
    }

    private void populateBasicData(KuorumUser politician, def line){
        politician.name = WordUtils.capitalizeFully(line."name")
        politician.bio = politician.bio?:line."bio"?.trim()
        politician.userType = UserType.POLITICIAN
        politician.email = generateEmail(politician, line)
        politician.password = !politician.password?.startsWith("CSV")?"CSV ${Math.random()}":politician.password
        politician.verified = true
        politician.personalData = politician.personalData ?: new PersonData()
        politician.personalData.gender = line."gender"?Gender.valueOf(line."gender"):Gender.MALE
        politician.personalData.userType = politician.userType
        politician.personalData.telephone = politician?.personalData?.telephone?:line."phone"?.trim()
        politician.alias = generateAlias(politician, line)
        if (!politician.save()){
            throw new KuorumException("Basic data not porvided, ${politician.errors}")
        }
    }

    private String generateEmail(KuorumUser politician, def line){
        String twitterAlias = line."twitter"?.trim()?.encodeAsTwitter()?.substring(1)
        politician?.email?:line."email"?:"info+${twitterAlias?:line."name"?.encodeAsMD5()}@kuorum.org"
    }

    private String generateAlias(KuorumUser politician, def line){
        String twitterAlias = line."twitter"?.trim()?.encodeAsTwitter()?.substring(1)
        String id = politician.id?.toString()
        if (!id){
            id = ObjectId.get().toString()
        }
        politician?.alias?:twitterAlias?:id.substring(id.length() - 15)
    }

    private void populateImages(KuorumUser politician, def line){
        String avatarUrl = line."picture"
        if (!politician.avatar && avatarUrl.startsWith("http")){
            KuorumFile avatar = fileService.createExternalFile(politician, avatarUrl,FileGroup.USER_AVATAR, FileType.IMAGE)
            if (politician.avatar && avatar){
                fileService.deleteKuorumFile(politician.avatar)
            }
            politician.avatar = avatar
        }
        String profileUrl = line."politicalPartyImage"
        if (!politician.imageProfile && profileUrl.startsWith("http")){
            KuorumFile imageProfile = fileService.createExternalFile(politician, profileUrl,FileGroup.USER_PROFILE, FileType.IMAGE)
            if (politician.imageProfile && imageProfile){
                fileService.deleteKuorumFile(politician.imageProfile)
            }
            politician.imageProfile= imageProfile
        }
    }

    private void populateSocialLinks(KuorumUser politician, def line){
        if (!politician.socialLinks){
            politician.socialLinks = new SocialLinks()
        }
        politician.socialLinks.blog = politician.socialLinks.blog?:line."blog"?.trim()
        politician.socialLinks.facebook = politician.socialLinks.facebook?:line."facebook"?.trim()
        politician.socialLinks.twitter = politician.socialLinks.twitter?.decodeTwitter()?:line."twitter"?.trim()?.decodeTwitter()
        politician.socialLinks.linkedIn = politician.socialLinks.linkedIn?:line."linkedin"?.trim()
        politician.socialLinks.googlePlus= politician.socialLinks.googlePlus?:line."googlePlus"?.trim()
        politician.socialLinks.instagram = politician.socialLinks.instagram?:line."instagram"?.trim()
        politician.socialLinks.youtube= politician.socialLinks.youtube?:line."youtubeChannel"?.trim()
        politician.socialLinks.officialWebSite = politician.socialLinks.officialWebSite?:line."officialWebsite"?.trim()
        politician.socialLinks.institutionalWebSite = politician.socialLinks.institutionalWebSite?:line."sourceWebsite"?.trim()
    }

    private void populateExternalPoliticianActivity(KuorumUser politician, def line){
        List<ExternalPoliticianActivity> externalPoliticianActivities = [];
        String prefix = "lastActivity"
        (1..5).each{i->
            ExternalPoliticianActivity epa = new ExternalPoliticianActivity()
            String title = line."${prefix}${i}"
            if (title){
                epa.date = parseDate(line."${prefix}${i}Date", IPDB_DATE_FORMAT)
                epa.title =title
                epa.link =line."${prefix}${i}Link"?.trim()
                epa.actionType =line."${prefix}${i}Action"?.trim()
                epa.outcomeType =line."${prefix}${i}Outcome"?.trim()
                externalPoliticianActivities << epa
            }
        }
        politician.externalPoliticianActivities = externalPoliticianActivities
        sortExternalPoliticianActivity(politician)
    }

    private void populateLeaning(KuorumUser politician, def line){
        if (!politician.politicianLeaning){
            politician.politicianLeaning = new PoliticianLeaning()
        }
        String leanindIndex = line."political_leaning_index"?.trim();
        Double dli = 0.5
        if (leanindIndex){
            dli = Double.parseDouble(leanindIndex)
        }
        politician.politicianLeaning.liberalIndex = Math.round(dli * 100)

    }

    private void populateAddress(KuorumUser politician, def line){
        if (!politician.institutionalOffice){
            politician.institutionalOffice = new OfficeDetails()
        }
        politician.institutionalOffice.address = line."institutionalAddress"?.trim()
        politician.institutionalOffice.fax = line."institutionalFax"?.trim()
        politician.institutionalOffice.assistants = line."assistants"?.trim()
        politician.institutionalOffice.mobile = line."institutionalMobilePhone"?.trim()
        politician.institutionalOffice.telephone = line."institutionalTelephone"?.trim()

        if (!politician.politicalOffice){
            politician.politicalOffice = new OfficeDetails()
        }
        politician.politicalOffice.address = line."electoralAddress"?.trim()
        politician.politicalOffice.fax = line."electoralFax"?.trim()
        politician.politicalOffice.assistants = ""
        politician.politicalOffice.mobile = line."electoralMobile"?.trim()
        politician.politicalOffice.telephone = line."electoralTelephone"?.trim()
    }
    private void populateExtraInfo(KuorumUser politician, def line){
        if (!politician.politicianExtraInfo){
            throw RuntimeException("Este politico no tiene el id de la BBDD")
        }
        politician.politicianExtraInfo.completeName =politician.politicianExtraInfo?.completeName?: line."completeName"?.trim()?:line."name"?.trim()
        politician.politicianExtraInfo.completeName =WordUtils.capitalizeFully(politician.politicianExtraInfo.completeName)
        politician.politicianExtraInfo.birthDate = politician.politicianExtraInfo.birthDate?:parseDate(line."dateOfBirth", "dd/MM/yyyy")
        politician.politicianExtraInfo.birthPlace = politician.politicianExtraInfo.birthPlace?:line."placeOfBirth"?.trim()
        politician.politicianExtraInfo.family = politician.politicianExtraInfo.family?:line."family"?.trim()
    }

    private void populateProfessionalDetails(KuorumUser politician, def line){
        if (!politician.professionalDetails){
            politician.professionalDetails = new ProfessionalDetails()
        }
        politician.professionalDetails.politicalParty = politician.professionalDetails.politicalParty?:line."politicalParty"?.trim()
        politician.professionalDetails.position = politician.professionalDetails.position?:line."position"?.trim()
        politician.professionalDetails.institution = politician.professionalDetails.institution?:line."institution"?.trim()
        politician.professionalDetails.constituency = politician.professionalDetails.constituency?:findConstituency(line)
        politician.professionalDetails.region = politician.professionalDetails.region?:findRegion(line)

    }

    private void populateCareerDetails(KuorumUser politician, def line){
        if (!politician.careerDetails){
            politician.careerDetails = new CareerDetails()
        }
        politician.careerDetails.profession = politician.careerDetails.profession?:line."profession"
        politician.careerDetails.cvLink =politician.careerDetails.cvLink?:line."cvLink"
        politician.careerDetails.declarationLink = politician.careerDetails.declarationLink?:line."declarationLink"
        politician.careerDetails.university = politician.careerDetails.university?:line."university"
        politician.careerDetails.school = politician.careerDetails.school?:line."school"
    }

    private Region findRegion(def line){
        def regionFields = ["region_code_alliance","region_code_nation", "region_code_state", "region_code_county", "region_code_city"]
        findRegionCombiningRegionCodes(regionFields, line)
    }

    private Region findConstituency(def line){
        String constituencyName = line."constituency"?.trim()
        Region constituency = findRegionByName(constituencyName)
        if (!constituency){
            def consituencyFields = ["constituency_code_alliance","constituency_code_nation", "constituency_code_state","constituency_code_county","constituency_code_city"]
            constituency = findRegionCombiningRegionCodes(consituencyFields, line)
        }
        constituency
    }

    private Region findRegionByName(String regionName){
        if (regionName){
            return regionService.findMostAccurateRegion(regionName)
        }else{
            return null
        }
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

    private Date parseDate(String dateText, String format){
        dateText?Date.parse(format,dateText):null
    }
}
