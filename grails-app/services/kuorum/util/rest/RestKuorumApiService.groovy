package kuorum.util.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.*
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

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
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


    public enum ApiMethod{
        USER_STATS_REPUTATION               ('/user/{userId}/stats/reputation'),
        USER_STATS_REPUTATION_EVOLUTION     ('/user/{userId}/stats/reputation/evolution'),

        USER_CAUSES_SUPPORTED   ('/user/{userId}/causes/support'),
        USER_CAUSES_SUPPORT     ('/user/{userId}/causes/support/{causeName}'),
        USER_CAUSES_DEFENDED    ('/user/{userId}/causes/defend'),
        USER_CAUSES_DEFEND      ('/user/{userId}/causes/defend/{causeName}'),

        USER                    ('/user/{userId}/'),
        USER_NEWS               ('/user/{userId}/news/'),

        USER_DATA               ('/user/{userId}/data/'),

        USER_CONTACTS           ('/contacts/{userId}'),
        USER_CONTACT            ('/contacts/{userId}/{contactId}'),
        USER_CONTACT_TAGS       ('/contacts/{userId}/tags'),
        USER_CONTACT_FILTERS    ('/contacts/{userId}/filters'),
        USER_CONTACT_FILTER     ("/contacts/{userId}/filters/{filterId}"),
        USER_CONTACT_FOLLOWER   ("/contacts/{userId}/follower"),
        USER_CONTACT_SUBSCRIBE  ("/contacts/{userId}/suscribe"),
        USER_CONTACT_REPORT     ("/contacts/{userId}/report"),

        CAUSE_OPERATIONS        ("/cause/{causeName}"),
        CAUSE_USERS_SUPPORTING  ("/cause/{causeName}/supporting"),
        CAUSE_SUGGESTIONS       ("/cause/suggest"),

        CUSTOMER_ACTIVE_PLANS  ("/customer/payment/plans"),
        CUSTOMER_PAYMENT_PLANS  ("/customer/payment/{userId}/plans"),
        CUSTOMER_PAYMENT_PLAN  ("/customer/payment/{userId}/plans/data"),
        CUSTOMER_PAYMENT_SUBSCRIPTION  ("/customer/payment/{userId}"),
        CUSTOMER_PAYMENT_TOKEN  ("/customer/payment/{userId}/paymentMethod"),
        PROMOTIONAL_CODES       ("/customer/payment/promotions/{promotionCode}/"),
        PROMOTIONAL_CODE_ADD    ("/customer/payment/promotions/{promotionCode}/{userId}"),

        REGION_GET      ("/geolocation/get"),
        REGION_SUGGEST  ("/geolocation/suggest"),
        REGION_FIND     ("/geolocation/find"),

        ACCOUNT_INFO                ("/notification/mailing/{userAlias}"),
        ACCOUNT_MAILS               ("/notification/mailing/{userAlias}/emails"),
        ACCOUNT_MAILS_SEND          ("/notification/mailing/{userAlias}/send"),
        ACCOUNT_NOTIFICATIONS       ("/notification/{userAlias}"),
        ACCOUNT_NOTIFICATIONS_CONFIG("/notification/{userAlias}/config"),

        ACCOUNT_MASS_MAILINGS                   ("/communication/massmailing/{userAlias}"),
        ACCOUNT_MASS_MAILING                    ("/communication/massmailing/{userAlias}/{campaignId}"),
        ACCOUNT_MASS_MAILING_SEND               ("/communication/massmailing/{userAlias}/{campaignId}/send"),
        ACCOUNT_MASS_MAILING_TRACKING           ("/communication/massmailing/{userAlias}/{campaignId}/trackingMails"),
        ACCOUNT_MASS_MAILING_REPORT             ("/communication/massmailing/{userAlias}/{campaignId}/trackingMails/report"),
        ACCOUNT_MASS_MAILING_CAMPAIGNS_REPORT   ("/communication/massmailing/{userAlias}/report"),
        ACCOUNT_MASS_MAILING_CONFIG             ("/communication/massmailing/{userAlias}/config"),

        ACCOUNT_DEBATES_ALL     ("/communication/debate/"),
        ACCOUNT_DEBATES         ("/communication/debate/{userAlias}"),
        ACCOUNT_DEBATE          ("/communication/debate/{userAlias}/{debateId}"),
        ACCOUNT_DEBATE_REPORT   ("/communication/debate/{userAlias}/{debateId}/report"),
        ACCOUNT_DEBATE_PROPOSALS("/communication/debate/{userAlias}/{debateId}/proposal"),
        ACCOUNT_DEBATE_PROPOSAL ("/communication/debate/{userAlias}/{debateId}/proposal/{proposalId}"),
        ACCOUNT_DEBATE_PROPOSAL_LIKE     ("/communication/debate/{userAlias}/{debateId}/proposal/{proposalId}/likes"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENTS      ("/communication/debate/{userAlias}/{debateId}/proposal/{proposalId}/comment"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENT       ("/communication/debate/{userAlias}/{debateId}/proposal/{proposalId}/comment/{commentId}"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENT_VOTE  ("/communication/debate/{userAlias}/{debateId}/proposal/{proposalId}/comment/{commentId}/vote"),

        ACCOUNT_POSTS_ALL       ("/communication/post/"),
        ACCOUNT_POSTS           ("/communication/post/{userAlias}"),
        ACCOUNT_POST            ("/communication/post/{userAlias}/{postId}"),
        ACCOUNT_POST_LIKES      ("/communication/post/{userAlias}/{postId}/likes"),

        ACCOUNT_EVENTS("/communication/event/{userAlias}"),
        ACCOUNT_EVENT("/communication/event/{userAlias}/{eventId}"),
        ACCOUNT_EVENT_ADD_ASSISTANT("/communication/event/{userAlias}/{eventId}/assistant/{assistantAlias}"),
        ACCOUNT_EVENT_CHECK_IN("/communication/event/{userAlias}/{eventId}/checkIn"),
        ACCOUNT_EVENT_REPORT("/communication/event/{userAlias}/{eventId}/assistant/report"),

        USER_CONTACTS_CAMPAIGNS_ALL ("/user/{userId}/dashboard/campaigns"),
        USER_CONTACTS_POSTS_ALL ("/user/{userId}/dashboard/post"),
        USER_CONTACTS_DEBATES_ALL   ("/user/{userId}/dashboard/debate"),


        ADMIN_MAILS_SEND("/admin/notification/mailing/send"),
        ADMIN_USER_CONFIG_SENDER("/admin/{userAlias}/config/mailing");

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

    private RESTClient getRestMailKuorumServices(TypeReference clazz){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        EncoderRegistry encoderRegistry = mailKuorumServices.getEncoder();
        encoderRegistry.putAt(groovyx.net.http.ContentType.JSON, {it ->
//            def builder = new groovy.json.JsonBuilder(); // Not use JacksonAnnotations
//            builder.content = it
//            String rawJson = builder.toString();

            ObjectMapper builder = new ObjectMapper()
//            String rawJson = builder.valueToTree(it).toString()
            String rawJson = builder.writeValueAsString(it)

            InputStreamEntity res = new InputStreamEntity(new ByteArrayInputStream(rawJson.getBytes(StandardCharsets.UTF_8)));
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
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
