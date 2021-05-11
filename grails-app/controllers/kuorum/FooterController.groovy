package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import org.kuorum.rest.model.domain.DomainLegalInfoRSDTO
import org.springframework.context.i18n.LocaleContextHolder

class FooterController {

    DomainService domainService;

    def privacyPolicy(){
        DomainLegalInfoRSDTO legalInfo = prepareLegalInfo()
        [legalInfo : legalInfo]
    }
    def termsUse(){
        DomainLegalInfoRSDTO legalInfo = prepareLegalInfo()
        [legalInfo : legalInfo]
    }

    // PREPARE EMPTY PARAMS
    private DomainLegalInfoRSDTO prepareLegalInfo(){
        DomainLegalInfoRSDTO legalInfo = domainService.getLegalInfo(CustomDomainResolver.domain)
        if (!legalInfo){
            legalInfo = new DomainLegalInfoRSDTO();
        }

        legalInfo.domainName = legalInfo.domainName?:g.message(code:'footer.menu.default.legalInfo.domainName')
        legalInfo.fileResponsibleEmail = legalInfo.fileResponsibleEmail?:g.message(code:'footer.menu.default.legalInfo.fileResponsibleEmail')
        legalInfo.fileResponsibleName = legalInfo.fileResponsibleName?:g.message(code:'footer.menu.default.legalInfo.fileResponsibleName')
        legalInfo.filePurpose = legalInfo.filePurpose?:g.message(code:'footer.menu.default.legalInfo.filePurpose')
        legalInfo.fileName= legalInfo.fileName?:g.message(code:'footer.menu.default.legalInfo.fileName')
        legalInfo.country= legalInfo.country?:g.message(code:'footer.menu.default.legalInfo.country')
        legalInfo.city= legalInfo.city?:g.message(code:'footer.menu.default.legalInfo.city')
        legalInfo.address= legalInfo.address?:g.message(code:'footer.menu.default.legalInfo.address')
        legalInfo.domainOwner= legalInfo.domainOwner?:g.message(code:'footer.menu.default.legalInfo.domainOwner')
        legalInfo.privacyPolicy = legalInfo.privacyPolicy?:null

        return legalInfo
    }

    def footerUserGuides() {
        Locale locale = LocaleContextHolder.getLocale();
        String langGuides = "en"
        String imgFile1 = "guide1_"
        String imgFile2 = "guide2_"
        if (locale.getLanguage() == "es"){
            langGuides = "es"
        }
        [
                langGuides:langGuides,
                imgFile1:imgFile1+langGuides+".png",
                imgFile2:imgFile2+langGuides+".png"

        ]
    }
}
