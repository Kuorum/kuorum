package kuorum.util.rest

import groovyx.net.http.RESTClient
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

    def get(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
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

    def put(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def post(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body) {
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.post(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                body:body,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }


    public enum ApiMethod{
        USER_STATS_LEANING_INDEX('/user/{userId}/stats/leaning-index'),
        CAUSE_OPERATIONS("/cause/{causeName}"),
        CAUSE_SUPPORT ("/cause/support/{userId}"),
        CAUSE_SUPPORT_OPERATIONS ("/cause/support/{causeName}/{userId}"),
        CAUSE_SUGGESTIONS ("/cause/suggest"),
        CAUSE_SUGGESTIONS_USER ("/cause/suggest/{userId}"),
        CAUSE_SUGGESTIONS_USER_DISCARD('/cause/suggest/{userId}/{causeName}'),

        CAUSE_POLITICIANS ("/cause/{causeName}/politicians"),

        ACCOUNT_INFO("/notification/mailing/{userAlias}"),
        ACCOUNT_MAILS("/notification/mailing/{userAlias}/emails");

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
}
