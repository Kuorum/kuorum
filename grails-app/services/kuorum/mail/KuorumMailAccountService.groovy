package kuorum.mail

import grails.transaction.Transactional
import groovyx.net.http.RESTClient
import kuorum.users.KuorumUser
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.notification.MailsMessageRSDTO
import org.springframework.beans.factory.annotation.Value

@Transactional
class KuorumMailAccountService {

    @Value('${kuorum.rest.url}')
    String kuorumRestServices

    @Value('${kuorum.rest.apiPath}')
    String apiPath

    @Value('${kuorum.rest.apiKey}')
    String kuorumRestApiKey

    String ACCOUNT_INFO="/notification/mailing/{userAlias}"
    String ACCOUNT_MAILS="/notification/mailing/{userAlias}/emails"

    MailsMessageRSDTO getUserMails(KuorumUser user) {
        KuorumMailAccountDetailsRSDTO account = getAccountDetails(user);
        MailsMessageRSDTO mails
        if (account?.active){
            RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
            String path = buildUrl(ACCOUNT_MAILS, [userAlias:user.alias]);
            def response = mailKuorumServices.get(
                    path: path,
                    headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                    query:[:],
                    requestContentType : groovyx.net.http.ContentType.JSON
            )
            mails =  new MailsMessageRSDTO(response.data)

        }else{
            mails = new MailsMessageRSDTO()
            mails.setTotalMails(0);
            mails.setMails([])
        }
        return mails;
    }

    KuorumMailAccountDetailsRSDTO changeAliasAccount(String currentAlias, String newAlias){
        KuorumMailAccountDetailsRSDTO account = getAccountDetailsWithAlias(currentAlias);
        if (account){
            RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
            String path = buildUrl(ACCOUNT_INFO, [userAlias:currentAlias]);
            def response = mailKuorumServices.patch(
                    path: path,
                    headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                    query:[newAlias:newAlias],
                    requestContentType : groovyx.net.http.ContentType.JSON
            )
            if (response.data){
                account = new KuorumMailAccountDetailsRSDTO(response.data)
            }
        }
        return account;
    }

    public KuorumMailAccountDetailsRSDTO getAccountDetails(KuorumUser user){
        return getAccountDetailsWithAlias(user.alias)
    }

    public KuorumMailAccountDetailsRSDTO getAccountDetailsWithAlias(String userAlias){
        if (userAlias){
            RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
            String path = buildUrl(ACCOUNT_INFO, [userAlias:userAlias]);
            def response = mailKuorumServices.get(
                    path: path,
                    headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                    query:[:],
                    requestContentType : groovyx.net.http.ContentType.JSON
            )
            KuorumMailAccountDetailsRSDTO account = null;
            if (response.data){
                account = new KuorumMailAccountDetailsRSDTO(response.data)
            }
            return account;
        }else{
            return null;
        }
    }

    KuorumMailAccountDetailsRSDTO activateAccount(KuorumUser user){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(ACCOUNT_INFO, [userAlias:user.alias]);
        def response = mailKuorumServices.put(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        KuorumMailAccountDetailsRSDTO account = new KuorumMailAccountDetailsRSDTO(response.data)
        account
    }

    KuorumMailAccountDetailsRSDTO deleteAccount(KuorumUser user){
        RESTClient mailKuorumServices = new RESTClient( kuorumRestServices)
        String path = buildUrl(ACCOUNT_INFO, [userAlias:user.alias]);
        def response = mailKuorumServices.delete(
                path: path,
                headers: ["User-Agent": "Kuorum Web", "token":kuorumRestApiKey],
                query:[:],
                requestContentType : groovyx.net.http.ContentType.JSON
        )
        KuorumMailAccountDetailsRSDTO account = new KuorumMailAccountDetailsRSDTO(response.data)
        account
    }

    private String buildUrl(String path, Map<String,String> params){
        params.each{ k, v -> path = path.replaceAll("\\{${k}}", v) }
        apiPath+path
    }
}
