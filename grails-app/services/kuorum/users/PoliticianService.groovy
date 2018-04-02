package kuorum.users

import com.mongodb.DBCursor
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.Region
import kuorum.RegionService
import kuorum.causes.CausesService
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailService
import kuorum.notifications.NotificationService
import kuorum.solr.IndexSolrService
import kuorum.users.extendedPoliticianData.*
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
    KuorumUserService kuorumUserService
    KuorumUserAuditService kuorumUserAuditService

    private static final String IPDB_DATE_FORMAT = "dd/MM/yyyy HH:mm"


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

    KuorumUser createPoliticianFromCSV(def line) {

        KuorumUser politician = findOrRecoverPolitician(line)
        populateBasicData(politician, line)
        populateImages(politician, line)
        populateProfessionalDetails(politician, line)
        populateCareerDetails(politician, line)
        populateSocialLinks(politician, line)
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
        updatePoliticianCauses(politician, tags, false)
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

    private KuorumUser findOrRecoverPolitician(def line){
        String email = generateEmail(null, line)
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
        politician.personalData.telephone = politician?.personalData?.telephone?:line."phone"?.trim()
        politician.alias = generateAlias(politician, line)
        politician.language = AvailableLanguage.fromLocaleParam(line."language"?:"en")?:AvailableLanguage.en_EN
        if (!politician.save(flush: true)){
            throw new KuorumException("Basic data not porvided, ${politician.errors}")
        }
    }

    private String generateEmail(KuorumUser politician, def line){
        String twitterAlias = generateAlias(politician, line)
        String email = politician?.email?:line."email"?:"info+${twitterAlias?:line."name"?.encodeAsMD5()}@kuorum.org"
        email.toLowerCase();
    }

    private String generateAlias(KuorumUser politician, def line){
        String twitterAlias = line."twitter"?.trim()?.encodeAsTwitter()
        if (twitterAlias){
            twitterAlias = twitterAlias.substring(1)
        }
        String id = politician?.id?.toString()
        if (!id){
            id = ObjectId.get().toString()
        }
        String alias = politician?.alias?:twitterAlias?:id.substring(id.length() - 15)
        alias.toLowerCase()
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
        politician.professionalDetails.region = politician.professionalDetails.region?:findRegion(line)
        politician.professionalDetails.constituency = politician.professionalDetails.constituency?:findConstituency(line,politician.professionalDetails.region)

    }

    private void populateCareerDetails(KuorumUser politician, def line){
        if (!politician.careerDetails){
            politician.careerDetails = new CareerDetails()
        }
        politician.careerDetails.profession = politician.careerDetails.profession?:line."profession"
        politician.careerDetails.cvLink =politician.careerDetails.cvLink?:fileService.createExternalFile(politician, line."cvLink", FileGroup.PDF, FileType.AMAZON_IMAGE)
        politician.careerDetails.declarationLink = politician.careerDetails.declarationLink?:fileService.createExternalFile(politician, line."declarationLink", FileGroup.PDF, FileType.AMAZON_IMAGE)
        politician.careerDetails.university = politician.careerDetails.university?:line."university"
        politician.careerDetails.school = politician.careerDetails.school?:line."school"
    }

    private Region findRegion(def line){
        def regionFields = ["region_code_alliance","region_code_nation", "region_code_state", "region_code_county", "region_code_city"]
        findRegionCombiningRegionCodes(regionFields, line)
    }

    private Region findConstituency(def line, Region country){
        def consituencyFields = ["constituency_code_alliance","constituency_code_nation", "constituency_code_state","constituency_code_county","constituency_code_city"]
        Region constituency = findRegionCombiningRegionCodes(consituencyFields, line)

        if (!constituency){
            String constituencyName = line."constituency"?.trim()
            constituency = findRegionByName(constituencyName, country)
        }
        constituency
    }

    private Region findRegionByName(String regionName, Region country){
        if (regionName){
            return regionService.findMostAccurateRegion(regionName, country)
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
