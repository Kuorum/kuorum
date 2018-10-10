package kuorum.util.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.*
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import org.apache.commons.io.IOUtils
import org.apache.http.entity.InputStreamEntity
import org.kuorum.rest.model.error.RestServiceError
import org.springframework.beans.factory.annotation.Value

import java.nio.charset.StandardCharsets

class RestKuorumApiService {


    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

//    @Value('${kuorum.rest.apiKey}')
//    String kuorumRestApiKey

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) throws KuorumException{
        RESTClient mailKuorumServices = new RESTClient(kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath, params)
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token": CustomDomainResolver.apiToken],
                query: query,
                requestContentType: ContentType.JSON
        )
        return response
    }

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":CustomDomainResolver.apiToken],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def get(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap, String adminApiKey = null) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

        String apiKey = adminApiKey?:CustomDomainResolver.apiToken
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":apiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def patch(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) throws KuorumException{
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.patch(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":CustomDomainResolver.apiToken],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def put(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap, String adminApiKey = null) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);
        String apiKey = adminApiKey?:CustomDomainResolver.apiToken
        String path = apiMethod.buildUrl(apiPath,params);
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":apiKey],
                query:query,
                body: body,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def post(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap) throws KuorumException{

        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap);

//        encoderRegistry
        String path = apiMethod.buildUrl(apiPath,params);

        def response = mailKuorumServices.post(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token": CustomDomainResolver.apiToken],
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
                    objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"))
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    obj = objectMapper.readValue(jsonString, clazz);
                }
                return obj
            }else{
                String jsonString = IOUtils.toString(resp.getEntity().getContent(), "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper()
                objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"))
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                RestServiceError serviceError = objectMapper.readValue(jsonString, RestServiceError.class);
                throw new KuorumException(serviceError.message, "error.api.${serviceError.code}", new ArrayList(serviceError.errorData.values()))
            }
        })

        mailKuorumServices.handler.failure = { resp, data ->
            throw new KuorumException("No found")
        }
        return mailKuorumServices

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
        USER_DOMAIN_VALIDATION  ('/user/{userId}/valid'),

        USER_CONTACTS           ('/contacts/{userId}'),
        USER_CONTACT            ('/contacts/{userId}/{contactId}'),
        USER_CONTACT_TAGS       ('/contacts/{userId}/tags'),
        USER_CONTACT_FILTERS    ('/contacts/{userId}/filters'),
        USER_CONTACT_FILTER     ("/contacts/{userId}/filters/{filterId}"),
        USER_CONTACT_FOLLOWER   ("/contacts/{userId}/follower"),
        USER_CONTACT_SUBSCRIBE  ("/contacts/{userId}/suscribe"),
        USER_CONTACT_REPORT     ("/contacts/{userId}/report"),
        USER_CONTACT_SOCIAL_IMPORT("/contacts/social/{provider}/request"),

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

        DOMAIN          ("/domain/"),
        DOMAIN_CONFIG   ("/domain/config"),
        DOMAIN_LEGAL    ("/domain/legal"),

        LOGIN           ("/login/token"),

        ACCOUNT_INFO                ("/notification/mailing/{userAlias}"),
        ACCOUNT_MAILS               ("/notification/mailing/{userId}/emails"),
        ACCOUNT_MAILS_SEND          ("/notification/mailing/{userId}/send"),
        ACCOUNT_NOTIFICATIONS       ("/notification/{userId}"),
        ACCOUNT_NOTIFICATIONS_CONFIG("/notification/{userId}/config"),

        ACCOUNT_MASS_MAILINGS                   ("/communication/massmailing/{userId}"),
        ACCOUNT_MASS_MAILING                    ("/communication/massmailing/{userId}/{campaignId}"),
        ACCOUNT_MASS_MAILING_SEND               ("/communication/massmailing/{userId}/{campaignId}/send"),
        ACCOUNT_MASS_MAILING_TRACKING           ("/communication/massmailing/{userId}/{campaignId}/trackingMails"),
        ACCOUNT_MASS_MAILING_REPORT             ("/communication/massmailing/{userId}/{campaignId}/trackingMails/report"),
        ACCOUNT_MASS_MAILING_CAMPAIGNS_REPORT   ("/communication/massmailing/{userId}/report"),
        ACCOUNT_MASS_MAILING_CONFIG             ("/communication/massmailing/{userId}/config"),

        ACCOUNT_CAMPAIGNS                       ("/communication/campaign/{userId}"),
        ACCOUNT_CAMPAIGN                        ("/communication/campaign/{userId}/{campaignId}"),


        ACCOUNT_DEBATES_ALL                     ("/communication/campaign/debate/"),
        ACCOUNT_DEBATES                         ("/communication/campaign/debate/{userId}"),
        ACCOUNT_DEBATE                          ("/communication/campaign/debate/{userId}/{debateId}"),
        ACCOUNT_DEBATE_REPORT                   ("/communication/campaign/debate/{userId}/{debateId}/report"),
        ACCOUNT_DEBATE_PROPOSALS                ("/communication/campaign/debate/{userId}/{debateId}/proposal"),
        ACCOUNT_DEBATE_PROPOSAL                 ("/communication/campaign/debate/{userId}/{debateId}/proposal/{proposalId}"),
        ACCOUNT_DEBATE_PROPOSAL_LIKE            ("/communication/campaign/debate/{userId}/{debateId}/proposal/{proposalId}/likes"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENTS        ("/communication/campaign/debate/{userId}/{debateId}/proposal/{proposalId}/comment"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENT         ("/communication/campaign/debate/{userId}/{debateId}/proposal/{proposalId}/comment/{commentId}"),
        ACCOUNT_DEBATE_PROPOSAL_COMMENT_VOTE    ("/communication/campaign/debate/{userId}/{debateId}/proposal/{proposalId}/comment/{commentId}/vote"),

        ACCOUNT_POSTS_ALL       ("/communication/campaign/post/"),
        ACCOUNT_POSTS           ("/communication/campaign/post/{userId}"),
        ACCOUNT_POST            ("/communication/campaign/post/{userId}/{postId}"),
        ACCOUNT_POST_LIKES      ("/communication/campaign/post/{userId}/{postId}/likes"),

        ACCOUNT_SURVEYS         ("/communication/campaign/survey/{userId}"),
        ACCOUNT_SURVEY          ("/communication/campaign/survey/{userId}/{surveyId}"),
        ACCOUNT_SURVEY_ANSWER   ("/communication/campaign/survey/{userId}/{surveyId}/question/{questionId}"),
        ACCOUNT_SURVEY_REPORT   ("/communication/campaign/survey/{userId}/{surveyId}/report"),

        ACCOUNT_ACTIVE_PARTICIPATORY_BUDGETS                            ("/communication/campaign/participatory-budget/"),
        ACCOUNT_PARTICIPATORY_BUDGETS                                   ("/communication/campaign/participatory-budget/{userId}"),
        ACCOUNT_PARTICIPATORY_BUDGET                                    ("/communication/campaign/participatory-budget/{userId}/{campaignId}"),
        ACCOUNT_PARTICIPATORY_BUDGET_REPORT                             ("/communication/campaign/participatory-budget/{userId}/{campaignId}/report"),
        ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSALS                 ("/communication/campaign/participatory-budget/{userId}/{campaignId}/proposal"),
        ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_VOTE             ("/communication/campaign/participatory-budget/{userId}/{campaignId}/proposal/{proposalId}/vote"),
        ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_SUPPORT          ("/communication/campaign/participatory-budget/{userId}/{campaignId}/proposal/{proposalId}/support"),
        ACCOUNT_PARTICIPATORY_BUDGET_DISTRICT_PROPOSAL_TECHNICAL_REVIEW ("/communication/campaign/participatory-budget/{userId}/{campaignId}/proposal/{proposalId}/technicalReview"),

        ACCOUNT_DISTRICT_PROPOSALS      ("/communication/campaign/participatory-budget/district-proposal/{userId}"),
        ACCOUNT_DISTRICT_PROPOSAL       ("/communication/campaign/participatory-budget/district-proposal/{userId}/{campaignId}"),

        ACCOUNT_EVENTS              ("/communication/campaign/event/{userId}"),
        ACCOUNT_EVENT               ("/communication/campaign/event/{userId}/{campaignId}"),
        ACCOUNT_EVENT_ADD_ASSISTANT ("/communication/campaign/event/{userId}/{campaignId}/assistant/{assistantId}"),
        ACCOUNT_EVENT_CHECK_IN      ("/communication/campaign/event/{userId}/{campaignId}/checkIn"),
        ACCOUNT_EVENT_REPORT        ("/communication/campaign/event/{userId}/{campaignId}/assistant/report"),

        USER_CONTACTS_CAMPAIGNS_ALL ("/user/{userId}/dashboard/campaigns"),

        SEARCH                  ("/search/"),
        SEARCH_INDEX_FULL       ("/search/index/full"),
        SEARCH_INDEX_DELTA      ("/search/index/delta"),
        SEARCH_SUGGEST          ("/search/suggest"),
        SEARCH_SUGGEST_USERS    ("/search/suggest/users"),
        SEARCH_SUGGEST_CAUSES   ("/search/suggest/causes"),

        ADMIN_MAILS_SEND("/admin/notification/mailing/send"),
        ADMIN_USER_CONFIG_SENDER("/admin/{userId}/config/mailing");

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
