package kuorum.admin

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.domain.DomainService
import kuorum.files.DomainResourcesService
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.admin.KuorumUserEmailSenderCommand
import kuorum.web.admin.KuorumUserRightsCommand
import kuorum.web.admin.domain.*
import kuorum.web.commands.LinkCommand
import kuorum.web.commands.domain.DeleteDomainCommand
import kuorum.web.commands.domain.EditDomainCarouselPicturesCommand
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId
import org.codehaus.groovy.runtime.InvokerHelper
import org.kuorum.rest.model.admin.AdminConfigMailingRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.domain.*
import org.kuorum.rest.model.domain.creation.NewDomainPaymentDataRDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRQDTO
import org.kuorum.rest.model.notification.campaign.config.NewsletterConfigRSDTO
import org.kuorum.rest.model.payment.BillingAmountUsersRangeDTO
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.springframework.web.multipart.commons.CommonsMultipartFile
import payment.campaign.CampaignService
import payment.campaign.NewsletterService

import java.lang.reflect.UndeclaredThrowableException

@Secured(['IS_AUTHENTICATED_FULLY','ROLE_ADMIN'])
class AdminController {

    def indexSolrService
    def springSecurityService

    KuorumUserService kuorumUserService

    NewsletterService newsletterService

    DomainService domainService

    RegisterService registerService

    DomainResourcesService domainResourcesService

    CampaignService campaignService

    KuorumMailService kuorumMailService


//    def afterInterceptor = [action: this.&prepareMenuData]
//    protected prepareMenuData = {model, modelAndView ->
    def afterInterceptor = { model, modelAndView ->

    }
    
    def index() {
        redirect mapping:'adminDomainConfig'
    }

    def domainConfig(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainConfigCommand domainConfigCommand = new DomainConfigCommand()
        domainConfigCommand.name = domainRSDTO.name
        domainConfigCommand.validation = domainRSDTO.validation
        domainConfigCommand.validationPhone = domainRSDTO.validationPhone
        domainConfigCommand.validationCode = domainRSDTO.validationCode
        domainConfigCommand.smsDomainName = domainRSDTO.smsDomainName
        domainConfigCommand.language = domainRSDTO.language
        domainConfigCommand.mainColor = domainRSDTO.mainColor
        domainConfigCommand.mainColorShadowed = domainRSDTO.mainColorShadowed
        domainConfigCommand.secondaryColor = domainRSDTO.secondaryColor
        domainConfigCommand.secondaryColorShadowed = domainRSDTO.secondaryColorShadowed
        domainConfigCommand.facebook = domainRSDTO.social?.facebook
        domainConfigCommand.twitter = domainRSDTO.social?.twitter
        domainConfigCommand.linkedIn = domainRSDTO.social?.linkedIn
        domainConfigCommand.instagram = domainRSDTO.social?.instagram
        domainConfigCommand.youtube = domainRSDTO.social?.youtube
        domainConfigCommand.titleWebFont = KuorumWebFont.build(domainRSDTO.webFontCombinationName)
        [command:domainConfigCommand]

    }

    def domainConfigSave(DomainConfigCommand command){
        if (command.hasErrors()){
            render view:'domainConfig', model:[command:command]
            return
        }
        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.name = command.name
        if (SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")){
            domainRDTO.validation = command.validation?:false
            domainRDTO.validationCode = command.validationCode?:false
            domainRDTO.validationPhone = command.validationPhone?:false
            domainRDTO.smsDomainName = command.smsDomainName?:''
        }
        domainRDTO.language = command.language
        domainRDTO.mainColor = command.mainColor
        domainRDTO.mainColorShadowed = command.mainColorShadowed
        domainRDTO.secondaryColor = command.secondaryColor
        domainRDTO.secondaryColorShadowed = command.secondaryColorShadowed
        domainRDTO.webFontCombinationName = command.titleWebFont.name()
        domainRDTO.social = new SocialRDTO()
        domainRDTO.social.facebook = command.facebook
        domainRDTO.social.twitter = command.twitter
        domainRDTO.social.linkedIn = command.linkedIn
        domainRDTO.social.instagram = command.instagram
        domainRDTO.social.youtube = command.youtube

        domainService.updateConfig(domainRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfig'
    }

    def editLandingInfo(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        DomainLandingCommand domainLandingCommand = new DomainLandingCommand()
        domainLandingCommand.slogan = domainRSDTO.slogan
        domainLandingCommand.subtitle = domainRSDTO.subtitle
        domainLandingCommand.domainDescription = domainRSDTO.domainDescription
        domainLandingCommand.footerLinks = domainRSDTO.footerLinks.collect{new LinkCommand(title: it.key, url: it.value)}
        domainLandingCommand.landingVisibleRoles = domainRSDTO.landingVisibleRoles
        [command:domainLandingCommand]
    }

    def editLandingInfoSave(DomainLandingCommand command){
        if (command.hasErrors()){
            render view:'editLandingInfo', model:[command:command]
            return
        }

        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.slogan = command.slogan
        domainRDTO.subtitle = command.subtitle
        domainRDTO.domainDescription = command.domainDescription
        domainRDTO.footerLinks = command.footerLinks?.findAll{it}?.collectEntries {[(it.title): it.url]}?:null
        domainRDTO.landingVisibleRoles = command.landingVisibleRoles
        domainService.updateConfig(domainRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfigLanding'
    }


//    private static final List campaignRoles = [ UserRoleRSDTO.ROLE_CAMPAIGN_NEWSLETTER, UserRoleRSDTO.ROLE_CAMPAIGN_POST,UserRoleRSDTO.ROLE_CAMPAIGN_DEBATE,UserRoleRSDTO.ROLE_CAMPAIGN_EVENT, UserRoleRSDTO.ROLE_CAMPAIGN_SURVEY, UserRoleRSDTO.ROLE_CAMPAIGN_PETITION,UserRoleRSDTO.ROLE_CAMPAIGN_PARTICIPATORY_BUDGET]
    private static final Map userRoles = [(UserRoleRSDTO.ROLE_ADMIN): 1,(UserRoleRSDTO.ROLE_SUPER_USER): 2,(UserRoleRSDTO.ROLE_USER): 3]

    def editAuthorizedCampaigns() {
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        def globalAuthoritiesCommand = [:]
        List campaignRoles = domainRSDTO.campaignRolesActive
        campaignRoles.each{ campaignRole ->
            globalAuthoritiesCommand.put(campaignRole, getRangeNumberFromDomainAuthority(campaignRole, domainRSDTO.globalAuthorities) )
        }
        [campaignRoles:campaignRoles, userRoles:userRoles.keySet(), globalAuthoritiesCommand:globalAuthoritiesCommand]
    }

    private int getRangeNumberFromDomainAuthority(UserRoleRSDTO campaignRole, Map<UserRoleRSDTO, List<UserRoleRSDTO>>  globalAuthorities){
        userRoles.keySet().collect{globalAuthorities.get(it)?.contains(campaignRole)?userRoles[it]:0}.max()
    }

    private List<UserRoleRSDTO> getListUserRoles(int number){
        userRoles.inject([]) { result, k, v ->
            if (v<= number) result << k
            result
        }
    }

    def updateAuthorizedCampaigns() {
        updateDomainUserRights()
        redirect mapping:'adminAuthorizedCampaigns'
    }


    private boolean updateDomainUserRights(Boolean initLandingVisibleRoles = false){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        domainRSDTO.globalAuthorities.get(UserRoleRSDTO.ROLE_USER)
        Map<UserRoleRSDTO, List<UserRoleRSDTO>> domainGlobalAuthorities = userRoles.keySet().collectEntries{[it, []]}
        List campaignRoles = domainRSDTO.campaignRolesActive
        campaignRoles.each{campaignRole ->
            int val = 0
            try{
                val = Integer.parseInt(params[campaignRole.toString()])
            }catch (Exception e){
                val = 0
            }
            getListUserRoles(val).each{userRole ->
                domainGlobalAuthorities[userRole].add(campaignRole)
            }
        }
        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.globalAuthorities = domainGlobalAuthorities
        if (initLandingVisibleRoles ){
            // Setting as empty the api will execute the default logic
            domainRDTO.landingVisibleRoles = []
        }
        domainService.updateConfig(domainRDTO)
        /* Reauthenticating the user reloads the new roles on his session */
        KuorumUserSession kuorumUserSession = springSecurityService.principal
        springSecurityService.reauthenticate kuorumUserSession.email

        if (!domainGlobalAuthorities[UserRoleRSDTO.ROLE_ADMIN]){
            flash.error = g.message(code:'kuorum.web.admin.domain.AuthorizedCampaignsCommand.error')
        }else{
            flash.message ="Success"
        }
    }

    private DomainRDTO getPopulatedDomainRDTO(){
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        def valid = DomainRDTO.getDeclaredFields().grep {  !it.synthetic }.collect{it.name}
        DomainRDTO domainRDTO = new DomainRDTO(domainRSDTO.properties.findAll{valid.contains(it.key)})
        return domainRDTO
    }

    def editLegalInfo() {
        DomainLegalInfoRSDTO domainLegalInfoRDSTO = domainService.getLegalInfo(CustomDomainResolver.domain)
        EditLegalInfoCommand editLegalInfoCommand = new EditLegalInfoCommand()
        editLegalInfoCommand.address = domainLegalInfoRDSTO?.address
        editLegalInfoCommand.city = domainLegalInfoRDSTO?.city
        editLegalInfoCommand.country = domainLegalInfoRDSTO?.country
        editLegalInfoCommand.domainOwner = domainLegalInfoRDSTO?.domainOwner
        editLegalInfoCommand.fileName = domainLegalInfoRDSTO?.fileName
        editLegalInfoCommand.filePurpose = domainLegalInfoRDSTO?.filePurpose
        editLegalInfoCommand.fileResponsibleEmail = domainLegalInfoRDSTO?.fileResponsibleEmail
        editLegalInfoCommand.fileResponsibleName = domainLegalInfoRDSTO?.fileResponsibleName
        editLegalInfoCommand.customLegalInfo = domainLegalInfoRDSTO?.customLegalInfo
        [command:editLegalInfoCommand]
    }

    def updateLegalInfo(EditLegalInfoCommand command) {
        if (command.hasErrors()){
            render view:'editLegalInfo', model:[command:command]
            return
        }
        DomainLegalInfoRDTO domainLegalInfoRDTO = new DomainLegalInfoRDTO()
        domainLegalInfoRDTO.address = command.address
        domainLegalInfoRDTO.city = command.city
        domainLegalInfoRDTO.country = command.country
        domainLegalInfoRDTO.domainOwner = command.domainOwner
        domainLegalInfoRDTO.fileName = command.fileName
        domainLegalInfoRDTO.filePurpose = command.filePurpose
        domainLegalInfoRDTO.fileResponsibleEmail = command.fileResponsibleEmail
        domainLegalInfoRDTO.fileResponsibleName = command.fileResponsibleName
        domainLegalInfoRDTO.customLegalInfo = command.customLegalInfo
        domainService.updateLegalInfo(domainLegalInfoRDTO)
        flash.message ="Success"
        redirect mapping:'adminDomainConfigLegalInfo'
    }

    def editDomainRelevantCampaigns() {
        List<CampaignRSDTO> domainCampaigns = campaignService.findRelevantDomainCampaigns()
        List<CampaignRSDTO> adminCampaigns = campaignService.findAllCampaigns(WebConstants.FAKE_LANDING_ALIAS_USER)
        Map domainCampaignsId = [first:null, second:null, third:null]
        DomainRSDTO domainRSDTO = CustomDomainResolver.domainRSDTO
        domainCampaignsId.first = domainCampaigns && domainCampaigns.size()>0 ? domainCampaigns.get(0).id:null
        domainCampaignsId.second = domainCampaigns && domainCampaigns.size()>1 ? domainCampaigns.get(1).id:null
        domainCampaignsId.third = domainCampaigns && domainCampaigns.size()>2 ? domainCampaigns.get(2).id:null
        [domainCampaignsId:domainCampaignsId,adminCampaigns:adminCampaigns, starredCampaignId:domainRSDTO.starredCampaignId]
    }

    def updateDomainRelevantCampaigns() {
        List<Long> campaignIds = params.campaignIds.collect{try{Long.parseLong(it)}catch (e){null}}.findAll{it}
        campaignService.updateRelevantDomainCampaigns(campaignIds)

        Long starredCampaignId = null
        try{starredCampaignId = Long.parseLong(params.starredCampaignId)}catch (e){}
        DomainRSDTO domainRSDTO = CustomDomainResolver.domainRSDTO

        if (starredCampaignId != domainRSDTO.starredCampaignId){
            DomainRDTO domainRDTO = getPopulatedDomainRDTO()
            domainRDTO.setStarredCampaignId(starredCampaignId)
            domainService.updateConfig(domainRDTO)
        }
        flash.message="Success"
        redirect mapping:'adminDomainConfigRelevantCampagins'
    }


    def requestedEmailSender(){
        DomainRSDTO domainRSDTO = CustomDomainResolver.domainRSDTO

        KuorumUserSession user = springSecurityService.principal
        NewsletterConfigRSDTO config = newsletterService.findNewsletterConfig(user)
        Boolean isRequested = config.getEmailSenderRequested()
        isRequested = false
        String emailSender = null
//        if (domainRSDTO.customDomainSender){
//            emailSender = "*@${domainRSDTO.domain}"
//        }
        [
                isRequested:isRequested,
                emailSender:emailSender
        ]
    }
    def requestedEmailSenderSend(){
        KuorumUserSession user = springSecurityService.principal
        NewsletterConfigRSDTO config = newsletterService.findNewsletterConfig(user)
        NewsletterConfigRQDTO configRQDTO = new NewsletterConfigRQDTO()
        use(InvokerHelper) {
            configRQDTO.setProperties(config.properties)
        }
        configRQDTO.setEmailSenderRequested(true)
        newsletterService.updateNewsletterConfig(user, configRQDTO)

        kuorumMailService.sendRequestACustomDomainAdmin(user)
        if (request.isXhr()){
            render([msg: ''] as JSON)
        }else{
            flash.message="Custom domain sender requested"
            redirect mapping:'adminRequestEmailSender'
        }
    }


    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def editLogo() {
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def uploadLogo() {

        CommonsMultipartFile customLogo = request.getFile('logo')
        try {
            if (customLogo && !customLogo.empty) {
                domainResourcesService.uploadLogoFile(customLogo.getInputStream())
                flash.message = message(code: 'admin.menu.domainConfig.uploadLogo.success')
            } else {
                flash.error = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
            }
            redirect mapping: 'adminDomainConfig'
        }catch (Exception e) {
            log.error(e)
        }
    }

    def editCarousel() {
        String domain = CustomDomainResolver.domain
        KuorumFile slide1 = domainResourcesService.getSlidePath(domain,1)
        KuorumFile slide2 = domainResourcesService.getSlidePath(domain,2)
        KuorumFile slide3 = domainResourcesService.getSlidePath(domain,3)
        EditDomainCarouselPicturesCommand command = new EditDomainCarouselPicturesCommand()
        command.slideId1 = slide1?.id?.toString()?:null
        command.slideId2 = slide2?.id?.toString()?:null
        command.slideId3 = slide3?.id?.toString()?:null
        [command: command]
    }

    def uploadCarousel(EditDomainCarouselPicturesCommand command) {
        if (command.hasErrors()){
            render view: "editCarousel", model: [command:command]
        }
        else {
            String domain = CustomDomainResolver.domain
            KuorumFile slideFile1 = KuorumFile.get(command.slideId1)
            KuorumFile slideFile2 = KuorumFile.get(command.slideId2)
            KuorumFile slideFile3 = KuorumFile.get(command.slideId3)
            domainResourcesService.uploadCarouselImages(slideFile1, slideFile2, slideFile3, domain)
            flash.message = "Sus imágenes se subieron correctamente"
            redirect mapping: 'adminDomainConfigUploadCarouselImages'
        }
    }

    def deleteDomain(){
        [command:new DeleteDomainCommand()]
    }

    def deleteDomainConfirm(DeleteDomainCommand command){
        if (command.hasErrors()){
            render view: 'deleteDomain', model: [command:command]
        }else{
            domainService.removeDomain(command.domainName)
            redirect mapping:'home'
        }
    }


    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def solrIndex(){
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def fullIndex(){
        def res = indexSolrService.fullIndex()
        render view: '/admin/solrIndex'
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def editUserRights(String userAlias){
        KuorumUserRSDTO user = kuorumUserService.findUserRSDTO(userAlias)
        KuorumUserRightsCommand command = new KuorumUserRightsCommand()
        command.userId = user.id
        command.active = user.active
        [command: command, user:user]
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def updateUserRights(KuorumUserRightsCommand command) {

        if (command.hasErrors()) {
            render view: "editUserRights", model: [command: command]
            return
        }
        KuorumUser user = KuorumUser.get(new ObjectId(command.userId))
        user.enabled = command.active?:false
        user = kuorumUserService.updateUser(user)

        flash.message = "${user.name} updated"

        redirect(mapping: 'editorAdminUserRights', params: user.encodeAsLinkProperties())
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def editDomainEmailSender() {
        DomainRSDTO domainRSDTO = CustomDomainResolver.domainRSDTO
        KuorumUserEmailSenderCommand command = new KuorumUserEmailSenderCommand()
        [
                command     : command,
                requestState: domainRSDTO.customDomainSender
        ]
    }

    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def updateDomainEmailSender(KuorumUserEmailSenderCommand command) {
        AdminConfigMailingRDTO adminRDTO = new AdminConfigMailingRDTO()
        adminRDTO.setDomainName(CustomDomainResolver.domain)
        adminRDTO.setMandrillAppKey(command.mandrillAppKey)

        domainService.updateNewsletterConfig(adminRDTO)

        redirect(mapping: 'adminEditDomainEmailSender')
    }


    @Secured(['IS_AUTHENTICATED_FULLY','ROLE_SUPER_ADMIN'])
    def validateDomain() {

    }


    def designLandingPage(){
        [command:new DomainConfigStep1Command(colorHexCode: CustomDomainResolver.domainRSDTO.mainColor)]
    }

    def saveDesignLandingPage(DomainConfigStep1Command command){
        CommonsMultipartFile customLogo = request.getFile('logo')
        if (!customLogo || customLogo.empty || command.hasErrors()) {
            if (!customLogo || customLogo.empty ){
                command.errors.rejectValue("logoName", "kuorum.web.admin.domain.DomainConfigStep1Command.logoName.nullable")
            }
//            flash.error = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
            render view: "designLandingPage", model: [command:command]
            return
        }

        try {
            domainResourcesService.uploadLogoFile(customLogo.getInputStream())
        }catch (Exception e) {
            log.error(e)
            flash.error = message(code: 'admin.menu.domainConfig.uploadLogo.unsuccess')
            render view: "designLandingPage", model: [command:command]
            return
        }

        String domain = CustomDomainResolver.domain
        KuorumFile slideFile1 = KuorumFile.get(command.slideId1)
        KuorumFile slideFile2 = KuorumFile.get(command.slideId2)
        KuorumFile slideFile3 = KuorumFile.get(command.slideId3)
        domainResourcesService.uploadCarouselImages(slideFile1, slideFile2, slideFile3, domain)

        DomainRDTO domainRDTO = getPopulatedDomainRDTO()
        domainRDTO.slogan = command.slogan
        domainRDTO.name = command.slogan
        domainRDTO.subtitle = command.subtitle
        domainRDTO.mainColor = command.colorHexCode.encodeAsHashtag() // ADD # if its necesary
        domainRDTO.mainColorShadowed = null
        domainRDTO.secondaryColor = null
        domainRDTO.secondaryColorShadowed = null
        domainService.updateConfig(domainRDTO)

        redirect mapping:'adminDomainRegisterStep2'
    }


    def userRights(){
        editAuthorizedCampaigns()
    }

    def saveUserRights(){
        updateDomainUserRights(true)
        redirect mapping: 'dashboard'
    }

    def editDomainPlan(){
        DomainPaymentInfoRSDTO domainPaymentInfo = domainService.getPaymentInfo()
        BillingAmountUsersRangeDTO usersRange = domainPaymentInfo.billingAmountUsersRange
        List<KuorumPaymentPlanDTO> plans = domainService.getPlans(usersRange)
        [domainPaymentInfo:domainPaymentInfo, plans:plans]
    }

    def saveNewDomainPlan(){
        DomainPlanRSDTO plan = DomainPlanRSDTO.valueOf(params.plan)
        DomainPaymentInfoRSDTO domainPaymentInfo = domainService.getPaymentInfo()
        NewDomainPaymentDataRDTO newDomainPaymentDataRDTO = new NewDomainPaymentDataRDTO()
        newDomainPaymentDataRDTO.billingAmountUsersRange = domainPaymentInfo.billingAmountUsersRange
        newDomainPaymentDataRDTO.billingCycle = domainPaymentInfo.billingCycle
        newDomainPaymentDataRDTO.nonce = "NO VALID NONCE - USE DEFAULT ONE"
        newDomainPaymentDataRDTO.promotionalCode = domainPaymentInfo.promotionalCode
        newDomainPaymentDataRDTO.domainPlan = plan
        try{
            domainPaymentInfo = domainService.updatePaymentInfo(newDomainPaymentDataRDTO)
            flash.message="Plan changed. Please review the new rigths"
            redirect mapping:'adminAuthorizedCampaigns'
        }catch (UndeclaredThrowableException e){
            String msgError = "Error updating participatory budget status"
            if (e.undeclaredThrowable.cause instanceof KuorumException){
                KuorumException ke = e.undeclaredThrowable.cause
                msgError = message(code: ke.errors[0].code)
            }
            flash.error=msgError
            redirect mapping:'adminDomainConfigPlan'
        }
    }
}
