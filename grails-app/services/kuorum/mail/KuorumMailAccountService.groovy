package kuorum.mail

import grails.transaction.Transactional
import groovyx.net.http.RESTClient
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO
import org.kuorum.rest.model.notification.MailsMessageRSDTO

@Transactional
class KuorumMailAccountService {


    RestKuorumApiService restKuorumApiService

    MailsMessageRSDTO getUserMails(KuorumUser user) {
        KuorumMailAccountDetailsRSDTO account = getAccountDetails(user);
        MailsMessageRSDTO mails
        if (account?.active){
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_MAILS,
                    [userAlias:user.alias],
                    [:]
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
            def response = restKuorumApiService.patch(
                    RestKuorumApiService.ApiMethod.ACCOUNT_INFO,
                    [userAlias:currentAlias],
                    [newAlias:newAlias]
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
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_INFO,
                    [userAlias:userAlias],
                    [:]
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
        if (user.alias){
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.ACCOUNT_INFO,
                    [userAlias:user.alias],
                    [:]
            )
            KuorumMailAccountDetailsRSDTO account = new KuorumMailAccountDetailsRSDTO(response.data)
            return account
        }else{
            return null;
        }
    }

    void deleteAccount(KuorumUser user){
        if (user.alias){
            def response = restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.ACCOUNT_INFO,
                    [userAlias:user.alias],
                    [:]
            )
        }
    }
}
