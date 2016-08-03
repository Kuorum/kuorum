package kuorum.util.rest

import grails.converters.JSON
import groovyx.net.http.RESTClient
import kuorum.core.exception.KuorumException
import org.springframework.beans.factory.annotation.Value

class RestKuorumApiService {


    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    @Value('${kuorum.rest.apiKey}')
    String kuorumRestApiKey


    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath, params);
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    public enum ApiMethod{
        USER_STATS_LEANING_INDEX            ('/user/{userId}/stats/leaning-index'),
        USER_STATS_REPUTATION               ('/user/{userId}/stats/reputation'),
        USER_STATS_REPUTATION_EVOLUTION     ('/user/{userId}/stats/reputation/evolution'),

        USER_CAUSES_SUPPORTED   ('/user/{userId}/causes/support'),
        USER_CAUSES_SUPPORT     ('/user/{userId}/causes/support/{causeName}'),
        USER_CAUSES_DEFENDED    ('/user/{userId}/causes/defend'),
        USER_CAUSES_DEFEND      ('/user/{userId}/causes/defend/{causeName}'),

        USER_NEWS               ('/user/{userId}/news/'),

        USER_CONTACT_ADD_BULK   ('/contacts/{userId}'),

        CAUSE_OPERATIONS        ("/cause/{causeName}"),
        CAUSE_USERS_DEFENDING   ("/cause/{causeName}/defending"),
        CAUSE_USERS_SUPPORTING  ("/cause/{causeName}/supporting"),
        CAUSE_SUGGESTIONS       ("/cause/suggest"),

        REGION_GET      ("/geolocation/get"),
        REGION_SUGGEST  ("/geolocation/suggest"),
        REGION_FIND     ("/geolocation/find"),

        ACCOUNT_INFO("/notification/mailing/{userAlias}"),
        ACCOUNT_MAILS("/notification/mailing/{userAlias}/emails"),
        ACCOUNT_MAILS_SEND("/notification/mailing/{userAlias}/send"),

        ADMIN_MAILS_SEND("/admin/notification/mailing/send");

        String url;
        ApiMethod(String url){
            this.url = url;
        }

        String buildUrl(String contextPath, Map<String,String> params){
            String builtUrl = url
            params.each{ k, v -> builtUrl = builtUrl.replaceAll("\\{${k}}", v) }
            contextPath+builtUrl
        }
    }

    def get(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        mailKuorumServices.handler.failure = { resp, data ->
            throw new KuorumException("No found - ${apiMethod}")
        }
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def patch(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.patch(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def put(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body = null) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                body: body,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }


    def post(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.post(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token": kuorumRestApiKey],
                query: query,
                body: body,
                requestContentType: groovyx.net.http.ContentType.JSON
        )
        return response
    }
}
