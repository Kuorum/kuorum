package kuorum.util.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovyx.net.http.*
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import org.apache.commons.io.IOUtils
import org.apache.http.entity.InputStreamEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.kuorum.rest.model.error.RestServiceError
import org.springframework.beans.factory.annotation.Value

import java.nio.charset.StandardCharsets

class RestKuorumApiService {


    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query) throws KuorumException{
        String apiKey = CustomDomainResolver.apiToken
        RESTClient mailKuorumServices = new RESTClient(kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath, params)
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization": apiKey],
                query: query,
                requestContentType: ContentType.JSON
        )
        return response
    }

    def delete(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap)

        String path = apiMethod.buildUrl(apiPath,params)
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization":CustomDomainResolver.apiToken],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def get(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap, String adminApiKey = null) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap)

        String apiKey = adminApiKey?:CustomDomainResolver.apiToken
        String path = apiMethod.buildUrl(apiPath,params)
        def response = mailKuorumServices.get(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization":apiKey],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def patch(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, TypeReference typeToMap) throws KuorumException{
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params)
        def response = mailKuorumServices.patch(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization":CustomDomainResolver.apiToken],
                query:query,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    def put(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap, String adminApiKey = null) throws KuorumException {
        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap)
        String apiKey = adminApiKey?:CustomDomainResolver.apiToken
        String path = apiMethod.buildUrl(apiPath,params)
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization":apiKey],
                query:query,
                body: body,
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        return response
    }

    String putFile(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, File file, String fileName) throws KuorumException {
        String jsonText ='''{"parameter": [{"name":"LIST","file":"file0"}, {"name":"SEARCH", "value":"Build"}, {"name":"LABEL", "value":"2015/11/20"}, {"name":"UPDATEDB", "value":"TRUE"}  ]}'''

        def http = new HTTPBuilder(kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params)
        http.request(Method.PUT, ContentType.TEXT) {req->
            uri.path = path
            uri.query = query
            headers = ["User-Agent": "Kuorum Web", "Authorization":CustomDomainResolver.apiToken]

            MultipartEntityBuilder multipartRequestEntity = new MultipartEntityBuilder()
            multipartRequestEntity.addPart('file', new FileBody(file,org.apache.http.entity.ContentType.DEFAULT_BINARY, fileName))
//            multipartRequestEntity.addPart('json', new StringBody(jsonText))

            req.entity =  multipartRequestEntity.build()

            response.success = { resp, data ->
                // response text
                return data.getText()
            }
        }
    }

    void getFile(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, OutputStream outputStream){
        def http = new HTTPBuilder(kuorumRestServices)
        String path = apiMethod.buildUrl(apiPath,params)
        http.request(Method.GET, ContentType.BINARY) {req->
            uri.path = path
            headers = ["User-Agent": "Kuorum Web", "Authorization":CustomDomainResolver.apiToken]

            response.success = { resp, data ->
                // response text
                org.apache.commons.io.IOUtils.copy(data, outputStream);
//                return data;
            }
        }
    }




    def post(ApiMethod apiMethod, Map<String,String> params, Map<String,String> query, def body,TypeReference typeToMap) throws KuorumException{

        RESTClient mailKuorumServices = getRestMailKuorumServices(typeToMap)

//        encoderRegistry
        String path = apiMethod.buildUrl(apiPath,params)

        def response = mailKuorumServices.post(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "Authorization": CustomDomainResolver.apiToken],
                query: query,
                body: body,
                requestContentType: groovyx.net.http.ContentType.JSON
        )
        return response
    }


    private RESTClient getRestMailKuorumServices(TypeReference clazz){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        EncoderRegistry encoderRegistry = mailKuorumServices.getEncoder()
        encoderRegistry.putAt(groovyx.net.http.ContentType.JSON, {it ->
//            def builder = new groovy.json.JsonBuilder(); // Not use JacksonAnnotations
//            builder.content = it
//            String rawJson = builder.toString();

            ObjectMapper builder = new ObjectMapper()
//            String rawJson = builder.valueToTree(it).toString()
            String rawJson = builder.writeValueAsString(it)

            InputStreamEntity res = new InputStreamEntity(new ByteArrayInputStream(rawJson.getBytes(StandardCharsets.UTF_8)))
            res.setContentType(groovyx.net.http.ContentType.JSON.toString())
            return res
        })

        ParserRegistry parserRegistry = mailKuorumServices.getParser()
        parserRegistry.putAt(groovyx.net.http.ContentType.JSON, { HttpResponseDecorator resp ->
            def obj = null
            if (resp.status ==200){
                if(clazz != null){
                    String jsonString = IOUtils.toString(resp.getEntity().getContent(), "UTF-8")
                    ObjectMapper objectMapper = new ObjectMapper()
                    objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"))
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    obj = objectMapper.readValue(jsonString, clazz)
                }
                return obj
            }else{
                try{
                    log.warn(resp.getContext().getAttribute("http.request").getAt("original").toString());
                }catch(Throwable t){
                    log.warn("Error recovering original URL called to api to print it in logs")
                }
                String jsonString = IOUtils.toString(resp.getEntity().getContent(), "UTF-8")
                ObjectMapper objectMapper = new ObjectMapper()
                objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"))
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                RestServiceError serviceError = objectMapper.readValue(jsonString, RestServiceError.class)
                throw new KuorumException(serviceError.message, "error.api.${serviceError.code}", new ArrayList(serviceError.errorData?.values()?:[]))
            }
        })

        mailKuorumServices.handler.failure = { resp, data ->
            throw new KuorumException("No found")
        }
        return mailKuorumServices

    }

    enum ApiMethod{
        USER_STATS_REPUTATION               ('/user/{userId}/stats/reputation'),
        USER_STATS_REPUTATION_EVOLUTION     ('/user/{userId}/stats/reputation/evolution'),

        USER_CAUSES_SUPPORTED   ('/user/{userId}/causes/support'),
        USER_CAUSES_SUPPORT     ('/user/{userId}/causes/support/{causeName}'),

        USER                    ('/user/{userId}/'),
        USER_EXTRA_DATA         ('/user/{userId}/extraData'),
        USER_NEWS               ('/user/{userId}/news/'),
        USER_IMG_AVATAR         ('/user/{userId}/files/avatar'),
        USER_IMG_PROFILE        ('/user/{userId}/files/profile'),

        USER_DATA                   ('/user/{userId}/data/'),
        USER_VALIDATION_STATUS      ('/user/{userId}/validation/{campaignId}'),
        USER_VALIDATION_CENSUS      ('/user/{userId}/validation/{campaignId}/census'),
        USER_VALIDATION_PHONE       ('/user/{userId}/validation/{campaignId}/phone'),
        USER_VALIDATION_CODE        ('/user/{userId}/validation/{campaignId}/code'),

        USER_CONTACTS               ('/contacts/{userId}'),
        USER_CONTACT                ('/contacts/{userId}/{contactId}'),
        USER_CONTACT_PERSONAL_CODE  ('/contacts/{userId}/{contactId}/personalCode'),
        USER_CONTACT_ACTIVITY       ('/contacts/{userId}/{contactId}/activity'),
        USER_CONTACTS_TAGS          ('/contacts/{userId}/tags'),
        USER_CONTACTS_PERSONAL_CODE ('/contacts/{userId}/personalCodes'),
        USER_CONTACT_FILES          ("/contacts/{userId}/{contactId}/files"),
        USER_CONTACT_FILTERS        ('/contacts/{userId}/filters'),
        USER_CONTACT_FILTER         ("/contacts/{userId}/filters/{filterId}"),
        USER_CONTACT_SUBSCRIBE      ("/contacts/{userId}/suscribe"),
        USER_CONTACT_REPORT         ("/contacts/{userId}/report"),
        USER_CONTACT_SOCIAL_IMPORT  ("/contacts/social/{provider}/request"),

        CENSUS_LOGIN            ("/census/loginByCode/{censusCode}"),

        USER_FOLLOWER           ("/user/{userId}/follower/"),
        USER_FOLLOWER_FOLLOWING ("/user/{userId}/follower/following"),

        CAUSE_OPERATIONS        ("/cause/{causeName}"),
        CAUSE_USERS_SUPPORTING  ("/cause/{causeName}/supporting"),
        CAUSE_SUGGESTIONS       ("/cause/suggest"),

        REGION_GET      ("/geolocation/get"),
        REGION_SUGGEST  ("/geolocation/suggest"),
        REGION_FIND     ("/geolocation/find"),

        DOMAINS             ("/domain/"),
        DOMAIN              ("/domain/{domainName}/"),
        DOMAIN_CONFIG       ("/domain/{domainName}/config"),
        DOMAIN_LEGAL        ("/domain/{domainName}/legal"),
        DOMAIN_PAYMENT      ("/domain/{domainName}/payment"),
        DOMAIN_MAIL_CONFIG  ("/domain/{domainName}/mailing"),

        CUSTOMER_PLANS              ("/customer/plans"),

        LOGIN           ("/login/token"),

        ACCOUNT_MAILS_SEND          ("/notification/mailing/{userId}/send"),
        ACCOUNT_NOTIFICATIONS       ("/notification/{userId}"),
        ACCOUNT_NOTIFICATIONS_CONFIG("/notification/{userId}/config"),

        ACCOUNT_MASS_MAILINGS                   ("/communication/massmailing/{userId}"),
        ACCOUNT_MASS_MAILING                    ("/communication/massmailing/{userId}/{campaignId}"),
        ACCOUNT_MASS_MAILING_SEND               ("/communication/massmailing/{userId}/{newsletterId}/send"),
        ACCOUNT_MASS_MAILING_COPY               ("/communication/massmailing/{userId}/{campaignId}/copy"),
        ACCOUNT_MASS_MAILING_TRACKING           ("/communication/massmailing/{userId}/{campaignId}/trackingMails"),
        ACCOUNT_MASS_MAILING_TRACKING_RESEND    ("/communication/massmailing/{userId}/{campaignId}/trackingMails/resend/{trackingEmailId}"),
        ACCOUNT_MASS_MAILING_REPORT             ("/communication/massmailing/{userId}/{campaignId}/trackingMails/report"),
        ACCOUNT_MASS_MAILING_CAMPAIGNS_REPORT   ("/communication/massmailing/{userId}/report"),
        ACCOUNT_MASS_MAILING_CONFIG             ("/communication/massmailing/{userId}/config"),
        ACCOUNT_MASS_MAILING_ATTACHMENT         ("/communication/massmailing/{userId}/{campaignId}/files"),
        ACCOUNT_MASS_MAILING_REPORTS            ("/communication/massmailing/{userId}/{campaignId}/reports"),
        ACCOUNT_MASS_MAILING_REPORT_FILE        ("/communication/massmailing/{userId}/{campaignId}/reports/{fileName}"),
        ACCOUNT_MASS_MAILING_PICTURE            ("/communication/massmailing/{userId}/{campaignId}/picture"),

        CAMPAIGNS_DOMAIN                        ("/communication/campaign/"),
        ACCOUNT_CAMPAIGNS                       ("/communication/campaign/{userId}"),
        ACCOUNT_CAMPAIGN                        ("/communication/campaign/{userId}/{campaignId}"),
        ACCOUNT_CAMPAIGN_FILES                  ("/communication/campaign/{userId}/{campaignId}/files"),
        ACCOUNT_CAMPAIGN_REPORTS                ("/communication/campaign/{userId}/{campaignId}/reports"),
        ACCOUNT_CAMPAIGN_REPORT_FILE            ("/communication/campaign/{userId}/{campaignId}/reports/{fileName}"),
        ACCOUNT_CAMPAIGN_PICTURE                ("/communication/campaign/{userId}/{campaignId}/picture"),
        ACCOUNT_CAMPAIGN_PAUSE                  ("/communication/campaign/{userId}/{campaignId}/pause"),
        ACCOUNT_CAMPAIGN_GROUPS                 ("/communication/campaign/{userId}/{campaignId}/groups/{filterId}"),


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

        ACCOUNT_BULLETINS_ALL       ("/communication/campaign/bulletin"),
        ACCOUNT_BULLETINS           ("/communication/campaign/bulletin/{userId}"),
        ACCOUNT_BULLETIN            ("/communication/campaign/bulletin/{userId}/{campaignId}"),
        ACCOUNT_BULLETIN_COPY       ("/communication/campaign/bulletin/{userId}/{campaignId}/copy"),

        ACCOUNT_SURVEYS               ("/communication/campaign/survey/{userId}"),
        ACCOUNT_SURVEY                ("/communication/campaign/survey/{userId}/{surveyId}"),
        ACCOUNT_SURVEY_ANSWER         ("/communication/campaign/survey/{userId}/{surveyId}/question/{questionId}"),
        ACCOUNT_SURVEY_ANSWER_FILE    ("/communication/campaign/survey/{userId}/{surveyId}/question/{questionId}/{questionOptionId}/file"),
        ACCOUNT_SURVEY_REPORT_STATS   ("/communication/campaign/survey/{userId}/{surveyId}/report/stats"),
        ACCOUNT_SURVEY_REPORT_RAW     ("/communication/campaign/survey/{userId}/{surveyId}/report/rawData"),

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

        ACCOUNT_PETITIONS_ALL       ("/communication/campaign/petition/"),
        ACCOUNT_PETITIONS           ("/communication/campaign/petition/{userId}"),
        ACCOUNT_PETITION            ("/communication/campaign/petition/{userId}/{petitionId}"),
        ACCOUNT_PETITION_SIGN       ("/communication/campaign/petition/{userId}/{petitionId}/sign"),

        ACCOUNT_EVENTS              ("/communication/campaign/event/{userId}"),
        ACCOUNT_EVENT               ("/communication/campaign/event/{userId}/{campaignId}"),
        ACCOUNT_EVENT_ADD_ASSISTANT ("/communication/campaign/event/{userId}/{campaignId}/assistant/{assistantId}"),
        ACCOUNT_EVENT_CHECK_IN      ("/communication/campaign/event/{userId}/{campaignId}/checkIn"),
        ACCOUNT_EVENT_REPORT        ("/communication/campaign/event/{userId}/{campaignId}/assistant/report"),

        ACCOUNT_MESSAGE_SEND        ("/communication/message/{userId}"),

        USER_CONTACTS_CAMPAIGNS_ALL ("/user/{userId}/dashboard/campaigns"),

        SEARCH                  ("/search/"),
        SEARCH_INDEX_FULL       ("/search/index/full"),
        SEARCH_INDEX_DELTA      ("/search/index/delta"),
        SEARCH_SUGGEST          ("/search/suggest"),
        SEARCH_SUGGEST_USERS    ("/search/suggest/users"),
        SEARCH_SUGGEST_CAUSES   ("/search/suggest/causes"),

        ADMIN_MAILS_SEND("/admin/notification/mailing/send")

        String url

        ApiMethod(String url){
            this.url = url
        }

        String buildUrl(String contextPath, Map<String,String> params){
            String builtUrl = url
            params.each{ k, v -> builtUrl = builtUrl.replaceAll("\\{${k}}", v) }
            (contextPath+builtUrl).toString()
        }
    }
}
