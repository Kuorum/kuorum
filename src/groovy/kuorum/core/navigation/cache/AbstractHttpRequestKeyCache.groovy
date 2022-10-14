package kuorum.core.navigation.cache

import kuorum.core.customDomain.CustomDomainResolver
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo

abstract class AbstractHttpRequestKeyCache implements ServletRequestResponseCache {

    private static final String USER_ALIAS_PARAM_NAME = "userAlias"
    private static final String CAMPAIGN_ID_PARAM_NAME = "campaignId"
    public static final String CONTEST_ID_PARAM_NAME = "contestId"
    public static final String PARTICIPATORY_BUDGET_ID_PARAM_NAME = "participatoryBudgetId"
    public static final String PARENT_SUFIX = "children"

    String buildKey(UrlMappingInfo urlMappingInfo) {
        return buildKey(urlMappingInfo, null)
    }

    String buildParentKey(UrlMappingInfo urlMappingInfo) {
        def parentId = getParentCampaignId(urlMappingInfo)
        if(parentId) {
            String key = buildGlobalKey()
            key += getPropertyOrEmpty(urlMappingInfo, USER_ALIAS_PARAM_NAME)
            key += parentId
            key += PARENT_SUFIX
            return key
        }
    }


    String buildKey(UrlMappingInfo urlMappingInfo, Locale locale) {
        String key = buildGlobalKey()
        key += getPropertyOrEmpty(urlMappingInfo, USER_ALIAS_PARAM_NAME)
        key += getPropertyOrEmpty(urlMappingInfo,CAMPAIGN_ID_PARAM_NAME)
        if (locale) {
            key += locale.getLanguage()
        }
        return key
    }

    String buildGlobalKey() {
        return CustomDomainResolver.domain
    }

    private String getPropertyOrEmpty(UrlMappingInfo urlMappingInfo, String parameterName) {
        Map parameters = urlMappingInfo.getParameters()
        return parameters.containsKey(parameterName) ? parameters.get(parameterName) : ""
    }

    private String getParentCampaignId(UrlMappingInfo urlMappingInfo) {
        Map parameters = urlMappingInfo.getParameters()
        if(parameters.containsKey(CONTEST_ID_PARAM_NAME)){
            return parameters.get(CONTEST_ID_PARAM_NAME) as String
        }else if(parameters.containsKey(PARTICIPATORY_BUDGET_ID_PARAM_NAME)){
            return parameters.get(PARTICIPATORY_BUDGET_ID_PARAM_NAME) as String
        }else {
            return null
        }
    }

}
