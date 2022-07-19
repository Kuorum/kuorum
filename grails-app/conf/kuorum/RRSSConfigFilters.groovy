package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.solr.SolrType
import kuorum.domain.DomainService
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.kuorum.rest.model.domain.DomainPrivacyRDTO
import org.kuorum.rest.model.domain.DomainTypeRSDTO
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO

class RRSSConfigFilters {

    GrailsApplication grailsApplication

    DomainService domainService

    def filters = {
        rrssFilter(controller: '*', action: '*') {
            before = {
//                log.debug("rrssConfig")
            }
            after = { Map model ->
                if (model != null) {
                    model.put("_facebookConfig", getFacebookConfig())
//                    model.put("_googleConfig",getGoogleConfig())
                    model.put("_googleCaptchaKey", getGoogleCaptchaKey())
                    model.put("_googleJsAPIKey", getGoogleJsAPIKey())
                    model.put("_domain", CustomDomainResolver.domain)
                    model.put("_social", CustomDomainResolver.domainRSDTO?.social ?: null)
                    model.put("_domainName", CustomDomainResolver.domainRSDTO?.name ?: "")
                    model.put("_domainResourcesPath", CustomDomainResolver.domainRSDTO?.basicRootUrlStaticResources ?: "")
                    model.put("_isActiveTour", (CustomDomainResolver.domainRSDTO.getDomainTypeRSDTO() != DomainTypeRSDTO.SURVEY)
                            || SpringSecurityUtils.ifAllGranted("ROLE_ADMIN")  ?: false)
                    model.put("_isSurveyPlatform", domainService.isSurveyPlatform())
                    model.put("_isPrivatePlatform", CustomDomainResolver.domainRSDTO.getDomainPrivacy() != DomainPrivacyRDTO.PUBLIC)
                    model.put("_showSocialButtons", !model.get("_isPrivatePlatform"))
                    model.put("_domainActiveCampaigns", getActiveCampaigns())
                    model.put("_domainValidations", [
                            census    : CustomDomainResolver.domainRSDTO.validationCensus,
                            phone     : CustomDomainResolver.domainRSDTO.validationPhone,
                            customCode: CustomDomainResolver.domainRSDTO.validationCode
                    ])
                }
            }
            afterView = { Exception e ->

            }
        }
    }

    private def getFacebookConfig() {
        return grailsApplication.config.oauth.providers.facebook
    }

    private def getGoogleConfig() {
        return grailsApplication.config.oauth.providers.google
    }

    private def getGoogleCaptchaKey() {
        return grailsApplication.config.recaptcha.providers.google.siteKey
    }

    private def getGoogleJsAPIKey() {
        return grailsApplication.config.kuorum.keys.google.api.js
    }

    private List<SolrType> getActiveCampaigns() {
        Map globalAuthorities = CustomDomainResolver.domainRSDTO?.globalAuthorities
        if (globalAuthorities) {
            List<UserRoleRSDTO> adminActiveRoles = globalAuthorities.get(UserRoleRSDTO.ROLE_ADMIN)
            List<SolrType> searchableCampaigns = adminActiveRoles
                    .collect { it.toString() }
                    .findAll { it.startsWith("ROLE_CAMPAIGN") }
                    .collect { it.replace("ROLE_CAMPAIGN_", "") }
                    .collect { SolrType.valueOf(it) }
            return (searchableCampaigns - [SolrType.DISTRICT_PROPOSAL, SolrType.NEWSLETTER]).sort { a, b -> a.ordinal() <=> b.ordinal() }
        } else {
            return []
        }
    }
}
