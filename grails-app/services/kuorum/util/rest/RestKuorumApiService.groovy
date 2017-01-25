package kuorum.util.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.ParserRegistry
import groovyx.net.http.RESTClient
import kuorum.core.exception.KuorumException
import org.apache.commons.io.IOUtils
import org.apache.http.entity.InputStreamEntity
import org.springframework.beans.factory.annotation.Value

import java.nio.charset.StandardCharsets

class RestKuorumApiService {


    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    @Value('${kuorum.rest.apiKey}')
    String kuorumRestApiKey

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) {
        RESTClient mailKuorumServices = new RESTClient(kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath, params)
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token": kuorumRestApiKey],
                query: query,
                requestContentType: ContentType.JSON
        )
        return response
    }

    public enum ApiMethod{
        USER_STATS_REPUTATION               ('/user/{userId}/stats/reputation'),
        USER_STATS_REPUTATION_EVOLUTION     ('/user/{userId}/stats/reputation/evolution'),

        USER_CAUSES_SUPPORTED   ('/user/{userId}/causes/support'),
        USER_CAUSES_SUPPORT     ('/user/{userId}/causes/support/{causeName}'),
        USER_CAUSES_DEFENDED    ('/user/{userId}/causes/defend'),
        USER_CAUSES_DEFEND      ('/user/{userId}/causes/defend/{causeName}'),

        USER_NEWS               ('/user/{userId}/news/'),

        USER_CONTACTS           ('/contacts/{userId}'),
        USER_CONTACT            ('/contacts/{userId}/{contactId}'),
        USER_CONTACT_TAGS       ('/contacts/{userId}/tags'),
        USER_CONTACT_FILTERS    ('/contacts/{userId}/filters'),
        USER_CONTACT_FILTER     ("/contacts/{userId}/filters/{filterId}"),
        USER_CONTACT_FOLLOWER   ("/contacts/{userId}/follower"),
        USER_CONTACT_SUBSCRIBE   ("/contacts/{userId}/suscribe"),

        CAUSE_OPERATIONS        ("/cause/{causeName}"),
        CAUSE_USERS_DEFENDING   ("/cause/{causeName}/defending"),
        CAUSE_USERS_SUPPORTING  ("/cause/{causeName}/supporting"),
        CAUSE_SUGGESTIONS       ("/cause/suggest"),

        PROMOTIONAL_CODES       ("/promotions/{promotionCode}/"),
        PROMOTIONAL_CODE_ADD    ("/promotions/{promotionCode}/{userId}"),

        REGION_GET      ("/geolocation/get"),
        REGION_SUGGEST  ("/geolocation/suggest"),
        REGION_FIND     ("/geolocation/find"),

        ACCOUNT_INFO                ("/notification/mailing/{userAlias}"),
        ACCOUNT_MAILS               ("/notification/mailing/{userAlias}/emails"),
        ACCOUNT_MAILS_SEND          ("/notification/mailing/{userAlias}/send"),

        ACCOUNT_MASS_MAILINGS           ("/communication/massmailing/{userAlias}"),
        ACCOUNT_MASS_MAILING            ("/communication/massmailing/{userAlias}/{campaignId}"),
        ACCOUNT_MASS_MAILING_SEND       ("/communication/massmailing/{userAlias}/{campaignId}/send"),
        ACCOUNT_MASS_MAILING_TRACKING   ("/communication/massmailing/{userAlias}/{campaignId}/trackingMails"),

        ACCOUNT_DEBATES         ("/communication/debate/{userAlias}"),
        ACCOUNT_DEBATE          ("/communication/debate/{userAlias}/{debateId}"),
        ACCOUNT_DEBATE_PROPOSAL ("/communication/debate/{userAlias}/{debateId}/proposal"),

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

    def get(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def patch(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) {
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

    def put(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap) {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);
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


    def post(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap) {

        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

//        encoderRegistry
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

    private RESTClient getRestMailKuorumServices(TypeReference clazz){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        EncoderRegistry encoderRegistry = mailKuorumServices.getEncoder();
        encoderRegistry.putAt(groovyx.net.http.ContentType.JSON, {it ->
            def builder = new groovy.json.JsonBuilder();
            builder.content = it
            InputStreamEntity res = new InputStreamEntity(new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8)));
            res.setContentType(groovyx.net.http.ContentType.JSON.toString())
            return res;
        })

        ParserRegistry parserRegistry = mailKuorumServices.getParser()
        parserRegistry.putAt(groovyx.net.http.ContentType.JSON, { HttpResponseDecorator resp ->
            def obj = null
            if (resp.status ==200){
                if(clazz != null){
                    String jsonString = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
                    ObjectMapper objectMapper = new ObjectMapper()
                    obj = objectMapper.readValue(jsonString, clazz);
                }
                return obj
            }else{
                throw new KuorumException("No found")
            }
        })

        mailKuorumServices.handler.failure = { resp, data ->
            throw new KuorumException("No found")
        }
        return mailKuorumServices

    }
}
