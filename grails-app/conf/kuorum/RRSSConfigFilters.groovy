package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.solr.SolrType
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO

class RRSSConfigFilters {

    GrailsApplication grailsApplication

    def filters = {
        all(controller: '*', action: '*') {
            before = {

            }
            after = { Map model ->
                if (model!= null){
                    model.put("_facebookConfig",getFacebookConfig())
                    model.put("_googleConfig",getGoogleConfig())
                    model.put("_googleCaptchaKey",getGoogleCaptchaKey())
                    model.put("_domain",CustomDomainResolver.domain)
                    model.put("_social", CustomDomainResolver.domainRSDTO?.social?:null)
                    model.put("_domainName", CustomDomainResolver.domainRSDTO?.name?:"")
                    model.put("_domainResourcesPath", CustomDomainResolver.domainRSDTO?.basicRootUrlStaticResources?:"")
                    model.put("_domainActiveCampaigns", getActiveCampaigns())
                }
            }
            afterView = { Exception e ->

            }
        }
    }

    private def getFacebookConfig(){
        return grailsApplication.config.oauth.providers.facebook
    }
    private def getGoogleConfig(){
        return grailsApplication.config.oauth.providers.google
    }

    private def getGoogleCaptchaKey(){
        return grailsApplication.config.recaptcha.providers.google.siteKey
    }

    private List<SolrType> getActiveCampaigns(){
        Map globalAuthorities = CustomDomainResolver.domainRSDTO?.globalAuthorities
        if (globalAuthorities){
            List<UserRoleRSDTO> adminActiveRoles = globalAuthorities.get(UserRoleRSDTO.ROLE_ADMIN);
            List<SolrType> searchableCampaigns =  adminActiveRoles
                    .collect{it.toString()}
                    .findAll{it.startsWith("ROLE_CAMPAIGN")}
                    .collect{it.replace("ROLE_CAMPAIGN_","")}
                    .collect{SolrType.valueOf(it)}
            return (searchableCampaigns - SolrType.DISTRICT_PROPOSAL).sort { a, b -> a.ordinal() <=> b.ordinal() };
        }else{
            return []
        }
    }
}
