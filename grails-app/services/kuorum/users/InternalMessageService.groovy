package kuorum.users

import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.message.NewMessageRDTO

@Transactional
class InternalMessageService {

    RestKuorumApiService restKuorumApiService

    def sendInternalMessage(KuorumUserSession sender, NewMessageRDTO newMessageRDTO) {
        Map<String, String> params = [userId: sender.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_MESSAGE_SEND,
                params,
                query,
                newMessageRDTO,
                null
        )
        null
  }
}
