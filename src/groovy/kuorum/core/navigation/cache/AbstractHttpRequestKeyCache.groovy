package kuorum.core.navigation.cache

import kuorum.core.customDomain.CustomDomainResolver
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo

abstract class AbstractHttpRequestKeyCache implements ServletRequestResponseCache {

    private static final String USER_ALIAS_PARAM_NAME = "userAlias"
    private static final String CAMPAIGN_ID_PARAM_NAME = "campaignId"
    public static final String CONTEST_ID_PARAM_NAME = "contestId"
    public static final String PARTICIPATORY_BUDGET_ID_PARAM_NAME = "participatoryBudgetId"

    String buildKey(UrlMappingInfo urlMappingInfo) {
        return buildKey(urlMappingInfo, null)
    }

    String buildKey(UrlMappingInfo urlMappingInfo, Locale locale) {
        String key = buildGlobalKey()
        key += getPropertyOrEmpty(urlMappingInfo, USER_ALIAS_PARAM_NAME)
        key += getCampaignId(urlMappingInfo)
        if (locale) {
            key += locale.getLanguage()
        }
        return key
    }

    String buildGlobalKey() {
        return CustomDomainResolver.domain
    }

    private Object getPropertyOrEmpty(UrlMappingInfo urlMappingInfo, String parameterName) {
        Map parameters = urlMappingInfo.getParameters()
        return parameters.containsKey(parameterName) ? parameters.get(parameterName) : ""
    }

    private Object getCampaignId(UrlMappingInfo urlMappingInfo) {
        Map parameters = urlMappingInfo.getParameters()
        if(parameters.containsKey(CONTEST_ID_PARAM_NAME)){
            return parameters.get(CONTEST_ID_PARAM_NAME)
        }else if(parameters.containsKey(PARTICIPATORY_BUDGET_ID_PARAM_NAME)){
            return parameters.get(PARTICIPATORY_BUDGET_ID_PARAM_NAME)
        }else {
            return getPropertyOrEmpty(urlMappingInfo,CAMPAIGN_ID_PARAM_NAME)
        }
    }

}
